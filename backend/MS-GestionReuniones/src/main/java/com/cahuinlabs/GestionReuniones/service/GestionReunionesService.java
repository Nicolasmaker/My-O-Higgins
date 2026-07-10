package com.cahuinlabs.GestionReuniones.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cahuinlabs.GestionReuniones.dto.ApoderadoDTO;
import com.cahuinlabs.GestionReuniones.dto.BitReunionApoderadoResponseDTO;
import com.cahuinlabs.GestionReuniones.dto.BitReunionGeneralResponseDTO;
import com.cahuinlabs.GestionReuniones.dto.BitReunionIndividualResponseDTO;
import com.cahuinlabs.GestionReuniones.dto.CalendarioEstudiantilDTO;
import com.cahuinlabs.GestionReuniones.dto.EstudianteDTO;
import com.cahuinlabs.GestionReuniones.models.entities.BitReunionApoderado;
import com.cahuinlabs.GestionReuniones.models.entities.BitReunionGeneral;
import com.cahuinlabs.GestionReuniones.models.entities.BitReunionIndividual;
import com.cahuinlabs.GestionReuniones.models.request.ActualizarApoderadoRequest;
import com.cahuinlabs.GestionReuniones.models.request.ActualizarFirmasRequest;
import com.cahuinlabs.GestionReuniones.models.request.ActualizarGeneralRequest;
import com.cahuinlabs.GestionReuniones.models.request.ActualizarIndividualRequest;
import com.cahuinlabs.GestionReuniones.models.request.BitReunionApoderadoRequest;
import com.cahuinlabs.GestionReuniones.models.request.CalendarioEventoRequest;
import com.cahuinlabs.GestionReuniones.models.request.ConfirmarReunionRequest;
import com.cahuinlabs.GestionReuniones.models.request.MensajeRequest;
import com.cahuinlabs.GestionReuniones.models.request.ReunionGeneralRequest;
import com.cahuinlabs.GestionReuniones.models.request.ReunionIndividualRequest;

import jakarta.persistence.EntityNotFoundException;
import com.cahuinlabs.GestionReuniones.repository.BitReunionApoderadoRepository;
import com.cahuinlabs.GestionReuniones.repository.BitReunionGeneralRepository;
import com.cahuinlabs.GestionReuniones.repository.BitReunionIndividualRepository;
import com.cahuinlabs.GestionReuniones.dto.FuncionarioDTO;

import jakarta.transaction.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;



@Service
// encargado de toda la logica de negocio para gestionar reuniones
// crea, actualiza y consulta reuniones con apoderados base, individual y general
public class GestionReunionesService {

    private static final Logger logger = LoggerFactory.getLogger(GestionReunionesService.class);

    @Autowired
    private BitReunionApoderadoRepository baseRepository;

    @Autowired
    private BitReunionIndividualRepository individualRepository;

    @Autowired
    private BitReunionGeneralRepository generalRepository;

    // RestClient para comunicarse con el microservicio de Autenticacion
    private final RestClient autenticacionRestClient;
    private final RestClient calendarioRestClient;
    private final RestClient mensajeriaRestClient;

    public GestionReunionesService(@Qualifier("autenticacionRestClient") RestClient autenticacionRestClient,
                                   @Qualifier("calendarioRestClient") RestClient calendarioRestClient,
                                   @Qualifier("mensajeriaRestClient") RestClient mensajeriaRestClient) {
        this.autenticacionRestClient = autenticacionRestClient;
        this.calendarioRestClient = calendarioRestClient;
        this.mensajeriaRestClient = mensajeriaRestClient;
    }

    // crea una reunion base con apoderado, guarda fecha, compromisos, observaciones y docente
    @Transactional
    public BitReunionApoderado registrarReunionApoderado(BitReunionApoderadoRequest request) {

        // Verificamos que el funcionario exista en el microservicio de Autenticacion
        if (!existeFuncionario(request.getDocenteUsuRut())) {
            throw new IllegalArgumentException("No se puede registrar la reunion: el funcionario con RUT " + request.getDocenteUsuRut() + " no existe en el sistema.");
        }

        BitReunionApoderado base = new BitReunionApoderado();
        base.setBitReuFec(request.getBitReuFec());
        base.setBitReuCompromisos(request.getBitReuCompromisos());
        base.setBitReuObs(request.getBitReuObs());
        base.setDocenteUsuRut(request.getDocenteUsuRut());
        base.setApoderadoUsuRut(request.getApoderadoUsuRut());
        base.setEstadoConfirmacion("PENDIENTE");
        BitReunionApoderado guardada = baseRepository.save(base);
        // El sync con el calendario se hace recien cuando el apoderado ACEPTA (ver confirmarReunion);
        // al crear solo se notifica.
        notificarApoderado(guardada, "Nueva reunión agendada", "Se agendó una reunión.");
        return guardada;
    }

