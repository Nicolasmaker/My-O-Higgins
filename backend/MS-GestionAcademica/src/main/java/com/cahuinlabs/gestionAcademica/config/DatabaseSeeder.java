package com.cahuinlabs.gestionAcademica.config;

import com.cahuinlabs.gestionAcademica.models.entities.Asignatura;
import com.cahuinlabs.gestionAcademica.models.entities.Nivel;
import com.cahuinlabs.gestionAcademica.models.request.AsignaturaRequest;
import com.cahuinlabs.gestionAcademica.models.request.Curso.CrearCursoRequest;
import com.cahuinlabs.gestionAcademica.models.request.Impartir.CrearImpartirRequest;
import com.cahuinlabs.gestionAcademica.models.request.NivelRequest;
import com.cahuinlabs.gestionAcademica.models.request.SalaRequest;
import com.cahuinlabs.gestionAcademica.repository.NivelRepository;
import com.cahuinlabs.gestionAcademica.service.AsignaturaService;
import com.cahuinlabs.gestionAcademica.service.CursoService;
import com.cahuinlabs.gestionAcademica.service.ImpartirService;
import com.cahuinlabs.gestionAcademica.service.NivelService;
import com.cahuinlabs.gestionAcademica.service.SalaService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final NivelRepository nivelRepository;
    private final NivelService nivelService;
    private final SalaService salaService;
    private final CursoService cursoService;
    private final AsignaturaService asignaturaService;
    private final ImpartirService impartirService;

    // RUTs reales sembrados por MS-Autenticacion/DatabaseSeeder.java — NO inventar nuevos,
    // deben calzar exacto porque no hay FK real entre bases (ver bugs BUG-04/05 del proyecto).
    private static final int DOC_LENGUAJE = 15234123;   // Camila Rojas
    private static final int DOC_MATEMATICA = 17823456; // Felipe Tapia
    private static final int DOC_BIOLOGIA = 16543987;   // Valentina Silva
    private static final int DOC_EDFISICA = 19345678;   // Matias Herrera
    private static final int DOC_HISTORIA = 14256789;   // Daniela Medina
    private static final int DOC_MUSICA = 18765432;     // Andres Vargas
    private static final int DOC_ARTES = 20123456;      // Javiera Flores
    private static final int DOC_FISICA = 13456789;     // Diego Salazar
    private static final int DOC_QUIMICA = 15987654;    // Carolina Reyes
    private static final int DOC_INGLES = 17123456;     // Sebastian Ortiz
    private static final int DOC_EDINICIAL = 18234567;  // Natalia Pizarro
    private static final int DOC_FILOSOFIA = 14678901;  // Rodrigo Morales
    private static final int DOC_TECNOLOGIA = 16890123; // Paula Gutierrez
    // Excluidos a propósito (piden 13 de los 15): docente12 Gonzalo Munoz (19987654, Educación
    // General) y docente13 Fernanda Castillo (21234567, Psicopedagoga/NEE) — ninguna asignatura
    // creada calza con su especialidad.

    public DatabaseSeeder(NivelRepository nivelRepository, NivelService nivelService, SalaService salaService,
            CursoService cursoService, AsignaturaService asignaturaService, ImpartirService impartirService) {
        this.nivelRepository = nivelRepository;
        this.nivelService = nivelService;
        this.salaService = salaService;
        this.cursoService = cursoService;
        this.asignaturaService = asignaturaService;
        this.impartirService = impartirService;
    }

    private record NivelDef(int nivNum, String nivTipo, String salaPrefijo, String bandaAsignaturas) {
    }

    @Override
    public void run(String... args) throws Exception {
        if (nivelRepository.count() > 0) {
            return;
        }

        Map<String, Asignatura> asignaturas = sembrarAsignaturas();

        List<NivelDef> niveles = new java.util.ArrayList<>();
        niveles.add(new NivelDef(0, "KINDER", "K1", "KINDER"));
        for (int grado = 1; grado <= 8; grado++) {
            niveles.add(new NivelDef(grado, "BASICO", "B" + grado, grado <= 6 ? "BASICO_1_6" : "BASICO_7_8"));
        }
        for (int grado = 1; grado <= 4; grado++) {
            niveles.add(new NivelDef(grado, "MEDIA", "M" + grado, grado <= 2 ? "MEDIA_1_2" : "MEDIA_3_4"));
        }

        for (NivelDef def : niveles) {
            Nivel nivel = nivelService.crearNivel(construirNivelRequest(def.nivNum(), def.nivTipo()));
            List<String> asignaturasBanda = asignaturasPorBanda(def.bandaAsignaturas());

            char[] letras = { 'A', 'B', 'C' };
            for (int seq = 1; seq <= 3; seq++) {
                String codigoSala = def.salaPrefijo() + "0" + seq;
                Integer capacidad = ThreadLocalRandom.current().nextInt(20, 31); // 20-30 inclusive

                SalaRequest salaRequest = new SalaRequest();
                salaRequest.setSalLetra(codigoSala);
                salaRequest.setSalaCapacidad(capacidad);
                Integer idSala = salaService.crearSala(salaRequest).getIdSal();

                CrearCursoRequest cursoRequest = new CrearCursoRequest();
                cursoRequest.setCurLetraSec(String.valueOf(letras[seq - 1]));
                cursoRequest.setCurAnioEscolar(2026);
                cursoRequest.setIdSala(idSala);
                cursoRequest.setIdNivel(nivel.getIdNiv());
                Integer idCurso = cursoService.crearCurso(cursoRequest).getIdCur();

                for (String nombreAsignatura : asignaturasBanda) {
                    Asignatura asignatura = asignaturas.get(nombreAsignatura);
                    CrearImpartirRequest impartirRequest = new CrearImpartirRequest();
                    impartirRequest.setDocenteUsuRut(docentePorAsignatura(nombreAsignatura));
                    impartirRequest.setIdAsignatura(asignatura.getIdAsi());
                    impartirRequest.setIdCurso(idCurso);
                    impartirService.crear(impartirRequest);
                }
            }
        }
    }

    private NivelRequest construirNivelRequest(int nivNum, String nivTipo) {
        NivelRequest request = new NivelRequest();
        request.setNivNum(nivNum);
        request.setNivTipo(nivTipo);
        return request;
    }

    private Map<String, Asignatura> sembrarAsignaturas() {
        Map<String, String> descripciones = new LinkedHashMap<>();
        // Comunes 1° básico a 4° medio (según banda)
        descripciones.put("Lenguaje y Comunicación", "Comprensión lectora, expresión oral y escrita, 1° a 6° básico.");
        descripciones.put("Lengua y Literatura", "Continuación de Lenguaje y Comunicación, 7° básico a 4° medio.");
        descripciones.put("Matemática", "Numeros, álgebra, geometría y datos, 1° básico a 4° medio.");
        descripciones.put("Historia, Geografía y Ciencias Sociales", "Historia, geografía y formación ciudadana, 1° básico a 2° medio.");
        descripciones.put("Ciencias Naturales", "Biología, física y química integradas, 1° a 8° básico.");
        descripciones.put("Idioma Extranjero: Inglés", "Comunicación en inglés, 1° básico a 4° medio.");
        descripciones.put("Artes Visuales", "Expresión y apreciación visual, 1° básico a 4° medio.");
        descripciones.put("Música", "Expresión y apreciación musical, 1° básico a 4° medio.");
        descripciones.put("Educación Física y Salud", "Actividad física y hábitos de vida saludable, 1° básico a 4° medio.");
        descripciones.put("Tecnología", "Diseño y resolución de problemas tecnológicos, 1° básico a 4° medio.");
        descripciones.put("Orientación", "Desarrollo personal, social y vocacional, 1° básico a 4° medio.");
        // Media diferenciada 1°-2° medio (reemplazan Ciencias Naturales)
        descripciones.put("Física", "Fenómenos físicos y su modelamiento, 1° y 2° medio.");
        descripciones.put("Química", "Estructura de la materia y reacciones químicas, 1° y 2° medio.");
        descripciones.put("Biología", "Sistemas vivos y procesos biológicos, 1° y 2° medio.");
        // Media 3°-4° (reemplazan Historia, Ciencias Naturales y Lenguaje y Comunicación)
        descripciones.put("Filosofía", "Pensamiento crítico y reflexión filosófica, 3° y 4° medio.");
        descripciones.put("Ciencias para la Ciudadanía", "Ciencia aplicada a problemáticas de la sociedad, 3° y 4° medio.");
        descripciones.put("Educación Ciudadana", "Formación cívica y democrática, 3° y 4° medio.");
        // Kínder
        descripciones.put("Exploración del Entorno Natural", "Núcleo de aprendizaje de Educación Parvularia, Kínder.");
        descripciones.put("Comprensión del Entorno Sociocultural", "Núcleo de aprendizaje de Educación Parvularia, Kínder.");
        descripciones.put("Pensamiento Matemático", "Núcleo de aprendizaje de Educación Parvularia, Kínder.");
        descripciones.put("Lenguaje Verbal", "Núcleo de aprendizaje de Educación Parvularia, Kínder.");
        descripciones.put("Lenguajes Artísticos", "Núcleo de aprendizaje de Educación Parvularia, Kínder.");
        descripciones.put("Identidad y Autonomía", "Núcleo de aprendizaje de Educación Parvularia, Kínder.");
        descripciones.put("Convivencia y Ciudadanía", "Núcleo de aprendizaje de Educación Parvularia, Kínder.");
        descripciones.put("Corporalidad y Movimiento", "Núcleo de aprendizaje de Educación Parvularia, Kínder.");

        Map<String, Asignatura> creadas = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : descripciones.entrySet()) {
            AsignaturaRequest request = new AsignaturaRequest();
            request.setAsiNom(entry.getKey());
            request.setAsiDes(entry.getValue());
            creadas.put(entry.getKey(), asignaturaService.crearAsignatura(request));
        }
        return creadas;
    }

    private List<String> asignaturasPorBanda(String banda) {
        return switch (banda) {
            case "KINDER" -> List.of(
                    "Exploración del Entorno Natural", "Comprensión del Entorno Sociocultural",
                    "Pensamiento Matemático", "Lenguaje Verbal", "Lenguajes Artísticos",
                    "Identidad y Autonomía", "Convivencia y Ciudadanía", "Corporalidad y Movimiento");
            case "BASICO_1_6" -> List.of(
                    "Lenguaje y Comunicación", "Matemática", "Historia, Geografía y Ciencias Sociales",
                    "Ciencias Naturales", "Idioma Extranjero: Inglés", "Artes Visuales", "Música",
                    "Educación Física y Salud", "Tecnología", "Orientación");
            case "BASICO_7_8" -> List.of(
                    "Lengua y Literatura", "Matemática", "Historia, Geografía y Ciencias Sociales",
                    "Ciencias Naturales", "Idioma Extranjero: Inglés", "Artes Visuales", "Música",
                    "Educación Física y Salud", "Tecnología", "Orientación");
            case "MEDIA_1_2" -> List.of(
                    "Lengua y Literatura", "Matemática", "Historia, Geografía y Ciencias Sociales",
                    "Física", "Química", "Biología", "Idioma Extranjero: Inglés", "Artes Visuales",
                    "Música", "Educación Física y Salud", "Tecnología", "Orientación");
            case "MEDIA_3_4" -> List.of(
                    "Lengua y Literatura", "Matemática", "Filosofía", "Ciencias para la Ciudadanía",
                    "Educación Ciudadana", "Idioma Extranjero: Inglés", "Artes Visuales", "Música",
                    "Educación Física y Salud", "Tecnología", "Orientación");
            default -> throw new IllegalStateException("Banda de asignaturas desconocida: " + banda);
        };
    }

    private int docentePorAsignatura(String nombreAsignatura) {
        return switch (nombreAsignatura) {
            case "Lenguaje y Comunicación", "Lengua y Literatura" -> DOC_LENGUAJE;
            case "Matemática" -> DOC_MATEMATICA;
            case "Historia, Geografía y Ciencias Sociales" -> DOC_HISTORIA;
            case "Ciencias Naturales", "Biología" -> DOC_BIOLOGIA;
            case "Idioma Extranjero: Inglés" -> DOC_INGLES;
            case "Artes Visuales" -> DOC_ARTES;
            case "Música" -> DOC_MUSICA;
            case "Educación Física y Salud" -> DOC_EDFISICA;
            case "Tecnología" -> DOC_TECNOLOGIA;
            case "Orientación" -> DOC_HISTORIA; // sin especialista dedicado, se reutiliza Historia
            case "Física" -> DOC_FISICA;
            case "Química" -> DOC_QUIMICA;
            case "Filosofía", "Ciencias para la Ciudadanía", "Educación Ciudadana" -> DOC_FILOSOFIA;
            case "Exploración del Entorno Natural", "Comprensión del Entorno Sociocultural",
                    "Pensamiento Matemático", "Lenguaje Verbal", "Lenguajes Artísticos",
                    "Identidad y Autonomía", "Convivencia y Ciudadanía", "Corporalidad y Movimiento" ->
                DOC_EDINICIAL;
            default -> throw new IllegalStateException("Sin docente mapeado para asignatura: " + nombreAsignatura);
        };
    }
}
