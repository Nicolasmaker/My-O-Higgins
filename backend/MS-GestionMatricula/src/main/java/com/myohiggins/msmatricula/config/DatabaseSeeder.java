package com.myohiggins.msmatricula.config;

import com.myohiggins.msmatricula.model.entities.Matricula;
import com.myohiggins.msmatricula.repository.MatriculaRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final MatriculaRepository matriculaRepository;

    // RUTs reales sembrados por MS-Autenticacion/DatabaseSeeder.java — NO inventar nuevos.
    // 40 estudiantes (26000001-26000040) pareados 1:1 con 40 apoderados (15000001-15000040,
    // apoderadoN <-> estudianteN): el frontend de Matrícula filtra apoderadoRut===usuario.usuRut
    // exacto, así que respetar el par exacto importa, no cualquier apoderado sirve.
    private static final int ESTUDIANTE_RUT_BASE = 26000000;
    private static final int APODERADO_RUT_BASE = 15000000;

    // 3 Directivos reales — únicos habilitados para registrar matrícula (regla de negocio ya
    // aplicada en frontend: gestión de Matrícula = solo ROLE_DIRECTIVO).
    private static final int[] DIRECTIVOS = { 10111222, 11222333, 13444555 };

    // Parentesco vive en Matricula (no en Apoderado/Estudiante de Autenticacion) porque describe
    // la relación apoderado-alumno de ESTA matrícula específica, no de la persona en general.
    private static final String[] PARENTESCOS = { "Padre", "Madre", "Tío", "Tía", "Abuela", "Tutor Legal" };

    // Se guarda directo por repositorio, sin pasar por MatriculaService.crearMatricula(): ese
    // método valida estudiante/apoderado/funcionario vía REST contra MS-Autenticacion, lo que
    // obligaría a levantar Autenticacion ANTES que este MS solo para poder sembrar (mismo tipo
    // de fragilidad de orden de arranque que tumbó a MS-HojaDeVida). Los RUTs usados acá son
    // copia exacta de los que Autenticacion ya siembra, así que el riesgo de dato huérfano es bajo.
    public DatabaseSeeder(MatriculaRepository matriculaRepository) {
        this.matriculaRepository = matriculaRepository;
    }

    // cursoId asume el orden de creación del DatabaseSeeder de MS-GestionAcademica (create-drop,
    // IDs reinician en 1 en cada arranque limpio): Kínder(1-3) A/B/C, Básico 1°-8° (4-27, 3 por
    // grado), Media 1°-4° (28-39, 3 por grado). Si ese orden cambia, estos IDs quedan huérfanos.
    private static final long CURSO_KINDER_A = 1L;
    private static final long CURSO_3B_A = 10L; // 3° básico A
    private static final long CURSO_6B_B = 20L; // 6° básico B
    private static final long CURSO_1M_A = 28L; // 1° medio A
    private static final long CURSO_3M_B = 35L; // 3° medio B

    @Override
    public void run(String... args) throws Exception {
        if (matriculaRepository.count() > 0) {
            return;
        }

        int indiceEstudiante = 1;
        indiceEstudiante = sembrarGrupo(indiceEstudiante, 5, CURSO_KINDER_A, "NUEVO");
        indiceEstudiante = sembrarGrupo(indiceEstudiante, 10, CURSO_3B_A, "ANTIGUO");
        indiceEstudiante = sembrarGrupo(indiceEstudiante, 10, CURSO_6B_B, "ANTIGUO");
        indiceEstudiante = sembrarGrupo(indiceEstudiante, 10, CURSO_1M_A, "NUEVO");
        sembrarGrupo(indiceEstudiante, 5, CURSO_3M_B, "ANTIGUO");
    }

    // Crea `cantidad` matrículas para el curso dado, tomando estudiantes/apoderados consecutivos
    // desde `desdeIndice` (1-based). El último de cada grupo queda REPITENTE para variar el dato.
    private int sembrarGrupo(int desdeIndice, int cantidad, long cursoId, String tipoAlumnoPorDefecto) {
        for (int i = 0; i < cantidad; i++) {
            int indice = desdeIndice + i;
            boolean esUltimoDelGrupo = i == cantidad - 1;

            Matricula matricula = new Matricula();
            matricula.setCursoId(cursoId);
            matricula.setAlumnoRut((long) (ESTUDIANTE_RUT_BASE + indice));
            matricula.setApoderadoRut((long) (APODERADO_RUT_BASE + indice));
            matricula.setFuncionarioUsuRut((long) DIRECTIVOS[indice % DIRECTIVOS.length]);
            matricula.setTipoAlumno(esUltimoDelGrupo ? "REPITENTE" : tipoAlumnoPorDefecto);
            matricula.setParentesco(PARENTESCOS[indice % PARENTESCOS.length]);

            matriculaRepository.save(matricula);
        }
        return desdeIndice + cantidad;
    }
}