    // crea una reunion individual docente/inspector con apoderado
    // primero guarda la base, luego los detalles de la entrevista confidencial
    @Transactional
    public BitReunionIndividual registrarReunionIndividual(ReunionIndividualRequest request) {

        // Verificamos que el funcionario exista en el microservicio de Autenticacion
        if (!existeFuncionario(request.getDocenteUsuRut())) {
            throw new IllegalArgumentException("No se puede registrar la reunion: el funcionario con RUT " + request.getDocenteUsuRut() + " no existe en el sistema.");
        }

        //  guardar la bitcora base
        BitReunionApoderado base = new BitReunionApoderado();
        base.setBitReuFec(request.getBitReuFec());
        base.setBitReuCompromisos(request.getBitReuCompromisos());
        base.setBitReuObs(request.getBitReuObs());
        base.setDocenteUsuRut(request.getDocenteUsuRut());
        base.setApoderadoUsuRut(request.getApoderadoUsuRut());
        base.setAlumnoRut(request.getAlumnoRut());
        base.setEstadoConfirmacion("PENDIENTE");
        base = baseRepository.save(base);

        //guardar el desglose confidencial de la entrevista individual
        BitReunionIndividual individual = new BitReunionIndividual();
        individual.setBitReuIndMotivReu(request.getBitReuIndMotivReu());
        individual.setBitReuIndTemTrat(request.getBitReuIndTemTrat());
        individual.setBitReuIndFirmaDoc(0); // Por defecto inicia sin firmar
        individual.setBitReuIndFirmaApo(0);
        individual.setIdAnotacion(request.getIdAnotacion());
        individual.setBitReunionApoderado(base);

        BitReunionIndividual guardada = individualRepository.save(individual);
        // El sync con el calendario se hace recien cuando el apoderado ACEPTA (ver confirmarReunion);
        // al crear solo se notifica.
        notificarApoderado(base, "Nueva entrevista individual agendada", "Motivo: " + request.getBitReuIndMotivReu());
        return guardada;
    }

    // crea una reunion general acta de coordinacion con toda la institucion
    // primero guarda la base, luego los detalles de acuerdos institucionales
    @Transactional
    public BitReunionGeneral registrarReunionGeneral(ReunionGeneralRequest request) {

        // Verificamos que el funcionario exista en el microservicio de Autenticacion
        if (!existeFuncionario(request.getDocenteUsuRut())) {
            throw new IllegalArgumentException("No se puede registrar la reunion: el funcionario con RUT " + request.getDocenteUsuRut() + " no existe en el sistema.");
        }

        BitReunionApoderado base = new BitReunionApoderado();
        base.setBitReuFec(request.getBitReuFec());
        base.setBitReuCompromisos(request.getBitReuCompromisos());
        base.setBitReuObs(request.getBitReuObs());
        base.setDocenteUsuRut(request.getDocenteUsuRut());
        base = baseRepository.save(base);

        // guardar la bitacora general de la reunion de curso (comunicado/acuerdos se llenan
        // despues con "Completar acta"; al agendar solo se conoce el curso y el tipo)
        BitReunionGeneral general = new BitReunionGeneral();
        general.setBitReuGenTipReu(request.getBitReuGenTipReu());
        general.setBitReuGenComunicEmi(request.getBitReuGenComunicEmi());
        general.setBitReuGenAcuerTrat(request.getBitReuGenAcuerTrat());
        general.setBitReuGenObs(request.getBitReuGenObs());
        general.setCursoId(request.getCursoId());
        general.setBitReunionApoderado(base);

        BitReunionGeneral guardada = generalRepository.save(general);
        sincronizarConCalendario(
                base,
                "Reunión de curso - " + guardada.getBitReuGenTipReu(),
                "Reunión general de curso agendada",
                guardada.getCursoId());
        return guardada;
    }

