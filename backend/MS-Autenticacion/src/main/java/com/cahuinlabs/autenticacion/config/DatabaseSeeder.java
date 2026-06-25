package com.cahuinlabs.autenticacion.config;

import com.cahuinlabs.autenticacion.models.request.funcionarios.directivos.CrearDirectivoRequest;
import com.cahuinlabs.autenticacion.models.request.funcionarios.docente.CrearDocenteRequest;
import com.cahuinlabs.autenticacion.models.request.funcionarios.inspector.CrearInspectorRequest;
import com.cahuinlabs.autenticacion.repository.UsuarioRepository;
import com.cahuinlabs.autenticacion.service.FuncionarioService;
import com.cahuinlabs.autenticacion.models.request.apoderado.CrearApoderadoRequest;
import com.cahuinlabs.autenticacion.models.request.estudiante.CrearEstudianteRequest;
import com.cahuinlabs.autenticacion.service.ApoderadoService;
import com.cahuinlabs.autenticacion.service.EstudianteService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final FuncionarioService funcionarioService;
    private final EstudianteService estudianteService;
    private final ApoderadoService apoderadoService;

    public DatabaseSeeder(UsuarioRepository usuarioRepository, FuncionarioService funcionarioService, EstudianteService estudianteService, ApoderadoService apoderadoService) {
        this.usuarioRepository = usuarioRepository;
        this.funcionarioService = funcionarioService;
        this.estudianteService = estudianteService;
        this.apoderadoService = apoderadoService;
    }

    @Override
    public void run(String... args) throws Exception {
        //Si la base de datos está vacía, creamos el primer usuario administrador/docente
        if (usuarioRepository.count() == 0) {

            // 1. Crear Directivo (Administrador Principal)
            CrearDirectivoRequest directivo = new CrearDirectivoRequest();
            directivo.setDirRut(11111111);
            directivo.setDirDvRut('1');
            directivo.setDirPrimerNombre("Admin");
            directivo.setDirApellidoPat("Sistema");
            directivo.setDirApellidoMat("Director");
            directivo.setDirEmail("admin@colegio.cl");
            directivo.setDirPassword("admin123"); 
            directivo.setDirTel("+56911111111");
            directivo.setDirTitulo("Ingeniero Comercial");
            directivo.setDirCargo("Director General");
            directivo.setDirDireccion("Avenida Principal");
            directivo.setDirNumeroDireccion(123);
            directivo.setDirTipoCasa("Casa");
            directivo.setIdComuna(1); 
            funcionarioService.crearDirectivo(directivo);

            // 2. Crear Docente (Profesor)
            CrearDocenteRequest docente = new CrearDocenteRequest();
            docente.setDcteRut(22222222);
            docente.setDcteDvRut('2');
            docente.setDctePrimerNombre("Profe");
            docente.setDcteApellidoPat("Matematicas");
            docente.setDcteApellidoMat("Experto");
            docente.setDcteEmail("profe@colegio.cl");
            docente.setDctePassword("profe123"); 
            docente.setDcteTel("+56922222222");
            docente.setDcteTitulo("Pedagogo");
            docente.setDcteEspecialidad("Matemáticas Avanzadas");
            docente.setDcteDireccion("Calle Los Profesores");
            docente.setDcteNumeroDireccion(456);
            docente.setDcteTipoCasa("Departamento");
            docente.setIdComuna(1); 
            funcionarioService.crearDocente(docente);

            // 3. Crear Inspector
            CrearInspectorRequest inspector = new CrearInspectorRequest();
            inspector.setInsRut(33333333);
            inspector.setInsDvRut('3');
            inspector.setInsPrimerNombre("Inspector");
            inspector.setInsApellidoPat("Estricto");
            inspector.setInsApellidoMat("Seguridad");
            inspector.setInsEmail("inspector@colegio.cl");
            inspector.setInsPassword("inspector123"); 
            inspector.setInsTel("+56933333333");
            inspector.setInsTitulo("Tecnico Educacion");
            inspector.setInsNivel("Patio Central");
            inspector.setInsDireccion("Calle Disciplina");
            inspector.setInsNumeroDireccion(789);
            inspector.setInsTipoCasa("Casa");
            inspector.setIdComuna(1); 
            funcionarioService.crearInspector(inspector);

            // 4. Crear Estudiante
            CrearEstudianteRequest estudiante = new CrearEstudianteRequest();
            estudiante.setEstRut(12345678);
            estudiante.setEstDvRut('9');
            estudiante.setEstPrimerNombre("Juan");
            estudiante.setEstSegundoNombre("Ignacio");
            estudiante.setEstApellidoPat("Perez");
            estudiante.setEstApellidoMat("Gonzalez");
            estudiante.setEstEmail("juan.perez@estudiante.cl");
            estudiante.setEstPassword("alumno123");
            estudiante.setEstTel("+56944444444");
            estudiante.setEstParentesco("Hijo");
            estudiante.setEstDireccion("Avenida Alumnos");
            estudiante.setEstNumeroDireccion(321);
            estudiante.setEstTipoCasa("Casa");
            estudiante.setIdComuna(1);
            estudianteService.crearEstudiante(estudiante);
            // 5. Crear Apoderado
            CrearApoderadoRequest apoderado = new CrearApoderadoRequest();
            apoderado.setApoRut(44444444);
            apoderado.setApoDvRut('4');
            apoderado.setApoPrimerNombre("Maria");
            apoderado.setApoApellidoPat("Gonzalez");
            apoderado.setApoApellidoMat("Lopez");
            apoderado.setApoEmail("maria.apoderada@gmail.com");
            apoderado.setApoPassword("apoderado123");
            apoderado.setApoTel("+56955555555");
            apoderado.setApoParentesco("Madre");
            apoderado.setApoDireccion("Avenida Alumnos");
            apoderado.setApoNumeroDireccion(321);
            apoderado.setApoTipoCasa("Casa");
            apoderado.setIdComuna(1);
            apoderadoService.crearApoderado(apoderado);

            System.out.println("==========================================================================");
            System.out.println("====== BASE DE DATOS INICIALIZADA CON USUARIOS DE PRUEBA (SEEDER) ========");
            System.out.println("==========================================================================");
            System.out.println("1. DIRECTIVO  (Admin)  -> RUT: 11111111 | Email: admin@colegio.cl");
            System.out.println("2. DOCENTE    (Profe)  -> RUT: 22222222 | Email: profe@colegio.cl");
            System.out.println("3. INSPECTOR           -> RUT: 33333333 | Email: inspector@colegio.cl");
            System.out.println("4. ESTUDIANTE (Alumno) -> RUT: 12345678 | Email: juan.perez@estudiante.cl");
            System.out.println("5. APODERADO           -> RUT: 44444444 | Email: maria.apoderada@gmail.com");
            System.out.println("==========================================================================");
            System.out.println("Todas las contraseñas siguen el formato: [rol]123 (ej. admin123, alumno123)");
            System.out.println("==========================================================================");
        }
    }
}
