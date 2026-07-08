package com.cahuinlabs.hojadevida.config;

import com.cahuinlabs.hojadevida.dto.ApoderadoDTO;
import com.cahuinlabs.hojadevida.model.AntecedentesAcademicos;
import com.cahuinlabs.hojadevida.model.AntecedentesApoderado;
import com.cahuinlabs.hojadevida.model.AntecedentesMedicos;
import com.cahuinlabs.hojadevida.model.HojaVidaEstudiante;
import com.cahuinlabs.hojadevida.repository.AntecedentesAcademicosRepository;
import com.cahuinlabs.hojadevida.repository.AntecedentesApoderadoRepository;
import com.cahuinlabs.hojadevida.repository.AntecedentesMedicosRepository;
import com.cahuinlabs.hojadevida.repository.HojaVidaRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

    private final HojaVidaRepository hojaVidaRepository;
    private final AntecedentesAcademicosRepository academicosRepository;
    private final AntecedentesApoderadoRepository apoderadoRepository;
    private final AntecedentesMedicosRepository medicosRepository;
    private final RestClient autenticacionRestClient;

    // RUTs reales sembrados por MS-Autenticacion/DatabaseSeeder.java — NO inventar nuevos. 40
    // estudiantes (26000001-26000040) pareados 1:1 con 40 apoderados (15000001-15000040,
    // apoderadoN <-> estudianteN).
    private static final int ESTUDIANTE_RUT_BASE = 26000000;
    private static final int APODERADO_RUT_BASE = 15000000;
    private static final int TOTAL_ESTUDIANTES = 40;
    private static final String[] TIPOS_SANGRE = { "O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-" };

    // Antecedentes médicos variados a propósito: ~1/3 sin ningún registro (nada), ~1/3 con algo
    // leve, ~1/3 con algo más serio — para no dejar los 40 idénticos ("Ninguna registrada" x40).
    // Formato de cada fila: { alergias, condicionesMedicas, medicamentos }.
    private static final String[][] CONDICIONES_LEVES = {
        { "Alergia leve al polen", "Ninguna registrada", "Ninguno" },
        { "Intolerancia leve a la lactosa", "Ninguna registrada", "Ninguno" },
        { "Alergia leve a ácaros de polvo", "Ninguna registrada", "Ninguno" },
        { "Ninguna registrada", "Rinitis alérgica estacional", "Antihistamínico ocasional" },
    };
    private static final String[][] CONDICIONES_SERIAS = {
        { "Ninguna registrada", "Asma bronquial", "Salbutamol (inhalador de rescate)" },
        { "Alergia severa a maní (riesgo de anafilaxia)", "Ninguna registrada", "Adrenalina autoinyectable (EpiPen)" },
        { "Ninguna registrada", "Diabetes tipo 1", "Insulina" },
        { "Ninguna registrada", "Epilepsia", "Ácido valproico" },
    };

    public DatabaseSeeder(HojaVidaRepository hojaVidaRepository,
            AntecedentesAcademicosRepository academicosRepository,
            AntecedentesApoderadoRepository apoderadoRepository,
            AntecedentesMedicosRepository medicosRepository,
            @Qualifier("autenticacionRestClient") RestClient autenticacionRestClient) {
        this.hojaVidaRepository = hojaVidaRepository;
        this.academicosRepository = academicosRepository;
        this.apoderadoRepository = apoderadoRepository;
        this.medicosRepository = medicosRepository;
        this.autenticacionRestClient = autenticacionRestClient;
    }

    @Override
    public void run(String... args) throws Exception {
        if (hojaVidaRepository.count() > 0) {
            return;
        }

        // matriculaId asume el orden de creación del DatabaseSeeder de MS-GestionMatricula
        // (ddl-auto=create, IDs reinician en 1 en cada arranque limpio): guarda las 40
        // matrículas en el mismo orden que los 40 estudiantes (índice N -> matriculaId N).
        // Si ese seeder cambia de orden, este mapeo queda desalineado.
        for (int i = 1; i <= TOTAL_ESTUDIANTES; i++) {
            long estudianteRut = ESTUDIANTE_RUT_BASE + i;
            long apoderadoRut = APODERADO_RUT_BASE + i;
            long matriculaId = i;

            HojaVidaEstudiante hoja = new HojaVidaEstudiante();
            hoja.setEstudianteUsuRut(estudianteRut);
            hoja.setMatriculaId(matriculaId);
            hoja.setEstado("Incorporado");
            HojaVidaEstudiante guardada = hojaVidaRepository.save(hoja);

            AntecedentesAcademicos academico = new AntecedentesAcademicos();
            academico.setAnioEscolar(2026);
            academico.setPromedioGeneralActual(5.0 + (i % 20) / 10.0); // 5.0-6.9 variado
            academico.setSituacionFinalAprobacion("S");
            academico.setHojaVida(guardada);
            academicosRepository.save(academico);

            AntecedentesApoderado apoderado = new AntecedentesApoderado();
            ApoderadoDTO apoderadoReal = obtenerApoderado((int) apoderadoRut);
            apoderado.setNombre(apoderadoReal != null
                    ? (apoderadoReal.nombre() + " " + apoderadoReal.apellido())
                    : "No especificado");
            apoderado.setTelefono(apoderadoReal != null && apoderadoReal.telefono() != null
                    ? apoderadoReal.telefono()
                    : "No especificado");
            // profesion/lugarTrabajo/disponibilidadHoraria no existen en Autenticacion — quedan
            // con placeholder, editables después desde la propia sección Hoja de Vida.
            apoderado.setProfesion("No especificado");
            apoderado.setDireccion("No especificado");
            apoderado.setLugarTrabajo("No especificado");
            apoderado.setDisponibilidadHoraria("N");
            apoderado.setHojaVida(guardada);
            apoderadoRepository.save(apoderado);

            // Sin antecedente médico para ~1/3 (estudiantes "sin nada" registrado) — se omite
            // la fila completa a propósito, no se crea con valores vacíos.
            int grupo = i % 3;
            if (grupo != 0) {
                String[] condicion = grupo == 1
                        ? CONDICIONES_LEVES[i % CONDICIONES_LEVES.length]
                        : CONDICIONES_SERIAS[i % CONDICIONES_SERIAS.length];

                AntecedentesMedicos medico = new AntecedentesMedicos();
                medico.setAlergias(condicion[0]);
                medico.setCondicionesMedicas(condicion[1]);
                medico.setMedicamentos(condicion[2]);
                medico.setTipoSangre(TIPOS_SANGRE[i % TIPOS_SANGRE.length]);
                medico.setHojaVida(guardada);
                medicosRepository.save(medico);
            }
        }
    }

    // Obtiene los datos del apoderado en Autenticacion. Devuelve null si no existe o si el
    // microservicio no responde — MS-Autenticacion debe estar arriba ANTES de levantar este MS
    // para que el seeder resuelva nombre/teléfono reales (si no, queda con placeholder).
    private ApoderadoDTO obtenerApoderado(Integer rut) {
        try {
            return autenticacionRestClient.get()
                    .uri("/apoderados/{rut}", rut)
                    .retrieve()
                    .body(ApoderadoDTO.class);
        } catch (Exception e) {
            logger.warn("No se pudo resolver el apoderado {} desde Autenticacion: {}", rut, e.getMessage());
            return null;
        }
    }
}