    // obtiene todas las reuniones base registradas con apoderados, enriquecidas con RUT+DV y nombre
    @Transactional
    public List<BitReunionApoderadoResponseDTO> listarReunionesApoderado() {
        return enriquecerApoderados(baseRepository.findAll());
    }

    // obtiene una reunion base por su identificador, enriquecida con RUT+DV y nombre
    public BitReunionApoderadoResponseDTO obtenerApoderadoPorId(Long idBitReu) {
        BitReunionApoderado base = baseRepository.findById(idBitReu)
                .orElseThrow(() -> new EntityNotFoundException("Bitácora no encontrada: " + idBitReu));
        return enriquecerApoderado(base);
    }

    // obtiene todas las reuniones individuales registradas, enriquecidas con RUT+DV y nombre
    @Transactional
    public List<BitReunionIndividualResponseDTO> listarIndividuales() {
        return individualRepository.findAll().stream().map(this::enriquecerIndividual).toList();
    }

    // obtiene todas las reuniones generales registradas, enriquecidas con RUT+DV y nombre
    @Transactional
    public List<BitReunionGeneralResponseDTO> listarGenerales() {
        return generalRepository.findAll().stream().map(this::enriquecerGeneral).toList();
    }

    // actualiza estado de firmas cambia de 0 (sin firmar) a 1 (firmado)
    @Transactional
    public BitReunionIndividual actualizarFirmas(Long idBitReuInd, ActualizarFirmasRequest request) {
        BitReunionIndividual individual = individualRepository.findById(idBitReuInd)
                .orElseThrow(() -> new EntityNotFoundException("Entrevista individual no encontrada: " + idBitReuInd));
        individual.setBitReuIndFirmaDoc(request.getFirmaDoc());
        individual.setBitReuIndFirmaApo(request.getFirmaApo());
        return individualRepository.save(individual);
    }

    // actualiza compromisos u observaciones de una reunion sin cambiar fecha ni autor
    @Transactional
    public BitReunionApoderado actualizarApoderado(Long idBitReu, ActualizarApoderadoRequest request) {
        BitReunionApoderado base = baseRepository.findById(idBitReu)
                .orElseThrow(() -> new EntityNotFoundException("Bitácora no encontrada: " + idBitReu));
        base.setBitReuCompromisos(request.getBitReuCompromisos());
        base.setBitReuObs(request.getBitReuObs());
        return baseRepository.save(base);
    }

    // completa la bitácora de una entrevista individual (temas tratados) tras la reunión
    @Transactional
    public BitReunionIndividual actualizarIndividual(Long idBitReuInd, ActualizarIndividualRequest request) {
        BitReunionIndividual individual = individualRepository.findById(idBitReuInd)
                .orElseThrow(() -> new EntityNotFoundException("Entrevista individual no encontrada: " + idBitReuInd));
        individual.setBitReuIndTemTrat(request.getBitReuIndTemTrat());
        return individualRepository.save(individual);
    }

    // completa el acta de una reunión general (comunicado/acuerdos/obs) tras la reunión
    @Transactional
    public BitReunionGeneral actualizarGeneral(Long idBitReuGen, ActualizarGeneralRequest request) {
        BitReunionGeneral general = generalRepository.findById(idBitReuGen)
                .orElseThrow(() -> new EntityNotFoundException("Reunión general no encontrada: " + idBitReuGen));
        general.setBitReuGenComunicEmi(request.getBitReuGenComunicEmi());
        general.setBitReuGenAcuerTrat(request.getBitReuGenAcuerTrat());
        general.setBitReuGenObs(request.getBitReuGenObs());
        return generalRepository.save(general);
    }

    // obtiene todas las reuniones registradas por un funcionario/docente especifico por su rut,
    // enriquecidas con RUT+DV y nombre
    public List<BitReunionApoderadoResponseDTO> listarPorFuncionario(Long funcionarioRut) {
        return enriquecerApoderados(baseRepository.findByDocenteUsuRut(funcionarioRut));
    }

