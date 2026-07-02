package com.cahuinlabs.GestionReuniones.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cahuinlabs.GestionReuniones.models.entities.BitReunionApoderado;
import com.cahuinlabs.GestionReuniones.models.entities.BitReunionGeneral;
import com.cahuinlabs.GestionReuniones.models.entities.BitReunionIndividual;
import com.cahuinlabs.GestionReuniones.models.request.ActualizarApoderadoRequest;
import com.cahuinlabs.GestionReuniones.models.request.ActualizarFirmasRequest;
import com.cahuinlabs.GestionReuniones.models.request.BitReunionApoderadoRequest;
import com.cahuinlabs.GestionReuniones.models.request.CalendarioEventoRequest;
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

    public GestionReunionesService(@Qualifier("autenticacionRestClient") RestClient autenticacionRestClient,
                                   @Qualifier("calendarioRestClient") RestClient calendarioRestClient) {
        this.autenticacionRestClient = autenticacionRestClient;
        this.calendarioRestClient = calendarioRestClient;
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
        BitReunionApoderado guardada = baseRepository.save(base);
        sincronizarConCalendario(guardada, "Reunión con apoderado", guardada.getBitReuCompromisos());
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
        base = baseRepository.save(base);

        //guardar el desglose confidencial de la entrevista individual
        BitReunionIndividual individual = new BitReunionIndividual();
        individual.setBitReuIndMotivReu(request.getBitReuIndMotivReu());
        individual.setBitReuIndTemTrat(request.getBitReuIndTemTrat());
        individual.setBitReuIndFirmaDoc(0); // Por defecto inicia sin firmar
        individual.setBitReuIndFirmaApo(0);
        individual.setBitReunionApoderado(base);

        BitReunionIndividual guardada = individualRepository.save(individual);
        sincronizarConCalendario(base, "Entrevista individual", guardada.getBitReuIndMotivReu() + " - " + guardada.getBitReuIndTemTrat());
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

        // guardar la bitacora general de acuerdos institucionales
        BitReunionGeneral general = new BitReunionGeneral();
        general.setBitReuGenTipReu(request.getBitReuGenTipReu());
        general.setBitReuGenComunicEmi(request.getBitReuGenComunicEmi());
        general.setBitReuGenAcuerTrat(request.getBitReuGenAcuerTrat());
        general.setBitReuGenObs(request.getBitReuGenObs());
        general.setBitReunionApoderado(base);

        BitReunionGeneral guardada = generalRepository.save(general);
        sincronizarConCalendario(base, "Reunión general - " + guardada.getBitReuGenTipReu(), guardada.getBitReuGenComunicEmi());
        return guardada;
    }

    // obtiene todas las reuniones base registradas con apoderados
    @Transactional
    public List<BitReunionApoderado> listarReunionesApoderado() {
        return baseRepository.findAll();
    }

    // obtiene una reunion base por su identificador
    public BitReunionApoderado obtenerApoderadoPorId(Long idBitReu) {
        return baseRepository.findById(idBitReu)
                .orElseThrow(() -> new EntityNotFoundException("Bitácora no encontrada: " + idBitReu));
    }

    // obtiene todas las reuniones individuales registradas
    @Transactional
    public List<BitReunionIndividual> listarIndividuales() {
        return individualRepository.findAll();
    }

    // obtiene todas las reuniones generales registradas
    @Transactional
    public List<BitReunionGeneral> listarGenerales() {
        return generalRepository.findAll();
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

    // obtiene todas las reuniones registradas por un funcionario/docente especifico por su rut
    public List<BitReunionApoderado> listarPorFuncionario(Long funcionarioRut) {
        return baseRepository.findByDocenteUsuRut(funcionarioRut);
    }

    // obtiene los detalles completos de una reunion individual especifica
    public BitReunionIndividual obtenerDetalleIndividual(Long idBitReuInd) {
        return individualRepository.findById(idBitReuInd)
                .orElseThrow(() -> new EntityNotFoundException("Reunión individual no encontrada: " + idBitReuInd));
    }

    // obtiene los detalles completos de una reunion general especifica
    public BitReunionGeneral obtenerDetalleGeneral(Long idBitReuGen) {
        return generalRepository.findById(idBitReuGen)
                .orElseThrow(() -> new EntityNotFoundException("Reunión general no encontrada: " + idBitReuGen));
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

    private void sincronizarConCalendario(BitReunionApoderado base, String titulo, String descripcion) {
        try {
            CalendarioEventoRequest evento = new CalendarioEventoRequest();
            evento.setTituloEvento(titulo);
            evento.setTipoEvento("Reunión");
            evento.setFechaInicio(base.getBitReuFec());
            evento.setFechaFin(base.getBitReuFec());
            evento.setIdMuralDigital(null);
            evento.setIdAsignatura(1L);
            evento.setDescripcionEvento(descripcion);

            calendarioRestClient.post()
                    .uri("/api/calendarios")
                    .body(evento)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            logger.warn("No se pudo sincronizar la reunión con el calendario: {}", e.getMessage());
        }
    }
}