    // el apoderado acepta o rechaza la citación. Solo al ACEPTAR se sincroniza con el
    // calendario (antes se creaba el evento igual al crear la reunión, sin importar si
    // el apoderado iba a asistir).
    @Transactional
    public BitReunionApoderado confirmarReunion(Long idBitReu, ConfirmarReunionRequest request) {
        String estado = request.getEstadoConfirmacion();
        if (!"ACEPTADA".equals(estado) && !"RECHAZADA".equals(estado)) {
            throw new IllegalArgumentException("estadoConfirmacion debe ser ACEPTADA o RECHAZADA");
        }

        BitReunionApoderado base = baseRepository.findById(idBitReu)
                .orElseThrow(() -> new EntityNotFoundException("Bitácora no encontrada: " + idBitReu));
        base.setEstadoConfirmacion(estado);

        if ("ACEPTADA".equals(estado)) {
            String titulo;
            String descripcion;
            var individualOpt = individualRepository.findByBitReunionApoderado_IdBitReu(base.getIdBitReu());
            if (individualOpt.isPresent()) {
                titulo = "Entrevista individual";
                descripcion = "Motivo: " + individualOpt.get().getBitReuIndMotivReu();
            } else {
                titulo = "Reunión con apoderado";
                descripcion = "Reunión aceptada por el apoderado";
            }
            sincronizarConCalendario(base, titulo, descripcion, null);
        }

        return baseRepository.save(base);
    }

    // obtiene los detalles completos de una reunion individual especifica, enriquecida
    public BitReunionIndividualResponseDTO obtenerDetalleIndividual(Long idBitReuInd) {
        BitReunionIndividual individual = individualRepository.findById(idBitReuInd)
                .orElseThrow(() -> new EntityNotFoundException("Reunión individual no encontrada: " + idBitReuInd));
        return enriquecerIndividual(individual);
    }

    // obtiene los detalles completos de una reunion general especifica, enriquecida
    public BitReunionGeneralResponseDTO obtenerDetalleGeneral(Long idBitReuGen) {
        BitReunionGeneral general = generalRepository.findById(idBitReuGen)
                .orElseThrow(() -> new EntityNotFoundException("Reunión general no encontrada: " + idBitReuGen));
        return enriquecerGeneral(general);
    }

    // metodo privado que pregunta al microservicio de Autenticacion si el funcionario existe
    // devuelve true si existe, false si no fue encontrado
    private boolean existeFuncionario(Long rut) {
        try {
            FuncionarioDTO funcionario = autenticacionRestClient.get()
                    .uri("/funcionarios/{rut}", rut)
                    .retrieve()
                    .body(FuncionarioDTO.class);

            return funcionario != null;
        } catch (HttpClientErrorException.NotFound e) {
            // el microservicio de Autenticacion respondio 404, el funcionario no existe
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al comunicarse con el microservicio de Autenticacion: " + e.getMessage());
        }
    }

    // Mapea una lista de bitacoras base crudas a su version enriquecida con RUT+DV y nombre.
    private List<BitReunionApoderadoResponseDTO> enriquecerApoderados(List<BitReunionApoderado> bases) {
        return bases.stream().map(this::enriquecerApoderado).toList();
    }

    // Enriquece una bitacora base con DV y nombre del docente, apoderado y alumno,
    // resueltos desde Autenticacion. Si algun dato no se encuentra o el microservicio
    // externo falla, se omite ese dato (queda null) en vez de romper el listado completo.
    private BitReunionApoderadoResponseDTO enriquecerApoderado(BitReunionApoderado base) {
        FuncionarioDTO docente = obtenerFuncionario(base.getDocenteUsuRut());
        ApoderadoDTO apoderado = base.getApoderadoUsuRut() != null ? obtenerApoderado(base.getApoderadoUsuRut()) : null;
        EstudianteDTO alumno = base.getAlumnoRut() != null ? obtenerEstudiante(base.getAlumnoRut()) : null;

        return new BitReunionApoderadoResponseDTO(
                base.getIdBitReu(),
                base.getBitReuFec(),
                base.getBitReuCompromisos(),
                base.getBitReuObs(),
                base.getDocenteUsuRut(),
                docente != null ? docente.dv() : null,
                docente != null ? docente.nombre() : null,
                docente != null ? docente.apellido() : null,
                base.getApoderadoUsuRut(),
                apoderado != null ? apoderado.dv() : null,
                apoderado != null ? apoderado.nombre() : null,
                apoderado != null ? apoderado.apellido() : null,
                base.getAlumnoRut(),
                alumno != null ? alumno.dv() : null,
                alumno != null ? alumno.nombre() : null,
                alumno != null ? alumno.apellido() : null,
                base.getEstadoConfirmacion(),
                base.getIdCalEst()
        );
    }

    private BitReunionIndividualResponseDTO enriquecerIndividual(BitReunionIndividual individual) {
        return new BitReunionIndividualResponseDTO(
                individual.getIdBitReuInd(),
                individual.getBitReuIndMotivReu(),
                individual.getBitReuIndTemTrat(),
                individual.getBitReuIndFirmaDoc(),
                individual.getBitReuIndFirmaApo(),
                individual.getIdAnotacion(),
                enriquecerApoderado(individual.getBitReunionApoderado())
        );
    }

    private BitReunionGeneralResponseDTO enriquecerGeneral(BitReunionGeneral general) {
        return new BitReunionGeneralResponseDTO(
                general.getBitReuGen(),
                general.getBitReuGenTipReu(),
                general.getBitReuGenComunicEmi(),
                general.getBitReuGenAcuerTrat(),
                general.getBitReuGenObs(),
                general.getCursoId(),
                enriquecerApoderado(general.getBitReunionApoderado())
        );
    }

    // Obtiene los datos del funcionario en Autenticacion. Devuelve null si no existe o si el
    // microservicio no responde.
    private FuncionarioDTO obtenerFuncionario(Long rut) {
        try {
            return autenticacionRestClient.get()
                    .uri("/funcionarios/{rut}", rut)
                    .retrieve()
                    .body(FuncionarioDTO.class);
        } catch (Exception e) {
            return null;
        }
    }

    // Obtiene los datos del apoderado en Autenticacion. Devuelve null si no existe o si el
    // microservicio no responde.
    private ApoderadoDTO obtenerApoderado(Long rut) {
        try {
            return autenticacionRestClient.get()
                    .uri("/apoderados/{rut}", rut)
                    .retrieve()
                    .body(ApoderadoDTO.class);
        } catch (Exception e) {
            return null;
        }
    }

    // Obtiene los datos del estudiante en Autenticacion. Devuelve null si no existe o si el
    // microservicio no responde.
    private EstudianteDTO obtenerEstudiante(Long rut) {
        try {
            return autenticacionRestClient.get()
                    .uri("/estudiantes/{rut}", rut)
                    .retrieve()
                    .body(EstudianteDTO.class);
        } catch (Exception e) {
            return null;
        }
    }

    private void sincronizarConCalendario(BitReunionApoderado base, String titulo, String descripcion, Long cursoId) {
        try {
            CalendarioEventoRequest evento = new CalendarioEventoRequest();
            evento.setTituloEvento(titulo);
            evento.setTipoEvento("Reunión");
            evento.setFechaInicio(base.getBitReuFec());
            evento.setFechaFin(base.getBitReuFec());
            evento.setIdMuralDigital(null);
            evento.setIdAsignatura(1L);
            evento.setCursoId(cursoId);
            evento.setDocenteUsuRut(base.getDocenteUsuRut());
            evento.setApoderadoUsuRut(base.getApoderadoUsuRut());
            evento.setAlumnoRut(base.getAlumnoRut());
            evento.setDescripcionEvento(descripcion);

            CalendarioEstudiantilDTO creado = calendarioRestClient.post()
                    .uri("/api/calendarios")
                    .body(evento)
                    .retrieve()
                    .body(CalendarioEstudiantilDTO.class);

            if (creado != null) {
                base.setIdCalEst(creado.getIdCalEst());
                baseRepository.save(base);
            }
        } catch (Exception e) {
            logger.warn("No se pudo sincronizar la reunión con el calendario: {}", e.getMessage());
        }
    }

    // Notifica al apoderado por Mensajería que se agendó una reunión. Best-effort: si falla,
    // solo se registra en el log (no bloquea la creación de la reunión).
    private void notificarApoderado(BitReunionApoderado base, String asunto, String detalle) {
        if (base.getApoderadoUsuRut() == null) {
            return;
        }
        try {
            MensajeRequest mensaje = new MensajeRequest();
            mensaje.setRemitenteRut(base.getDocenteUsuRut());
            mensaje.setDestinatarioRut(base.getApoderadoUsuRut());
            mensaje.setAsunto(asunto);
            mensaje.setContenido("Se ha agendado una reunión para el " + base.getBitReuFec() + ". " + detalle);

            mensajeriaRestClient.post()
                    .uri("/api/mensajeria/enviar")
                    .body(mensaje)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            logger.warn("No se pudo notificar al apoderado: {}", e.getMessage());
        }
    }
}