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
            // Directivo 1: Director General
            CrearDirectivoRequest directivo1 = new CrearDirectivoRequest();
            directivo1.setDirRut(10111222);
            directivo1.setDirDvRut('5');
            directivo1.setDirPrimerNombre("Mauricio");
            directivo1.setDirSegundoNombre("Alejandro");
            directivo1.setDirApellidoPat("Paredes");
            directivo1.setDirApellidoMat("Soto");
            directivo1.setDirEmail("m.paredess@directivo.myohiggins.cl");
            directivo1.setDirPassword("directivo123"); 
            directivo1.setDirTel("+56991111111");
            directivo1.setDirTitulo("Magíster en Gestión Educacional");
            directivo1.setDirCargo("Director General");
            directivo1.setDirDireccion("Avenida Los Fundadores");
            directivo1.setDirNumeroDireccion(100);
            directivo1.setDirTipoCasa("Casa");
            directivo1.setIdComuna(8); 
            funcionarioService.crearDirectivo(directivo1);

            // Directivo 2: Jefe de UTP
            CrearDirectivoRequest directivo2 = new CrearDirectivoRequest();
            directivo2.setDirRut(11222333);
            directivo2.setDirDvRut('9');
            directivo2.setDirPrimerNombre("Patricia");
            directivo2.setDirApellidoPat("Vergara");
            directivo2.setDirApellidoMat("Lillo");
            directivo2.setDirEmail("p.vergaral@directivo.myohiggins.cl");
            directivo2.setDirPassword("directivo123"); 
            directivo2.setDirTel("+56992222222");
            directivo2.setDirTitulo("Profesor de Estado con Mención en Currículum");
            directivo2.setDirCargo("Jefe de UTP");
            directivo2.setDirDireccion("Calle Los Sauces");
            directivo2.setDirNumeroDireccion(432);
            directivo2.setDirTipoCasa("Departamento");
            directivo2.setIdComuna(3); 
            funcionarioService.crearDirectivo(directivo2);

            // Directivo 3: Secretaria de Dirección
            CrearDirectivoRequest directivo3 = new CrearDirectivoRequest();
            directivo3.setDirRut(13444555);
            directivo3.setDirDvRut('6');
            directivo3.setDirPrimerNombre("Claudia");
            directivo3.setDirSegundoNombre("Andrea");
            directivo3.setDirApellidoPat("Navarro");
            directivo3.setDirApellidoMat("Moya");
            directivo3.setDirEmail("c.navarrom@directivo.myohiggins.cl");
            directivo3.setDirPassword("directivo123"); 
            directivo3.setDirTel("+56993333333");
            directivo3.setDirTitulo("Técnico en Administración de Empresas");
            directivo3.setDirCargo("Secretaria");
            directivo3.setDirDireccion("Pasaje Los Aromos");
            directivo3.setDirNumeroDireccion(87);
            directivo3.setDirTipoCasa("Casa");
            directivo3.setIdComuna(15); 
            funcionarioService.crearDirectivo(directivo3);

            // 2. Crear Docente (Profesor)
            // Docente 1
            CrearDocenteRequest docente1 = new CrearDocenteRequest();
            docente1.setDcteRut(15234123);
            docente1.setDcteDvRut('7');
            docente1.setDctePrimerNombre("Camila");
            docente1.setDcteApellidoPat("Rojas");
            docente1.setDcteApellidoMat("Valdes");
            docente1.setDcteEmail("c.rojasv@myohiggins.cl");
            docente1.setDctePassword("profe123"); 
            docente1.setDcteTel("+56971234561");
            docente1.setDcteTitulo("Pedagogía en Lenguaje");
            docente1.setDcteEspecialidad("Lenguaje y Comunicación");
            docente1.setDcteDireccion("Avenida Las Araucarias");
            docente1.setDcteNumeroDireccion(112);
            docente1.setDcteTipoCasa("Casa");
            docente1.setIdComuna(1); 
            funcionarioService.crearDocente(docente1);

            // Docente 2
            CrearDocenteRequest docente2 = new CrearDocenteRequest();
            docente2.setDcteRut(17823456);
            docente2.setDcteDvRut('0');
            docente2.setDctePrimerNombre("Felipe");
            docente2.setDcteApellidoPat("Tapia");
            docente2.setDcteApellidoMat("Soto");
            docente2.setDcteEmail("f.tapias@myohiggins.cl");
            docente2.setDctePassword("profe123"); 
            docente2.setDcteTel("+56971234562");
            docente2.setDcteTitulo("Profesor de Estado");
            docente2.setDcteEspecialidad("Matemáticas");
            docente2.setDcteDireccion("Calle Los Tilos");
            docente2.setDcteNumeroDireccion(845);
            docente2.setDcteTipoCasa("Departamento");
            docente2.setIdComuna(2); 
            funcionarioService.crearDocente(docente2);

            // Docente 3
            CrearDocenteRequest docente3 = new CrearDocenteRequest();
            docente3.setDcteRut(16543987);
            docente3.setDcteDvRut('2');
            docente3.setDctePrimerNombre("Valentina");
            docente3.setDcteApellidoPat("Silva");
            docente3.setDcteApellidoMat("Castro");
            docente3.setDcteEmail("v.silvac@myohiggins.cl");
            docente3.setDctePassword("profe123"); 
            docente3.setDcteTel("+56971234563");
            docente3.setDcteTitulo("Pedagogía en Ciencias");
            docente3.setDcteEspecialidad("Biología");
            docente3.setDcteDireccion("Pasaje Las Rosas");
            docente3.setDcteNumeroDireccion(334);
            docente3.setDcteTipoCasa("Casa");
            docente3.setIdComuna(3); 
            funcionarioService.crearDocente(docente3);

            // Docente 4
            CrearDocenteRequest docente4 = new CrearDocenteRequest();
            docente4.setDcteRut(19345678);
            docente4.setDcteDvRut('2');
            docente4.setDctePrimerNombre("Matias");
            docente4.setDcteApellidoPat("Herrera");
            docente4.setDcteApellidoMat("Ruiz");
            docente4.setDcteEmail("m.herrerar@myohiggins.cl");
            docente4.setDctePassword("profe123"); 
            docente4.setDcteTel("+56971234564");
            docente4.setDcteTitulo("Profesor de Educación Física");
            docente4.setDcteEspecialidad("Deportes y Recreación");
            docente4.setDcteDireccion("Avenida del Deporte");
            docente4.setDcteNumeroDireccion(90);
            docente4.setDcteTipoCasa("Departamento");
            docente4.setIdComuna(4); 
            funcionarioService.crearDocente(docente4);

            // Docente 5
            CrearDocenteRequest docente5 = new CrearDocenteRequest();
            docente5.setDcteRut(14256789);
            docente5.setDcteDvRut('K');
            docente5.setDctePrimerNombre("Daniela");
            docente5.setDcteApellidoPat("Medina");
            docente5.setDcteApellidoMat("Cruz");
            docente5.setDcteEmail("d.medinac@myohiggins.cl");
            docente5.setDctePassword("profe123"); 
            docente5.setDcteTel("+56971234565");
            docente5.setDcteTitulo("Pedagogía en Historia");
            docente5.setDcteEspecialidad("Historia y Geografía");
            docente5.setDcteDireccion("Calle Los Héroes");
            docente5.setDcteNumeroDireccion(567);
            docente5.setDcteTipoCasa("Casa");
            docente5.setIdComuna(5); 
            funcionarioService.crearDocente(docente5);

            // Docente 6
            CrearDocenteRequest docente6 = new CrearDocenteRequest();
            docente6.setDcteRut(18765432);
            docente6.setDcteDvRut('7');
            docente6.setDctePrimerNombre("Andres");
            docente6.setDcteApellidoPat("Vargas");
            docente6.setDcteApellidoMat("Pena"); // Sin ñ para evitar problemas de codificación
            docente6.setDcteEmail("a.vargasp@myohiggins.cl");
            docente6.setDctePassword("profe123"); 
            docente6.setDcteTel("+56971234566");
            docente6.setDcteTitulo("Licenciatura en Artes");
            docente6.setDcteEspecialidad("Música");
            docente6.setDcteDireccion("Pasaje Los Acordes");
            docente6.setDcteNumeroDireccion(101);
            docente6.setDcteTipoCasa("Departamento");
            docente6.setIdComuna(6); 
            funcionarioService.crearDocente(docente6);

            // Docente 7
            CrearDocenteRequest docente7 = new CrearDocenteRequest();
            docente7.setDcteRut(20123456);
            docente7.setDcteDvRut('5');
            docente7.setDctePrimerNombre("Javiera");
            docente7.setDcteApellidoPat("Flores");
            docente7.setDcteApellidoMat("Diaz");
            docente7.setDcteEmail("j.floresd@myohiggins.cl");
            docente7.setDctePassword("profe123"); 
            docente7.setDcteTel("+56971234567");
            docente7.setDcteTitulo("Profesor de Artes Visuales");
            docente7.setDcteEspecialidad("Artes Plásticas");
            docente7.setDcteDireccion("Avenida Los Pintores");
            docente7.setDcteNumeroDireccion(233);
            docente7.setDcteTipoCasa("Casa");
            docente7.setIdComuna(7); 
            funcionarioService.crearDocente(docente7);

            // Docente 8
            CrearDocenteRequest docente8 = new CrearDocenteRequest();
            docente8.setDcteRut(13456789);
            docente8.setDcteDvRut('9');
            docente8.setDctePrimerNombre("Diego");
            docente8.setDcteApellidoPat("Salazar");
            docente8.setDcteApellidoMat("Moya");
            docente8.setDcteEmail("d.salazarm@myohiggins.cl");
            docente8.setDctePassword("profe123"); 
            docente8.setDcteTel("+56971234568");
            docente8.setDcteTitulo("Pedagogía en Ciencias Exactas");
            docente8.setDcteEspecialidad("Física");
            docente8.setDcteDireccion("Calle Gravedad");
            docente8.setDcteNumeroDireccion(980);
            docente8.setDcteTipoCasa("Departamento");
            docente8.setIdComuna(8); 
            funcionarioService.crearDocente(docente8);

            // Docente 9
            CrearDocenteRequest docente9 = new CrearDocenteRequest();
            docente9.setDcteRut(15987654);
            docente9.setDcteDvRut('3');
            docente9.setDctePrimerNombre("Carolina");
            docente9.setDcteApellidoPat("Reyes");
            docente9.setDcteApellidoMat("Rios");
            docente9.setDcteEmail("c.reyesr@myohiggins.cl");
            docente9.setDctePassword("profe123"); 
            docente9.setDcteTel("+56971234569");
            docente9.setDcteTitulo("Pedagogía en Ciencias");
            docente9.setDcteEspecialidad("Química");
            docente9.setDcteDireccion("Avenida Los Elementos");
            docente9.setDcteNumeroDireccion(404);
            docente9.setDcteTipoCasa("Casa");
            docente9.setIdComuna(9); 
            funcionarioService.crearDocente(docente9);

            // Docente 10
            CrearDocenteRequest docente10 = new CrearDocenteRequest();
            docente10.setDcteRut(17123456);
            docente10.setDcteDvRut('5');
            docente10.setDctePrimerNombre("Sebastian");
            docente10.setDcteApellidoPat("Ortiz");
            docente10.setDcteApellidoMat("Vega");
            docente10.setDcteEmail("s.ortizv@myohiggins.cl");
            docente10.setDctePassword("profe123"); 
            docente10.setDcteTel("+56971234570");
            docente10.setDcteTitulo("Profesor de Inglés");
            docente10.setDcteEspecialidad("Idiomas Extranjeros");
            docente10.setDcteDireccion("Calle Oxford");
            docente10.setDcteNumeroDireccion(22);
            docente10.setDcteTipoCasa("Departamento");
            docente10.setIdComuna(10); 
            funcionarioService.crearDocente(docente10);

            // Docente 11
            CrearDocenteRequest docente11 = new CrearDocenteRequest();
            docente11.setDcteRut(18234567);
            docente11.setDcteDvRut('9');
            docente11.setDctePrimerNombre("Natalia");
            docente11.setDcteApellidoPat("Pizarro");
            docente11.setDcteApellidoMat("Leon");
            docente11.setDcteEmail("n.pizarrol@myohiggins.cl");
            docente11.setDctePassword("profe123"); 
            docente11.setDcteTel("+56971234571");
            docente11.setDcteTitulo("Educadora de Párvulos");
            docente11.setDcteEspecialidad("Educación Inicial");
            docente11.setDcteDireccion("Pasaje Los Niños");
            docente11.setDcteNumeroDireccion(77);
            docente11.setDcteTipoCasa("Casa");
            docente11.setIdComuna(11); 
            funcionarioService.crearDocente(docente11);

            // Docente 12
            CrearDocenteRequest docente12 = new CrearDocenteRequest();
            docente12.setDcteRut(19987654);
            docente12.setDcteDvRut('6');
            docente12.setDctePrimerNombre("Gonzalo");
            docente12.setDcteApellidoPat("Munoz"); // Sin ñ para el correo
            docente12.setDcteApellidoMat("Vera");
            docente12.setDcteEmail("g.munozv@myohiggins.cl");
            docente12.setDctePassword("profe123"); 
            docente12.setDcteTel("+56971234572");
            docente12.setDcteTitulo("Profesor de Educación Básica");
            docente12.setDcteEspecialidad("Educación General");
            docente12.setDcteDireccion("Avenida Central");
            docente12.setDcteNumeroDireccion(555);
            docente12.setDcteTipoCasa("Departamento");
            docente12.setIdComuna(12); 
            funcionarioService.crearDocente(docente12);

            // Docente 13
            CrearDocenteRequest docente13 = new CrearDocenteRequest();
            docente13.setDcteRut(21234567);
            docente13.setDcteDvRut('9');
            docente13.setDctePrimerNombre("Fernanda");
            docente13.setDcteApellidoPat("Castillo");
            docente13.setDcteApellidoMat("Pino");
            docente13.setDcteEmail("f.castillop@myohiggins.cl");
            docente13.setDctePassword("profe123"); 
            docente13.setDcteTel("+56971234573");
            docente13.setDcteTitulo("Psicopedagoga");
            docente13.setDcteEspecialidad("Necesidades Educativas Especiales");
            docente13.setDcteDireccion("Calle La Inclusión");
            docente13.setDcteNumeroDireccion(610);
            docente13.setDcteTipoCasa("Casa");
            docente13.setIdComuna(13); 
            funcionarioService.crearDocente(docente13);

            // Docente 14
            CrearDocenteRequest docente14 = new CrearDocenteRequest();
            docente14.setDcteRut(14678901);
            docente14.setDcteDvRut('3');
            docente14.setDctePrimerNombre("Rodrigo");
            docente14.setDcteApellidoPat("Morales");
            docente14.setDcteApellidoMat("Toro");
            docente14.setDcteEmail("r.moralest@myohiggins.cl");
            docente14.setDctePassword("profe123"); 
            docente14.setDcteTel("+56971234574");
            docente14.setDcteTitulo("Licenciado en Filosofía");
            docente14.setDcteEspecialidad("Filosofía y Pensamiento Crítico");
            docente14.setDcteDireccion("Avenida Platón");
            docente14.setDcteNumeroDireccion(300);
            docente14.setDcteTipoCasa("Departamento");
            docente14.setIdComuna(14); 
            funcionarioService.crearDocente(docente14);

            // Docente 15
            CrearDocenteRequest docente15 = new CrearDocenteRequest();
            docente15.setDcteRut(16890123);
            docente15.setDcteDvRut('2');
            docente15.setDctePrimerNombre("Paula");
            docente15.setDcteApellidoPat("Gutierrez");
            docente15.setDcteApellidoMat("Lagos");
            docente15.setDcteEmail("p.gutierrezl@myohiggins.cl");
            docente15.setDctePassword("profe123"); 
            docente15.setDcteTel("+56971234575");
            docente15.setDcteTitulo("Ingeniero Informático");
            docente15.setDcteEspecialidad("Computación y Tecnología");
            docente15.setDcteDireccion("Calle Los Bits");
            docente15.setDcteNumeroDireccion(1024);
            docente15.setDcteTipoCasa("Casa");
            docente15.setIdComuna(15); 
            funcionarioService.crearDocente(docente15);

            // 3. Crear Inspector
            // Inspector 1
            CrearInspectorRequest inspector1 = new CrearInspectorRequest();
            inspector1.setInsRut(16111222);
            inspector1.setInsDvRut('4');
            inspector1.setInsPrimerNombre("Carlos");
            inspector1.setInsSegundoNombre("Alberto");
            inspector1.setInsApellidoPat("Pinto");
            inspector1.setInsApellidoMat("Salinas");
            inspector1.setInsEmail("c.pintos@ins.myohiggins.cl");
            inspector1.setInsPassword("inspector123"); 
            inspector1.setInsTel("+56981111111");
            inspector1.setInsTitulo("Técnico en Convivencia Escolar");
            inspector1.setInsNivel("Primer Ciclo Básico");
            inspector1.setInsDireccion("Avenida Los Halcones");
            inspector1.setInsNumeroDireccion(123);
            inspector1.setInsTipoCasa("Casa");
            inspector1.setIdComuna(2); 
            funcionarioService.crearInspector(inspector1);

            // Inspector 2
            CrearInspectorRequest inspector2 = new CrearInspectorRequest();
            inspector2.setInsRut(17222333);
            inspector2.setInsDvRut('8');
            inspector2.setInsPrimerNombre("Marcela");
            inspector2.setInsApellidoPat("Bravo");
            inspector2.setInsApellidoMat("Nunez"); // Sin 'ñ' para el estándar del correo
            inspector2.setInsEmail("m.bravon@ins.myohiggins.cl");
            inspector2.setInsPassword("inspector123"); 
            inspector2.setInsTel("+56982222222");
            inspector2.setInsTitulo("Asistente de la Educación");
            inspector2.setInsNivel("Enseñanza Media");
            inspector2.setInsDireccion("Calle Los Cerezos");
            inspector2.setInsNumeroDireccion(45);
            inspector2.setInsTipoCasa("Departamento");
            inspector2.setIdComuna(7); 
            funcionarioService.crearInspector(inspector2);

            // Inspector 3
            CrearInspectorRequest inspector3 = new CrearInspectorRequest();
            inspector3.setInsRut(18333444);
            inspector3.setInsDvRut('1');
            inspector3.setInsPrimerNombre("Roberto");
            inspector3.setInsSegundoNombre("Ignacio");
            inspector3.setInsApellidoPat("Caceres");
            inspector3.setInsApellidoMat("Godoy");
            inspector3.setInsEmail("r.caceresg@ins.myohiggins.cl");
            inspector3.setInsPassword("inspector123"); 
            inspector3.setInsTel("+56983333333");
            inspector3.setInsTitulo("Psicólogo Educacional");
            inspector3.setInsNivel("Patio Central");
            inspector3.setInsDireccion("Pasaje La Paz");
            inspector3.setInsNumeroDireccion(889);
            inspector3.setInsTipoCasa("Casa");
            inspector3.setIdComuna(12); 
            funcionarioService.crearInspector(inspector3);

            // Inspector 4
            CrearInspectorRequest inspector4 = new CrearInspectorRequest();
            inspector4.setInsRut(19444555);
            inspector4.setInsDvRut('5');
            inspector4.setInsPrimerNombre("Lorena");
            inspector4.setInsSegundoNombre("Andrea");
            inspector4.setInsApellidoPat("Fuentes");
            inspector4.setInsApellidoMat("Silva");
            inspector4.setInsEmail("l.fuentess@ins.myohiggins.cl");
            inspector4.setInsPassword("inspector123"); 
            inspector4.setInsTel("+56984444444");
            inspector4.setInsTitulo("Trabajadora Social");
            inspector4.setInsNivel("Segundo Ciclo Básico");
            inspector4.setInsDireccion("Avenida del Parque");
            inspector4.setInsNumeroDireccion(202);
            inspector4.setInsTipoCasa("Departamento");
            inspector4.setIdComuna(5); 
            funcionarioService.crearInspector(inspector4);

            // Inspector 5
            CrearInspectorRequest inspector5 = new CrearInspectorRequest();
            inspector5.setInsRut(20555666);
            inspector5.setInsDvRut('4');
            inspector5.setInsPrimerNombre("Hector");
            inspector5.setInsApellidoPat("Valenzuela");
            inspector5.setInsApellidoMat("Rios");
            inspector5.setInsEmail("h.valenzuelar@ins.myohiggins.cl");
            inspector5.setInsPassword("inspector123"); 
            inspector5.setInsTel("+56985555555");
            inspector5.setInsTitulo("Técnico en Educación");
            inspector5.setInsNivel("Pabellón Deportivo");
            inspector5.setInsDireccion("Camino Las Lomas");
            inspector5.setInsNumeroDireccion(654);
            inspector5.setInsTipoCasa("Casa");
            inspector5.setIdComuna(14); 
            funcionarioService.crearInspector(inspector5);

            // 4. Crear Estudiante 
            // Estudiante 1
            CrearEstudianteRequest estudiante1 = new CrearEstudianteRequest();
            estudiante1.setEstRut(26000001);
            estudiante1.setEstDvRut('7');
            estudiante1.setEstPrimerNombre("Lucas");
            estudiante1.setEstSegundoNombre("Andres");
            estudiante1.setEstApellidoPat("Molina");
            estudiante1.setEstApellidoMat("Vega");
            estudiante1.setEstEmail("l.molinav@alumno.myohiggins.cl");
            estudiante1.setEstPassword("estudiante123");
            estudiante1.setEstTel("+56940000001");
            estudiante1.setEstDireccion("Pasaje Los Girasoles");
            estudiante1.setEstNumeroDireccion(12);
            estudiante1.setEstTipoCasa("Casa");
            estudiante1.setIdComuna(4);
            estudianteService.crearEstudiante(estudiante1);

            // Estudiante 2
            CrearEstudianteRequest estudiante2 = new CrearEstudianteRequest();
            estudiante2.setEstRut(26000002);
            estudiante2.setEstDvRut('5');
            estudiante2.setEstPrimerNombre("Sofia");
            estudiante2.setEstApellidoPat("Carrasco");
            estudiante2.setEstApellidoMat("Pino");
            estudiante2.setEstEmail("s.carrascop@alumno.myohiggins.cl");
            estudiante2.setEstPassword("estudiante123");
            estudiante2.setEstTel("+56940000002");
            estudiante2.setEstDireccion("Avenida Central");
            estudiante2.setEstNumeroDireccion(345);
            estudiante2.setEstTipoCasa("Departamento");
            estudiante2.setIdComuna(1);
            estudianteService.crearEstudiante(estudiante2);

            // Estudiante 3
            CrearEstudianteRequest estudiante3 = new CrearEstudianteRequest();
            estudiante3.setEstRut(26000003);
            estudiante3.setEstDvRut('3');
            estudiante3.setEstPrimerNombre("Martin");
            estudiante3.setEstSegundoNombre("Ignacio");
            estudiante3.setEstApellidoPat("Rojas");
            estudiante3.setEstApellidoMat("Cortes");
            estudiante3.setEstEmail("m.rojasc@alumno.myohiggins.cl");
            estudiante3.setEstPassword("estudiante123");
            estudiante3.setEstTel("+56940000003");
            estudiante3.setEstDireccion("Calle Las Rosas");
            estudiante3.setEstNumeroDireccion(88);
            estudiante3.setEstTipoCasa("Casa");
            estudiante3.setIdComuna(12);
            estudianteService.crearEstudiante(estudiante3);

            // Estudiante 4
            CrearEstudianteRequest estudiante4 = new CrearEstudianteRequest();
            estudiante4.setEstRut(26000004);
            estudiante4.setEstDvRut('1');
            estudiante4.setEstPrimerNombre("Isidora");
            estudiante4.setEstApellidoPat("Gomez");
            estudiante4.setEstApellidoMat("Salinas");
            estudiante4.setEstEmail("i.gomezs@alumno.myohiggins.cl");
            estudiante4.setEstPassword("estudiante123");
            estudiante4.setEstTel("+56940000004");
            estudiante4.setEstDireccion("Avenida del Mar");
            estudiante4.setEstNumeroDireccion(102);
            estudiante4.setEstTipoCasa("Departamento");
            estudiante4.setIdComuna(2);
            estudianteService.crearEstudiante(estudiante4);

            // Estudiante 5
            CrearEstudianteRequest estudiante5 = new CrearEstudianteRequest();
            estudiante5.setEstRut(26000005);
            estudiante5.setEstDvRut('K');
            estudiante5.setEstPrimerNombre("Agustin");
            estudiante5.setEstSegundoNombre("Alonso");
            estudiante5.setEstApellidoPat("Silva");
            estudiante5.setEstApellidoMat("Tapia");
            estudiante5.setEstEmail("a.silvat@alumno.myohiggins.cl");
            estudiante5.setEstPassword("estudiante123");
            estudiante5.setEstTel("+56940000005");
            estudiante5.setEstDireccion("Camino Real");
            estudiante5.setEstNumeroDireccion(555);
            estudiante5.setEstTipoCasa("Casa");
            estudiante5.setIdComuna(8);
            estudianteService.crearEstudiante(estudiante5);

            // Estudiante 6
            CrearEstudianteRequest estudiante6 = new CrearEstudianteRequest();
            estudiante6.setEstRut(26000006);
            estudiante6.setEstDvRut('8');
            estudiante6.setEstPrimerNombre("Emilia");
            estudiante6.setEstApellidoPat("Contreras");
            estudiante6.setEstApellidoMat("Lagos");
            estudiante6.setEstEmail("e.contrerasl@alumno.myohiggins.cl");
            estudiante6.setEstPassword("estudiante123");
            estudiante6.setEstTel("+56940000006");
            estudiante6.setEstDireccion("Calle Los Tilos");
            estudiante6.setEstNumeroDireccion(90);
            estudiante6.setEstTipoCasa("Departamento");
            estudiante6.setIdComuna(15);
            estudianteService.crearEstudiante(estudiante6);

            // Estudiante 7
            CrearEstudianteRequest estudiante7 = new CrearEstudianteRequest();
            estudiante7.setEstRut(26000007);
            estudiante7.setEstDvRut('6');
            estudiante7.setEstPrimerNombre("Mateo");
            estudiante7.setEstSegundoNombre("Tomas");
            estudiante7.setEstApellidoPat("Morales");
            estudiante7.setEstApellidoMat("Figueroa");
            estudiante7.setEstEmail("m.moralesf@alumno.myohiggins.cl");
            estudiante7.setEstPassword("estudiante123");
            estudiante7.setEstTel("+56940000007");
            estudiante7.setEstDireccion("Pasaje Los Andes");
            estudiante7.setEstNumeroDireccion(11);
            estudiante7.setEstTipoCasa("Casa");
            estudiante7.setIdComuna(5);
            estudianteService.crearEstudiante(estudiante7);

            // Estudiante 8
            CrearEstudianteRequest estudiante8 = new CrearEstudianteRequest();
            estudiante8.setEstRut(26000008);
            estudiante8.setEstDvRut('4');
            estudiante8.setEstPrimerNombre("Florencia");
            estudiante8.setEstApellidoPat("Sepulveda");
            estudiante8.setEstApellidoMat("Araya");
            estudiante8.setEstEmail("f.sepulvedaa@alumno.myohiggins.cl");
            estudiante8.setEstPassword("estudiante123");
            estudiante8.setEstTel("+56940000008");
            estudiante8.setEstDireccion("Avenida Principal");
            estudiante8.setEstNumeroDireccion(776);
            estudiante8.setEstTipoCasa("Casa");
            estudiante8.setIdComuna(9);
            estudianteService.crearEstudiante(estudiante8);

            // Estudiante 9
            CrearEstudianteRequest estudiante9 = new CrearEstudianteRequest();
            estudiante9.setEstRut(26000009);
            estudiante9.setEstDvRut('2');
            estudiante9.setEstPrimerNombre("Joaquin");
            estudiante9.setEstSegundoNombre("Esteban");
            estudiante9.setEstApellidoPat("Fuentes");
            estudiante9.setEstApellidoMat("Garrido");
            estudiante9.setEstEmail("j.fuentesg@alumno.myohiggins.cl");
            estudiante9.setEstPassword("estudiante123");
            estudiante9.setEstTel("+56940000009");
            estudiante9.setEstDireccion("Calle Independencia");
            estudiante9.setEstNumeroDireccion(33);
            estudiante9.setEstTipoCasa("Departamento");
            estudiante9.setIdComuna(7);
            estudianteService.crearEstudiante(estudiante9);

            // Estudiante 10
            CrearEstudianteRequest estudiante10 = new CrearEstudianteRequest();
            estudiante10.setEstRut(26000010);
            estudiante10.setEstDvRut('6');
            estudiante10.setEstPrimerNombre("Antonella");
            estudiante10.setEstApellidoPat("Perez");
            estudiante10.setEstApellidoMat("Moya");
            estudiante10.setEstEmail("a.perezm@alumno.myohiggins.cl");
            estudiante10.setEstPassword("estudiante123");
            estudiante10.setEstTel("+56940000010");
            estudiante10.setEstDireccion("Pasaje La Estrella");
            estudiante10.setEstNumeroDireccion(101);
            estudiante10.setEstTipoCasa("Casa");
            estudiante10.setIdComuna(13);
            estudianteService.crearEstudiante(estudiante10);

            // Estudiante 11
            CrearEstudianteRequest estudiante11 = new CrearEstudianteRequest();
            estudiante11.setEstRut(26000011);
            estudiante11.setEstDvRut('4');
            estudiante11.setEstPrimerNombre("Maximiliano");
            estudiante11.setEstApellidoPat("Valenzuela");
            estudiante11.setEstApellidoMat("Rios");
            estudiante11.setEstEmail("m.valenzuelar@alumno.myohiggins.cl");
            estudiante11.setEstPassword("estudiante123");
            estudiante11.setEstTel("+56940000011");
            estudiante11.setEstDireccion("Avenida Los Pinos");
            estudiante11.setEstNumeroDireccion(444);
            estudiante11.setEstTipoCasa("Casa");
            estudiante11.setIdComuna(3);
            estudianteService.crearEstudiante(estudiante11);

            // Estudiante 12
            CrearEstudianteRequest estudiante12 = new CrearEstudianteRequest();
            estudiante12.setEstRut(26000012);
            estudiante12.setEstDvRut('2');
            estudiante12.setEstPrimerNombre("Martina");
            estudiante12.setEstSegundoNombre("Paz");
            estudiante12.setEstApellidoPat("Caceres");
            estudiante12.setEstApellidoMat("Orellana");
            estudiante12.setEstEmail("m.cacereso@alumno.myohiggins.cl");
            estudiante12.setEstPassword("estudiante123");
            estudiante12.setEstTel("+56940000012");
            estudiante12.setEstDireccion("Calle Los Naranjos");
            estudiante12.setEstNumeroDireccion(56);
            estudiante12.setEstTipoCasa("Departamento");
            estudiante12.setIdComuna(10);
            estudianteService.crearEstudiante(estudiante12);

            // Estudiante 13
            CrearEstudianteRequest estudiante13 = new CrearEstudianteRequest();
            estudiante13.setEstRut(26000013);
            estudiante13.setEstDvRut('0');
            estudiante13.setEstPrimerNombre("Tomas");
            estudiante13.setEstApellidoPat("Herrera");
            estudiante13.setEstApellidoMat("Vidal");
            estudiante13.setEstEmail("t.herrerav@alumno.myohiggins.cl");
            estudiante13.setEstPassword("estudiante123");
            estudiante13.setEstTel("+56940000013");
            estudiante13.setEstDireccion("Avenida Libertad");
            estudiante13.setEstNumeroDireccion(890);
            estudiante13.setEstTipoCasa("Casa");
            estudiante13.setIdComuna(11);
            estudianteService.crearEstudiante(estudiante13);

            // Estudiante 14
            CrearEstudianteRequest estudiante14 = new CrearEstudianteRequest();
            estudiante14.setEstRut(26000014);
            estudiante14.setEstDvRut('9');
            estudiante14.setEstPrimerNombre("Catalina");
            estudiante14.setEstApellidoPat("Castillo");
            estudiante14.setEstApellidoMat("Soto");
            estudiante14.setEstEmail("c.castillos@alumno.myohiggins.cl");
            estudiante14.setEstPassword("estudiante123");
            estudiante14.setEstTel("+56940000014");
            estudiante14.setEstDireccion("Pasaje El Sol");
            estudiante14.setEstNumeroDireccion(23);
            estudiante14.setEstTipoCasa("Casa");
            estudiante14.setIdComuna(6);
            estudianteService.crearEstudiante(estudiante14);

            // Estudiante 15
            CrearEstudianteRequest estudiante15 = new CrearEstudianteRequest();
            estudiante15.setEstRut(26000015);
            estudiante15.setEstDvRut('7');
            estudiante15.setEstPrimerNombre("Gabriel");
            estudiante15.setEstSegundoNombre("Alonso");
            estudiante15.setEstApellidoPat("Bravo");
            estudiante15.setEstApellidoMat("Navarro");
            estudiante15.setEstEmail("g.bravon@alumno.myohiggins.cl");
            estudiante15.setEstPassword("estudiante123");
            estudiante15.setEstTel("+56940000015");
            estudiante15.setEstDireccion("Calle Los Almendros");
            estudiante15.setEstNumeroDireccion(404);
            estudiante15.setEstTipoCasa("Departamento");
            estudiante15.setIdComuna(14);
            estudianteService.crearEstudiante(estudiante15);

            // Estudiante 16
            CrearEstudianteRequest estudiante16 = new CrearEstudianteRequest();
            estudiante16.setEstRut(26000016);
            estudiante16.setEstDvRut('5');
            estudiante16.setEstPrimerNombre("Julieta");
            estudiante16.setEstApellidoPat("Guzman");
            estudiante16.setEstApellidoMat("Paredes");
            estudiante16.setEstEmail("j.guzmanp@alumno.myohiggins.cl");
            estudiante16.setEstPassword("estudiante123");
            estudiante16.setEstTel("+56940000016");
            estudiante16.setEstDireccion("Avenida Sur");
            estudiante16.setEstNumeroDireccion(1020);
            estudiante16.setEstTipoCasa("Casa");
            estudiante16.setIdComuna(2);
            estudianteService.crearEstudiante(estudiante16);

            // Estudiante 17
            CrearEstudianteRequest estudiante17 = new CrearEstudianteRequest();
            estudiante17.setEstRut(26000017);
            estudiante17.setEstDvRut('3');
            estudiante17.setEstPrimerNombre("Felipe");
            estudiante17.setEstSegundoNombre("Andres");
            estudiante17.setEstApellidoPat("Gallardo");
            estudiante17.setEstApellidoMat("Leiva");
            estudiante17.setEstEmail("f.gallardol@alumno.myohiggins.cl");
            estudiante17.setEstPassword("estudiante123");
            estudiante17.setEstTel("+56940000017");
            estudiante17.setEstDireccion("Pasaje Bicentenario");
            estudiante17.setEstNumeroDireccion(99);
            estudiante17.setEstTipoCasa("Casa");
            estudiante17.setIdComuna(1);
            estudianteService.crearEstudiante(estudiante17);

            // Estudiante 18
            CrearEstudianteRequest estudiante18 = new CrearEstudianteRequest();
            estudiante18.setEstRut(26000018);
            estudiante18.setEstDvRut('1');
            estudiante18.setEstPrimerNombre("Amelia");
            estudiante18.setEstApellidoPat("Poblete");
            estudiante18.setEstApellidoMat("Godoy");
            estudiante18.setEstEmail("a.pobleteg@alumno.myohiggins.cl");
            estudiante18.setEstPassword("estudiante123");
            estudiante18.setEstTel("+56940000018");
            estudiante18.setEstDireccion("Calle Nueva");
            estudiante18.setEstNumeroDireccion(201);
            estudiante18.setEstTipoCasa("Departamento");
            estudiante18.setIdComuna(4);
            estudianteService.crearEstudiante(estudiante18);

            // Estudiante 19
            CrearEstudianteRequest estudiante19 = new CrearEstudianteRequest();
            estudiante19.setEstRut(26000019);
            estudiante19.setEstDvRut('K');
            estudiante19.setEstPrimerNombre("Diego");
            estudiante19.setEstApellidoPat("Saavedra");
            estudiante19.setEstApellidoMat("Vergara");
            estudiante19.setEstEmail("d.saavedrav@alumno.myohiggins.cl");
            estudiante19.setEstPassword("estudiante123");
            estudiante19.setEstTel("+56940000019");
            estudiante19.setEstDireccion("Avenida Costanera");
            estudiante19.setEstNumeroDireccion(665);
            estudiante19.setEstTipoCasa("Casa");
            estudiante19.setIdComuna(8);
            estudianteService.crearEstudiante(estudiante19);

            // Estudiante 20
            CrearEstudianteRequest estudiante20 = new CrearEstudianteRequest();
            estudiante20.setEstRut(26000020);
            estudiante20.setEstDvRut('5');
            estudiante20.setEstPrimerNombre("Victoria");
            estudiante20.setEstSegundoNombre("Isabel");
            estudiante20.setEstApellidoPat("Mendoza");
            estudiante20.setEstApellidoMat("Cardenas");
            estudiante20.setEstEmail("v.mendozac@alumno.myohiggins.cl");
            estudiante20.setEstPassword("estudiante123");
            estudiante20.setEstTel("+56940000020");
            estudiante20.setEstDireccion("Calle Las Violetas");
            estudiante20.setEstNumeroDireccion(42);
            estudiante20.setEstTipoCasa("Casa");
            estudiante20.setIdComuna(11);
            estudianteService.crearEstudiante(estudiante20);

            // Estudiante 21
            CrearEstudianteRequest estudiante21 = new CrearEstudianteRequest();
            estudiante21.setEstRut(26000021);
            estudiante21.setEstDvRut('3');
            estudiante21.setEstPrimerNombre("Vicente");
            estudiante21.setEstApellidoPat("Urrutia");
            estudiante21.setEstApellidoMat("Acuña");
            estudiante21.setEstEmail("v.urrutiaa@alumno.myohiggins.cl");
            estudiante21.setEstPassword("estudiante123");
            estudiante21.setEstTel("+56940000021");
            estudiante21.setEstDireccion("Pasaje Los Pinguinos");
            estudiante21.setEstNumeroDireccion(888);
            estudiante21.setEstTipoCasa("Departamento");
            estudiante21.setIdComuna(13);
            estudianteService.crearEstudiante(estudiante21);

            // Estudiante 22
            CrearEstudianteRequest estudiante22 = new CrearEstudianteRequest();
            estudiante22.setEstRut(26000022);
            estudiante22.setEstDvRut('1');
            estudiante22.setEstPrimerNombre("Maite");
            estudiante22.setEstApellidoPat("Garrido");
            estudiante22.setEstApellidoMat("Bustos");
            estudiante22.setEstEmail("m.garridob@alumno.myohiggins.cl");
            estudiante22.setEstPassword("estudiante123");
            estudiante22.setEstTel("+56940000022");
            estudiante22.setEstDireccion("Avenida Las Torres");
            estudiante22.setEstNumeroDireccion(1011);
            estudiante22.setEstTipoCasa("Casa");
            estudiante22.setIdComuna(15);
            estudianteService.crearEstudiante(estudiante22);

            // Estudiante 23
            CrearEstudianteRequest estudiante23 = new CrearEstudianteRequest();
            estudiante23.setEstRut(26000023);
            estudiante23.setEstDvRut('K');
            estudiante23.setEstPrimerNombre("Nicolas");
            estudiante23.setEstSegundoNombre("Matias");
            estudiante23.setEstApellidoPat("Peña");
            estudiante23.setEstApellidoMat("Vera");
            estudiante23.setEstEmail("n.penav@alumno.myohiggins.cl");
            estudiante23.setEstPassword("estudiante123");
            estudiante23.setEstTel("+56940000023");
            estudiante23.setEstDireccion("Calle El Alba");
            estudiante23.setEstNumeroDireccion(254);
            estudiante23.setEstTipoCasa("Casa");
            estudiante23.setIdComuna(7);
            estudianteService.crearEstudiante(estudiante23);

            // Estudiante 24
            CrearEstudianteRequest estudiante24 = new CrearEstudianteRequest();
            estudiante24.setEstRut(26000024);
            estudiante24.setEstDvRut('8');
            estudiante24.setEstPrimerNombre("Josefa");
            estudiante24.setEstApellidoPat("Cortes");
            estudiante24.setEstApellidoMat("Osorio");
            estudiante24.setEstEmail("j.corteso@alumno.myohiggins.cl");
            estudiante24.setEstPassword("estudiante123");
            estudiante24.setEstTel("+56940000024");
            estudiante24.setEstDireccion("Avenida Brasil");
            estudiante24.setEstNumeroDireccion(96);
            estudiante24.setEstTipoCasa("Departamento");
            estudiante24.setIdComuna(3);
            estudianteService.crearEstudiante(estudiante24);

            // Estudiante 25
            CrearEstudianteRequest estudiante25 = new CrearEstudianteRequest();
            estudiante25.setEstRut(26000025);
            estudiante25.setEstDvRut('6');
            estudiante25.setEstPrimerNombre("Renato");
            estudiante25.setEstApellidoPat("Donoso");
            estudiante25.setEstApellidoMat("Perez");
            estudiante25.setEstEmail("r.donosop@alumno.myohiggins.cl");
            estudiante25.setEstPassword("estudiante123");
            estudiante25.setEstTel("+56940000025");
            estudiante25.setEstDireccion("Pasaje Las Lilas");
            estudiante25.setEstNumeroDireccion(14);
            estudiante25.setEstTipoCasa("Casa");
            estudiante25.setIdComuna(5);
            estudianteService.crearEstudiante(estudiante25);

            // Estudiante 26
            CrearEstudianteRequest estudiante26 = new CrearEstudianteRequest();
            estudiante26.setEstRut(26000026);
            estudiante26.setEstDvRut('4');
            estudiante26.setEstPrimerNombre("Amanda");
            estudiante26.setEstSegundoNombre("Belen");
            estudiante26.setEstApellidoPat("Moya");
            estudiante26.setEstApellidoMat("Gutierrez");
            estudiante26.setEstEmail("a.moyag@alumno.myohiggins.cl");
            estudiante26.setEstPassword("estudiante123");
            estudiante26.setEstTel("+56940000026");
            estudiante26.setEstDireccion("Calle Los Copihues");
            estudiante26.setEstNumeroDireccion(785);
            estudiante26.setEstTipoCasa("Casa");
            estudiante26.setIdComuna(9);
            estudianteService.crearEstudiante(estudiante26);

            // Estudiante 27
            CrearEstudianteRequest estudiante27 = new CrearEstudianteRequest();
            estudiante27.setEstRut(26000027);
            estudiante27.setEstDvRut('2');
            estudiante27.setEstPrimerNombre("Jeronimo");
            estudiante27.setEstApellidoPat("Rios");
            estudiante27.setEstApellidoMat("Lara");
            estudiante27.setEstEmail("j.riosl@alumno.myohiggins.cl");
            estudiante27.setEstPassword("estudiante123");
            estudiante27.setEstTel("+56940000027");
            estudiante27.setEstDireccion("Avenida Las Perdices");
            estudiante27.setEstNumeroDireccion(109);
            estudiante27.setEstTipoCasa("Departamento");
            estudiante27.setIdComuna(12);
            estudianteService.crearEstudiante(estudiante27);

            // Estudiante 28
            CrearEstudianteRequest estudiante28 = new CrearEstudianteRequest();
            estudiante28.setEstRut(26000028);
            estudiante28.setEstDvRut('0');
            estudiante28.setEstPrimerNombre("Trinidad");
            estudiante28.setEstApellidoPat("Salazar");
            estudiante28.setEstApellidoMat("Munoz");
            estudiante28.setEstEmail("t.salazarm@alumno.myohiggins.cl");
            estudiante28.setEstPassword("estudiante123");
            estudiante28.setEstTel("+56940000028");
            estudiante28.setEstDireccion("Pasaje Los Canelos");
            estudiante28.setEstNumeroDireccion(332);
            estudiante28.setEstTipoCasa("Casa");
            estudiante28.setIdComuna(6);
            estudianteService.crearEstudiante(estudiante28);

            // Estudiante 29
            CrearEstudianteRequest estudiante29 = new CrearEstudianteRequest();
            estudiante29.setEstRut(26000029);
            estudiante29.setEstDvRut('9');
            estudiante29.setEstPrimerNombre("Alonso");
            estudiante29.setEstSegundoNombre("Patricio");
            estudiante29.setEstApellidoPat("Navarrete");
            estudiante29.setEstApellidoMat("Arias");
            estudiante29.setEstEmail("a.navarretea@alumno.myohiggins.cl");
            estudiante29.setEstPassword("estudiante123");
            estudiante29.setEstTel("+56940000029");
            estudiante29.setEstDireccion("Calle O'Higgins");
            estudiante29.setEstNumeroDireccion(881);
            estudiante29.setEstTipoCasa("Casa");
            estudiante29.setIdComuna(2);
            estudianteService.crearEstudiante(estudiante29);

            // Estudiante 30
            CrearEstudianteRequest estudiante30 = new CrearEstudianteRequest();
            estudiante30.setEstRut(26000030);
            estudiante30.setEstDvRut('4');
            estudiante30.setEstPrimerNombre("Samantha");
            estudiante30.setEstApellidoPat("Reyes");
            estudiante30.setEstApellidoMat("Fierro");
            estudiante30.setEstEmail("s.reyesf@alumno.myohiggins.cl");
            estudiante30.setEstPassword("estudiante123");
            estudiante30.setEstTel("+56940000030");
            estudiante30.setEstDireccion("Avenida España");
            estudiante30.setEstNumeroDireccion(65);
            estudiante30.setEstTipoCasa("Departamento");
            estudiante30.setIdComuna(14);
            estudianteService.crearEstudiante(estudiante30);

            // Estudiante 31
            CrearEstudianteRequest estudiante31 = new CrearEstudianteRequest();
            estudiante31.setEstRut(26000031);
            estudiante31.setEstDvRut('2');
            estudiante31.setEstPrimerNombre("Maximo");
            estudiante31.setEstApellidoPat("Vidal");
            estudiante31.setEstApellidoMat("Correa");
            estudiante31.setEstEmail("m.vidalc@alumno.myohiggins.cl");
            estudiante31.setEstPassword("estudiante123");
            estudiante31.setEstTel("+56940000031");
            estudiante31.setEstDireccion("Pasaje Las Encinas");
            estudiante31.setEstNumeroDireccion(105);
            estudiante31.setEstTipoCasa("Casa");
            estudiante31.setIdComuna(4);
            estudianteService.crearEstudiante(estudiante31);

            // Estudiante 32
            CrearEstudianteRequest estudiante32 = new CrearEstudianteRequest();
            estudiante32.setEstRut(26000032);
            estudiante32.setEstDvRut('0');
            estudiante32.setEstPrimerNombre("Colomba");
            estudiante32.setEstSegundoNombre("Rafaela");
            estudiante32.setEstApellidoPat("Zuniga");
            estudiante32.setEstApellidoMat("Paredes");
            estudiante32.setEstEmail("c.zunigap@alumno.myohiggins.cl");
            estudiante32.setEstPassword("estudiante123");
            estudiante32.setEstTel("+56940000032");
            estudiante32.setEstDireccion("Calle Los Robles");
            estudiante32.setEstNumeroDireccion(330);
            estudiante32.setEstTipoCasa("Casa");
            estudiante32.setIdComuna(8);
            estudianteService.crearEstudiante(estudiante32);

            // Estudiante 33
            CrearEstudianteRequest estudiante33 = new CrearEstudianteRequest();
            estudiante33.setEstRut(26000033);
            estudiante33.setEstDvRut('9');
            estudiante33.setEstPrimerNombre("Cristobal");
            estudiante33.setEstApellidoPat("Aedo");
            estudiante33.setEstApellidoMat("Villegas");
            estudiante33.setEstEmail("c.aedov@alumno.myohiggins.cl");
            estudiante33.setEstPassword("estudiante123");
            estudiante33.setEstTel("+56940000033");
            estudiante33.setEstDireccion("Avenida Caupolican");
            estudiante33.setEstNumeroDireccion(710);
            estudiante33.setEstTipoCasa("Departamento");
            estudiante33.setIdComuna(1);
            estudianteService.crearEstudiante(estudiante33);

            // Estudiante 34
            CrearEstudianteRequest estudiante34 = new CrearEstudianteRequest();
            estudiante34.setEstRut(26000034);
            estudiante34.setEstDvRut('7');
            estudiante34.setEstPrimerNombre("Emilia");
            estudiante34.setEstApellidoPat("Lagos");
            estudiante34.setEstApellidoMat("Ortiz");
            estudiante34.setEstEmail("e.lagoso@alumno.myohiggins.cl");
            estudiante34.setEstPassword("estudiante123");
            estudiante34.setEstTel("+56940000034");
            estudiante34.setEstDireccion("Pasaje Los Notros");
            estudiante34.setEstNumeroDireccion(18);
            estudiante34.setEstTipoCasa("Casa");
            estudiante34.setIdComuna(10);
            estudianteService.crearEstudiante(estudiante34);

            // Estudiante 35
            CrearEstudianteRequest estudiante35 = new CrearEstudianteRequest();
            estudiante35.setEstRut(26000035);
            estudiante35.setEstDvRut('5');
            estudiante35.setEstPrimerNombre("Daniel");
            estudiante35.setEstSegundoNombre("Eduardo");
            estudiante35.setEstApellidoPat("Castro");
            estudiante35.setEstApellidoMat("Fuenzalida");
            estudiante35.setEstEmail("d.castrof@alumno.myohiggins.cl");
            estudiante35.setEstPassword("estudiante123");
            estudiante35.setEstTel("+56940000035");
            estudiante35.setEstDireccion("Calle San Martin");
            estudiante35.setEstNumeroDireccion(555);
            estudiante35.setEstTipoCasa("Casa");
            estudiante35.setIdComuna(12);
            estudianteService.crearEstudiante(estudiante35);

            // Estudiante 36
            CrearEstudianteRequest estudiante36 = new CrearEstudianteRequest();
            estudiante36.setEstRut(26000036);
            estudiante36.setEstDvRut('3');
            estudiante36.setEstPrimerNombre("Belen");
            estudiante36.setEstApellidoPat("Escobar");
            estudiante36.setEstApellidoMat("Jara");
            estudiante36.setEstEmail("b.escobarj@alumno.myohiggins.cl");
            estudiante36.setEstPassword("estudiante123");
            estudiante36.setEstTel("+56940000036");
            estudiante36.setEstDireccion("Avenida Pedro de Valdivia");
            estudiante36.setEstNumeroDireccion(912);
            estudiante36.setEstTipoCasa("Departamento");
            estudiante36.setIdComuna(3);
            estudianteService.crearEstudiante(estudiante36);

            // Estudiante 37
            CrearEstudianteRequest estudiante37 = new CrearEstudianteRequest();
            estudiante37.setEstRut(26000037);
            estudiante37.setEstDvRut('1');
            estudiante37.setEstPrimerNombre("Pedro");
            estudiante37.setEstApellidoPat("Orellana");
            estudiante37.setEstApellidoMat("Martinez");
            estudiante37.setEstEmail("p.orellanam@alumno.myohiggins.cl");
            estudiante37.setEstPassword("estudiante123");
            estudiante37.setEstTel("+56940000037");
            estudiante37.setEstDireccion("Pasaje Los Abedules");
            estudiante37.setEstNumeroDireccion(77);
            estudiante37.setEstTipoCasa("Casa");
            estudiante37.setIdComuna(15);
            estudianteService.crearEstudiante(estudiante37);

            // Estudiante 38
            CrearEstudianteRequest estudiante38 = new CrearEstudianteRequest();
            estudiante38.setEstRut(26000038);
            estudiante38.setEstDvRut('K');
            estudiante38.setEstPrimerNombre("Mia");
            estudiante38.setEstSegundoNombre("Ignacia");
            estudiante38.setEstApellidoPat("Guzman");
            estudiante38.setEstApellidoMat("Carvajal");
            estudiante38.setEstEmail("m.guzmanc@alumno.myohiggins.cl");
            estudiante38.setEstPassword("estudiante123");
            estudiante38.setEstTel("+56940000038");
            estudiante38.setEstDireccion("Calle Los Maitenes");
            estudiante38.setEstNumeroDireccion(620);
            estudiante38.setEstTipoCasa("Casa");
            estudiante38.setIdComuna(5);
            estudianteService.crearEstudiante(estudiante38);

            // Estudiante 39
            CrearEstudianteRequest estudiante39 = new CrearEstudianteRequest();
            estudiante39.setEstRut(26000039);
            estudiante39.setEstDvRut('8');
            estudiante39.setEstPrimerNombre("Bruno");
            estudiante39.setEstApellidoPat("Pino");
            estudiante39.setEstApellidoMat("Valdes");
            estudiante39.setEstEmail("b.pinov@alumno.myohiggins.cl");
            estudiante39.setEstPassword("estudiante123");
            estudiante39.setEstTel("+56940000039");
            estudiante39.setEstDireccion("Avenida Bicentenario");
            estudiante39.setEstNumeroDireccion(112);
            estudiante39.setEstTipoCasa("Departamento");
            estudiante39.setIdComuna(9);
            estudianteService.crearEstudiante(estudiante39);

            // Estudiante 40
            CrearEstudianteRequest estudiante40 = new CrearEstudianteRequest();
            estudiante40.setEstRut(26000040);
            estudiante40.setEstDvRut('3');
            estudiante40.setEstPrimerNombre("Leonor");
            estudiante40.setEstApellidoPat("Santana");
            estudiante40.setEstApellidoMat("Roman");
            estudiante40.setEstEmail("l.santanar@alumno.myohiggins.cl");
            estudiante40.setEstPassword("estudiante123");
            estudiante40.setEstTel("+56940000040");
            estudiante40.setEstDireccion("Pasaje La Amistad");
            estudiante40.setEstNumeroDireccion(49);
            estudiante40.setEstTipoCasa("Casa");
            estudiante40.setIdComuna(13);
            estudianteService.crearEstudiante(estudiante40);


            // 5. Crear Apoderado
            // Apoderado 1 (Estudiante 1: Hermano - Molina Vega)
            CrearApoderadoRequest apoderado1 = new CrearApoderadoRequest();
            apoderado1.setApoRut(15000001);
            apoderado1.setApoDvRut('7');
            apoderado1.setApoPrimerNombre("Felipe");
            apoderado1.setApoApellidoPat("Molina");
            apoderado1.setApoApellidoMat("Vega");
            apoderado1.setApoEmail("f.molinav@apoderado.myohiggins.cl");
            apoderado1.setApoPassword("apoderado123");
            apoderado1.setApoTel("+56950000001");
            apoderado1.setApoDireccion("Pasaje Los Girasoles");
            apoderado1.setApoNumeroDireccion(12);
            apoderado1.setApoTipoCasa("Casa");
            apoderado1.setIdComuna(4);
            apoderadoService.crearApoderado(apoderado1);

            // Apoderado 2 (Estudiante 2: Hija - Carrasco Pino)
            CrearApoderadoRequest apoderado2 = new CrearApoderadoRequest();
            apoderado2.setApoRut(15000002);
            apoderado2.setApoDvRut('5');
            apoderado2.setApoPrimerNombre("Claudia");
            apoderado2.setApoApellidoPat("Pino");
            apoderado2.setApoApellidoMat("Salinas");
            apoderado2.setApoEmail("c.pinos@apoderado.myohiggins.cl");
            apoderado2.setApoPassword("apoderado123");
            apoderado2.setApoTel("+56950000002");
            apoderado2.setApoDireccion("Avenida Central");
            apoderado2.setApoNumeroDireccion(345);
            apoderado2.setApoTipoCasa("Departamento");
            apoderado2.setIdComuna(1);
            apoderadoService.crearApoderado(apoderado2);

            // Apoderado 3 (Estudiante 3: Hijo - Rojas Cortes)
            CrearApoderadoRequest apoderado3 = new CrearApoderadoRequest();
            apoderado3.setApoRut(15000003);
            apoderado3.setApoDvRut('3');
            apoderado3.setApoPrimerNombre("Juan");
            apoderado3.setApoApellidoPat("Rojas");
            apoderado3.setApoApellidoMat("Mendez");
            apoderado3.setApoEmail("j.rojasm@apoderado.myohiggins.cl");
            apoderado3.setApoPassword("apoderado123");
            apoderado3.setApoTel("+56950000003");
            apoderado3.setApoDireccion("Calle Las Rosas");
            apoderado3.setApoNumeroDireccion(88);
            apoderado3.setApoTipoCasa("Casa");
            apoderado3.setIdComuna(12);
            apoderadoService.crearApoderado(apoderado3);

            // Apoderado 4 (Estudiante 4: Tía - Gomez Salinas)
            CrearApoderadoRequest apoderado4 = new CrearApoderadoRequest();
            apoderado4.setApoRut(15000004);
            apoderado4.setApoDvRut('1');
            apoderado4.setApoPrimerNombre("Marta");
            apoderado4.setApoApellidoPat("Salinas");
            apoderado4.setApoApellidoMat("Perez");
            apoderado4.setApoEmail("m.salinasp@apoderado.myohiggins.cl");
            apoderado4.setApoPassword("apoderado123");
            apoderado4.setApoTel("+56950000004");
            apoderado4.setApoDireccion("Avenida del Mar");
            apoderado4.setApoNumeroDireccion(102);
            apoderado4.setApoTipoCasa("Departamento");
            apoderado4.setIdComuna(2);
            apoderadoService.crearApoderado(apoderado4);

            // Apoderado 5 (Estudiante 5: Hijo - Silva Tapia)
            CrearApoderadoRequest apoderado5 = new CrearApoderadoRequest();
            apoderado5.setApoRut(15000005);
            apoderado5.setApoDvRut('K');
            apoderado5.setApoPrimerNombre("Andrea");
            apoderado5.setApoApellidoPat("Tapia");
            apoderado5.setApoApellidoMat("Valdes");
            apoderado5.setApoEmail("a.tapiav@apoderado.myohiggins.cl");
            apoderado5.setApoPassword("apoderado123");
            apoderado5.setApoTel("+56950000005");
            apoderado5.setApoDireccion("Camino Real");
            apoderado5.setApoNumeroDireccion(555);
            apoderado5.setApoTipoCasa("Casa");
            apoderado5.setIdComuna(8);
            apoderadoService.crearApoderado(apoderado5);

            // Apoderado 6 (Estudiante 6: Hija - Contreras Lagos)
            CrearApoderadoRequest apoderado6 = new CrearApoderadoRequest();
            apoderado6.setApoRut(15000006);
            apoderado6.setApoDvRut('8');
            apoderado6.setApoPrimerNombre("Luis");
            apoderado6.setApoApellidoPat("Contreras");
            apoderado6.setApoApellidoMat("Soto");
            apoderado6.setApoEmail("l.contrerass@apoderado.myohiggins.cl");
            apoderado6.setApoPassword("apoderado123");
            apoderado6.setApoTel("+56950000006");
            apoderado6.setApoDireccion("Calle Los Tilos");
            apoderado6.setApoNumeroDireccion(90);
            apoderado6.setApoTipoCasa("Departamento");
            apoderado6.setIdComuna(15);
            apoderadoService.crearApoderado(apoderado6);

            // Apoderado 7 (Estudiante 7: Abuela - Morales Figueroa)
            CrearApoderadoRequest apoderado7 = new CrearApoderadoRequest();
            apoderado7.setApoRut(15000007);
            apoderado7.setApoDvRut('6');
            apoderado7.setApoPrimerNombre("Carmen");
            apoderado7.setApoApellidoPat("Morales");
            apoderado7.setApoApellidoMat("Rios");
            apoderado7.setApoEmail("c.moralesr@apoderado.myohiggins.cl");
            apoderado7.setApoPassword("apoderado123");
            apoderado7.setApoTel("+56950000007");
            apoderado7.setApoDireccion("Pasaje Los Andes");
            apoderado7.setApoNumeroDireccion(11);
            apoderado7.setApoTipoCasa("Casa");
            apoderado7.setIdComuna(5);
            apoderadoService.crearApoderado(apoderado7);

            // Apoderado 8 (Estudiante 8: Hija - Sepulveda Araya)
            CrearApoderadoRequest apoderado8 = new CrearApoderadoRequest();
            apoderado8.setApoRut(15000008);
            apoderado8.setApoDvRut('4');
            apoderado8.setApoPrimerNombre("Daniela");
            apoderado8.setApoApellidoPat("Araya");
            apoderado8.setApoApellidoMat("Castro");
            apoderado8.setApoEmail("d.arayac@apoderado.myohiggins.cl");
            apoderado8.setApoPassword("apoderado123");
            apoderado8.setApoTel("+56950000008");
            apoderado8.setApoDireccion("Avenida Principal");
            apoderado8.setApoNumeroDireccion(776);
            apoderado8.setApoTipoCasa("Casa");
            apoderado8.setIdComuna(9);
            apoderadoService.crearApoderado(apoderado8);

            // Apoderado 9 (Estudiante 9: Hijo - Fuentes Garrido)
            CrearApoderadoRequest apoderado9 = new CrearApoderadoRequest();
            apoderado9.setApoRut(15000009);
            apoderado9.setApoDvRut('2');
            apoderado9.setApoPrimerNombre("Rodrigo");
            apoderado9.setApoApellidoPat("Fuentes");
            apoderado9.setApoApellidoMat("Leal");
            apoderado9.setApoEmail("r.fuentesl@apoderado.myohiggins.cl");
            apoderado9.setApoPassword("apoderado123");
            apoderado9.setApoTel("+56950000009");
            apoderado9.setApoDireccion("Calle Independencia");
            apoderado9.setApoNumeroDireccion(33);
            apoderado9.setApoTipoCasa("Departamento");
            apoderado9.setIdComuna(7);
            apoderadoService.crearApoderado(apoderado9);

            // Apoderado 10 (Estudiante 10: Nieto - Perez Moya)
            CrearApoderadoRequest apoderado10 = new CrearApoderadoRequest();
            apoderado10.setApoRut(15000010);
            apoderado10.setApoDvRut('6');
            apoderado10.setApoPrimerNombre("Hugo");
            apoderado10.setApoApellidoPat("Perez");
            apoderado10.setApoApellidoMat("Guzman");
            apoderado10.setApoEmail("h.perezg@apoderado.myohiggins.cl");
            apoderado10.setApoPassword("apoderado123");
            apoderado10.setApoTel("+56950000010");
            apoderado10.setApoDireccion("Pasaje La Estrella");
            apoderado10.setApoNumeroDireccion(101);
            apoderado10.setApoTipoCasa("Casa");
            apoderado10.setIdComuna(13);
            apoderadoService.crearApoderado(apoderado10);

            // Apoderado 11 (Estudiante 11: Hijo - Valenzuela Rios)
            CrearApoderadoRequest apoderado11 = new CrearApoderadoRequest();
            apoderado11.setApoRut(15000011);
            apoderado11.setApoDvRut('4');
            apoderado11.setApoPrimerNombre("Loreto");
            apoderado11.setApoApellidoPat("Rios");
            apoderado11.setApoApellidoMat("Silva");
            apoderado11.setApoEmail("l.rioss@apoderado.myohiggins.cl");
            apoderado11.setApoPassword("apoderado123");
            apoderado11.setApoTel("+56950000011");
            apoderado11.setApoDireccion("Avenida Los Pinos");
            apoderado11.setApoNumeroDireccion(444);
            apoderado11.setApoTipoCasa("Casa");
            apoderado11.setIdComuna(3);
            apoderadoService.crearApoderado(apoderado11);

            // Apoderado 12 (Estudiante 12: Hija - Caceres Orellana)
            CrearApoderadoRequest apoderado12 = new CrearApoderadoRequest();
            apoderado12.setApoRut(15000012);
            apoderado12.setApoDvRut('2');
            apoderado12.setApoPrimerNombre("Esteban");
            apoderado12.setApoApellidoPat("Caceres");
            apoderado12.setApoApellidoMat("Muñoz");
            apoderado12.setApoEmail("e.caceresm@apoderado.myohiggins.cl");
            apoderado12.setApoPassword("apoderado123");
            apoderado12.setApoTel("+56950000012");
            apoderado12.setApoDireccion("Calle Los Naranjos");
            apoderado12.setApoNumeroDireccion(56);
            apoderado12.setApoTipoCasa("Departamento");
            apoderado12.setIdComuna(10);
            apoderadoService.crearApoderado(apoderado12);

            // Apoderado 13 (Estudiante 13: Tutor Legal - Herrera Vidal)
            CrearApoderadoRequest apoderado13 = new CrearApoderadoRequest();
            apoderado13.setApoRut(15000013);
            apoderado13.setApoDvRut('0');
            apoderado13.setApoPrimerNombre("Raul");
            apoderado13.setApoApellidoPat("Herrera");
            apoderado13.setApoApellidoMat("Pino");
            apoderado13.setApoEmail("r.herrerap@apoderado.myohiggins.cl");
            apoderado13.setApoPassword("apoderado123");
            apoderado13.setApoTel("+56950000013");
            apoderado13.setApoDireccion("Avenida Libertad");
            apoderado13.setApoNumeroDireccion(890);
            apoderado13.setApoTipoCasa("Casa");
            apoderado13.setIdComuna(11);
            apoderadoService.crearApoderado(apoderado13);

            // Apoderado 14 (Estudiante 14: Hija - Castillo Soto)
            CrearApoderadoRequest apoderado14 = new CrearApoderadoRequest();
            apoderado14.setApoRut(15000014);
            apoderado14.setApoDvRut('9');
            apoderado14.setApoPrimerNombre("Valeria");
            apoderado14.setApoApellidoPat("Soto");
            apoderado14.setApoApellidoMat("Moya");
            apoderado14.setApoEmail("v.sotom@apoderado.myohiggins.cl");
            apoderado14.setApoPassword("apoderado123");
            apoderado14.setApoTel("+56950000014");
            apoderado14.setApoDireccion("Pasaje El Sol");
            apoderado14.setApoNumeroDireccion(23);
            apoderado14.setApoTipoCasa("Casa");
            apoderado14.setIdComuna(6);
            apoderadoService.crearApoderado(apoderado14);

            // Apoderado 15 (Estudiante 15: Hijo - Bravo Navarro)
            CrearApoderadoRequest apoderado15 = new CrearApoderadoRequest();
            apoderado15.setApoRut(15000015);
            apoderado15.setApoDvRut('7');
            apoderado15.setApoPrimerNombre("Mario");
            apoderado15.setApoApellidoPat("Bravo");
            apoderado15.setApoApellidoMat("Orellana");
            apoderado15.setApoEmail("m.bravoo@apoderado.myohiggins.cl");
            apoderado15.setApoPassword("apoderado123");
            apoderado15.setApoTel("+56950000015");
            apoderado15.setApoDireccion("Calle Los Almendros");
            apoderado15.setApoNumeroDireccion(404);
            apoderado15.setApoTipoCasa("Departamento");
            apoderado15.setIdComuna(14);
            apoderadoService.crearApoderado(apoderado15);

            // Apoderado 16 (Estudiante 16: Hermana - Guzman Paredes)
            CrearApoderadoRequest apoderado16 = new CrearApoderadoRequest();
            apoderado16.setApoRut(15000016);
            apoderado16.setApoDvRut('5');
            apoderado16.setApoPrimerNombre("Camila");
            apoderado16.setApoApellidoPat("Guzman");
            apoderado16.setApoApellidoMat("Paredes");
            apoderado16.setApoEmail("c.guzmanp@apoderado.myohiggins.cl");
            apoderado16.setApoPassword("apoderado123");
            apoderado16.setApoTel("+56950000016");
            apoderado16.setApoDireccion("Avenida Sur");
            apoderado16.setApoNumeroDireccion(1020);
            apoderado16.setApoTipoCasa("Casa");
            apoderado16.setIdComuna(2);
            apoderadoService.crearApoderado(apoderado16);

            // Apoderado 17 (Estudiante 17: Hijo - Gallardo Leiva)
            CrearApoderadoRequest apoderado17 = new CrearApoderadoRequest();
            apoderado17.setApoRut(15000017);
            apoderado17.setApoDvRut('3');
            apoderado17.setApoPrimerNombre("Teresa");
            apoderado17.setApoApellidoPat("Leiva");
            apoderado17.setApoApellidoMat("Cortes");
            apoderado17.setApoEmail("t.leivac@apoderado.myohiggins.cl");
            apoderado17.setApoPassword("apoderado123");
            apoderado17.setApoTel("+56950000017");
            apoderado17.setApoDireccion("Pasaje Bicentenario");
            apoderado17.setApoNumeroDireccion(99);
            apoderado17.setApoTipoCasa("Casa");
            apoderado17.setIdComuna(1);
            apoderadoService.crearApoderado(apoderado17);

            // Apoderado 18 (Estudiante 18: Hija - Poblete Godoy)
            CrearApoderadoRequest apoderado18 = new CrearApoderadoRequest();
            apoderado18.setApoRut(15000018);
            apoderado18.setApoDvRut('1');
            apoderado18.setApoPrimerNombre("Cristian");
            apoderado18.setApoApellidoPat("Poblete");
            apoderado18.setApoApellidoMat("Fierro");
            apoderado18.setApoEmail("c.pobletef@apoderado.myohiggins.cl");
            apoderado18.setApoPassword("apoderado123");
            apoderado18.setApoTel("+56950000018");
            apoderado18.setApoDireccion("Calle Nueva");
            apoderado18.setApoNumeroDireccion(201);
            apoderado18.setApoTipoCasa("Departamento");
            apoderado18.setIdComuna(4);
            apoderadoService.crearApoderado(apoderado18);

            // Apoderado 19 (Estudiante 19: Tío - Saavedra Vergara)
            CrearApoderadoRequest apoderado19 = new CrearApoderadoRequest();
            apoderado19.setApoRut(15000019);
            apoderado19.setApoDvRut('K');
            apoderado19.setApoPrimerNombre("Patricio");
            apoderado19.setApoApellidoPat("Saavedra");
            apoderado19.setApoApellidoMat("Bustos");
            apoderado19.setApoEmail("p.saavedrab@apoderado.myohiggins.cl");
            apoderado19.setApoPassword("apoderado123");
            apoderado19.setApoTel("+56950000019");
            apoderado19.setApoDireccion("Avenida Costanera");
            apoderado19.setApoNumeroDireccion(665);
            apoderado19.setApoTipoCasa("Casa");
            apoderado19.setIdComuna(8);
            apoderadoService.crearApoderado(apoderado19);

            // Apoderado 20 (Estudiante 20: Hija - Mendoza Cardenas)
            CrearApoderadoRequest apoderado20 = new CrearApoderadoRequest();
            apoderado20.setApoRut(15000020);
            apoderado20.setApoDvRut('3');
            apoderado20.setApoPrimerNombre("Alejandra");
            apoderado20.setApoApellidoPat("Cardenas");
            apoderado20.setApoApellidoMat("Vidal");
            apoderado20.setApoEmail("a.cardenasv@apoderado.myohiggins.cl");
            apoderado20.setApoPassword("apoderado123");
            apoderado20.setApoTel("+56950000020");
            apoderado20.setApoDireccion("Calle Las Violetas");
            apoderado20.setApoNumeroDireccion(42);
            apoderado20.setApoTipoCasa("Casa");
            apoderado20.setIdComuna(11);
            apoderadoService.crearApoderado(apoderado20);

            // Apoderado 21 (Estudiante 21: Hijo - Urrutia Acuña)
            CrearApoderadoRequest apoderado21 = new CrearApoderadoRequest();
            apoderado21.setApoRut(15000021);
            apoderado21.setApoDvRut('1');
            apoderado21.setApoPrimerNombre("Sergio");
            apoderado21.setApoApellidoPat("Urrutia");
            apoderado21.setApoApellidoMat("Rios");
            apoderado21.setApoEmail("s.urrutiar@apoderado.myohiggins.cl");
            apoderado21.setApoPassword("apoderado123");
            apoderado21.setApoTel("+56950000021");
            apoderado21.setApoDireccion("Pasaje Los Pinguinos");
            apoderado21.setApoNumeroDireccion(888);
            apoderado21.setApoTipoCasa("Departamento");
            apoderado21.setIdComuna(13);
            apoderadoService.crearApoderado(apoderado21);

            // Apoderado 22 (Estudiante 22: Abuelo - Garrido Bustos)
            CrearApoderadoRequest apoderado22 = new CrearApoderadoRequest();
            apoderado22.setApoRut(15000022);
            apoderado22.setApoDvRut('K');
            apoderado22.setApoPrimerNombre("Pedro");
            apoderado22.setApoApellidoPat("Garrido");
            apoderado22.setApoApellidoMat("Navarro");
            apoderado22.setApoEmail("p.garridon@apoderado.myohiggins.cl");
            apoderado22.setApoPassword("apoderado123");
            apoderado22.setApoTel("+56950000022");
            apoderado22.setApoDireccion("Avenida Las Torres");
            apoderado22.setApoNumeroDireccion(1011);
            apoderado22.setApoTipoCasa("Casa");
            apoderado22.setIdComuna(15);
            apoderadoService.crearApoderado(apoderado22);

            // Apoderado 23 (Estudiante 23: Hijo - Peña Vera)
            CrearApoderadoRequest apoderado23 = new CrearApoderadoRequest();
            apoderado23.setApoRut(15000023);
            apoderado23.setApoDvRut('8');
            apoderado23.setApoPrimerNombre("Marcela");
            apoderado23.setApoApellidoPat("Vera");
            apoderado23.setApoApellidoMat("Lagos");
            apoderado23.setApoEmail("m.veral@apoderado.myohiggins.cl");
            apoderado23.setApoPassword("apoderado123");
            apoderado23.setApoTel("+56950000023");
            apoderado23.setApoDireccion("Calle El Alba");
            apoderado23.setApoNumeroDireccion(254);
            apoderado23.setApoTipoCasa("Casa");
            apoderado23.setIdComuna(7);
            apoderadoService.crearApoderado(apoderado23);

            // Apoderado 24 (Estudiante 24: Hija - Cortes Osorio)
            CrearApoderadoRequest apoderado24 = new CrearApoderadoRequest();
            apoderado24.setApoRut(15000024);
            apoderado24.setApoDvRut('6');
            apoderado24.setApoPrimerNombre("Hector");
            apoderado24.setApoApellidoPat("Cortes");
            apoderado24.setApoApellidoMat("Vega");
            apoderado24.setApoEmail("h.cortesv@apoderado.myohiggins.cl");
            apoderado24.setApoPassword("apoderado123");
            apoderado24.setApoTel("+56950000024");
            apoderado24.setApoDireccion("Avenida Brasil");
            apoderado24.setApoNumeroDireccion(96);
            apoderado24.setApoTipoCasa("Departamento");
            apoderado24.setIdComuna(3);
            apoderadoService.crearApoderado(apoderado24);

            // Apoderado 25 (Estudiante 25: Nieta - Donoso Perez)
            CrearApoderadoRequest apoderado25 = new CrearApoderadoRequest();
            apoderado25.setApoRut(15000025);
            apoderado25.setApoDvRut('4');
            apoderado25.setApoPrimerNombre("Margarita");
            apoderado25.setApoApellidoPat("Donoso");
            apoderado25.setApoApellidoMat("Gutierrez");
            apoderado25.setApoEmail("m.donosog@apoderado.myohiggins.cl");
            apoderado25.setApoPassword("apoderado123");
            apoderado25.setApoTel("+56950000025");
            apoderado25.setApoDireccion("Pasaje Las Lilas");
            apoderado25.setApoNumeroDireccion(14);
            apoderado25.setApoTipoCasa("Casa");
            apoderado25.setIdComuna(5);
            apoderadoService.crearApoderado(apoderado25);

            // Apoderado 26 (Estudiante 26: Hija - Moya Gutierrez)
            CrearApoderadoRequest apoderado26 = new CrearApoderadoRequest();
            apoderado26.setApoRut(15000026);
            apoderado26.setApoDvRut('2');
            apoderado26.setApoPrimerNombre("Ana");
            apoderado26.setApoApellidoPat("Gutierrez");
            apoderado26.setApoApellidoMat("Rojas");
            apoderado26.setApoEmail("a.gutierrezr@apoderado.myohiggins.cl");
            apoderado26.setApoPassword("apoderado123");
            apoderado26.setApoTel("+56950000026");
            apoderado26.setApoDireccion("Calle Los Copihues");
            apoderado26.setApoNumeroDireccion(785);
            apoderado26.setApoTipoCasa("Casa");
            apoderado26.setIdComuna(9);
            apoderadoService.crearApoderado(apoderado26);

            // Apoderado 27 (Estudiante 27: Hijo - Rios Lara)
            CrearApoderadoRequest apoderado27 = new CrearApoderadoRequest();
            apoderado27.setApoRut(15000027);
            apoderado27.setApoDvRut('0');
            apoderado27.setApoPrimerNombre("Francisco");
            apoderado27.setApoApellidoPat("Rios");
            apoderado27.setApoApellidoMat("Paredes");
            apoderado27.setApoEmail("f.riosp@apoderado.myohiggins.cl");
            apoderado27.setApoPassword("apoderado123");
            apoderado27.setApoTel("+56950000027");
            apoderado27.setApoDireccion("Avenida Las Perdices");
            apoderado27.setApoNumeroDireccion(109);
            apoderado27.setApoTipoCasa("Departamento");
            apoderado27.setIdComuna(12);
            apoderadoService.crearApoderado(apoderado27);

            // Apoderado 28 (Estudiante 28: Hermano - Salazar Munoz)
            CrearApoderadoRequest apoderado28 = new CrearApoderadoRequest();
            apoderado28.setApoRut(15000028);
            apoderado28.setApoDvRut('9');
            apoderado28.setApoPrimerNombre("Benjamin");
            apoderado28.setApoApellidoPat("Salazar");
            apoderado28.setApoApellidoMat("Munoz");
            apoderado28.setApoEmail("b.salazarm@apoderado.myohiggins.cl");
            apoderado28.setApoPassword("apoderado123");
            apoderado28.setApoTel("+56950000028");
            apoderado28.setApoDireccion("Pasaje Los Canelos");
            apoderado28.setApoNumeroDireccion(332);
            apoderado28.setApoTipoCasa("Casa");
            apoderado28.setIdComuna(6);
            apoderadoService.crearApoderado(apoderado28);

            // Apoderado 29 (Estudiante 29: Hijo - Navarrete Arias)
            CrearApoderadoRequest apoderado29 = new CrearApoderadoRequest();
            apoderado29.setApoRut(15000029);
            apoderado29.setApoDvRut('7');
            apoderado29.setApoPrimerNombre("Paula");
            apoderado29.setApoApellidoPat("Arias");
            apoderado29.setApoApellidoMat("Gomez");
            apoderado29.setApoEmail("p.ariasg@apoderado.myohiggins.cl");
            apoderado29.setApoPassword("apoderado123");
            apoderado29.setApoTel("+56950000029");
            apoderado29.setApoDireccion("Calle O'Higgins");
            apoderado29.setApoNumeroDireccion(881);
            apoderado29.setApoTipoCasa("Casa");
            apoderado29.setIdComuna(2);
            apoderadoService.crearApoderado(apoderado29);

            // Apoderado 30 (Estudiante 30: Hija - Reyes Fierro)
            CrearApoderadoRequest apoderado30 = new CrearApoderadoRequest();
            apoderado30.setApoRut(15000030);
            apoderado30.setApoDvRut('0');
            apoderado30.setApoPrimerNombre("Marcelo");
            apoderado30.setApoApellidoPat("Reyes");
            apoderado30.setApoApellidoMat("Lillo");
            apoderado30.setApoEmail("m.reyesl@apoderado.myohiggins.cl");
            apoderado30.setApoPassword("apoderado123");
            apoderado30.setApoTel("+56950000030");
            apoderado30.setApoDireccion("Avenida España");
            apoderado30.setApoNumeroDireccion(65);
            apoderado30.setApoTipoCasa("Departamento");
            apoderado30.setIdComuna(14);
            apoderadoService.crearApoderado(apoderado30);

            // Apoderado 31 (Estudiante 31: Tía - Vidal Correa)
            CrearApoderadoRequest apoderado31 = new CrearApoderadoRequest();
            apoderado31.setApoRut(15000031);
            apoderado31.setApoDvRut('9');
            apoderado31.setApoPrimerNombre("Veronica");
            apoderado31.setApoApellidoPat("Correa");
            apoderado31.setApoApellidoMat("Castillo");
            apoderado31.setApoEmail("v.correac@apoderado.myohiggins.cl");
            apoderado31.setApoPassword("apoderado123");
            apoderado31.setApoTel("+56950000031");
            apoderado31.setApoDireccion("Pasaje Las Encinas");
            apoderado31.setApoNumeroDireccion(105);
            apoderado31.setApoTipoCasa("Casa");
            apoderado31.setIdComuna(4);
            apoderadoService.crearApoderado(apoderado31);

            // Apoderado 32 (Estudiante 32: Hija - Zuniga Paredes)
            CrearApoderadoRequest apoderado32 = new CrearApoderadoRequest();
            apoderado32.setApoRut(15000032);
            apoderado32.setApoDvRut('7');
            apoderado32.setApoPrimerNombre("Silvia");
            apoderado32.setApoApellidoPat("Paredes");
            apoderado32.setApoApellidoMat("Herrera");
            apoderado32.setApoEmail("s.paredesh@apoderado.myohiggins.cl");
            apoderado32.setApoPassword("apoderado123");
            apoderado32.setApoTel("+56950000032");
            apoderado32.setApoDireccion("Calle Los Robles");
            apoderado32.setApoNumeroDireccion(330);
            apoderado32.setApoTipoCasa("Casa");
            apoderado32.setIdComuna(8);
            apoderadoService.crearApoderado(apoderado32);

            // Apoderado 33 (Estudiante 33: Hijo - Aedo Villegas)
            CrearApoderadoRequest apoderado33 = new CrearApoderadoRequest();
            apoderado33.setApoRut(15000033);
            apoderado33.setApoDvRut('5');
            apoderado33.setApoPrimerNombre("Mauricio");
            apoderado33.setApoApellidoPat("Aedo");
            apoderado33.setApoApellidoMat("Osorio");
            apoderado33.setApoEmail("m.aedoo@apoderado.myohiggins.cl");
            apoderado33.setApoPassword("apoderado123");
            apoderado33.setApoTel("+56950000033");
            apoderado33.setApoDireccion("Avenida Caupolican");
            apoderado33.setApoNumeroDireccion(710);
            apoderado33.setApoTipoCasa("Departamento");
            apoderado33.setIdComuna(1);
            apoderadoService.crearApoderado(apoderado33);

            // Apoderado 34 (Estudiante 34: Primo - Lagos Ortiz)
            CrearApoderadoRequest apoderado34 = new CrearApoderadoRequest();
            apoderado34.setApoRut(15000034);
            apoderado34.setApoDvRut('3');
            apoderado34.setApoPrimerNombre("Fernando");
            apoderado34.setApoApellidoPat("Lagos");
            apoderado34.setApoApellidoMat("Moya");
            apoderado34.setApoEmail("f.lagosm@apoderado.myohiggins.cl");
            apoderado34.setApoPassword("apoderado123");
            apoderado34.setApoTel("+56950000034");
            apoderado34.setApoDireccion("Pasaje Los Notros");
            apoderado34.setApoNumeroDireccion(18);
            apoderado34.setApoTipoCasa("Casa");
            apoderado34.setIdComuna(10);
            apoderadoService.crearApoderado(apoderado34);

            // Apoderado 35 (Estudiante 35: Hijo - Castro Fuenzalida)
            CrearApoderadoRequest apoderado35 = new CrearApoderadoRequest();
            apoderado35.setApoRut(15000035);
            apoderado35.setApoDvRut('1');
            apoderado35.setApoPrimerNombre("Ximena");
            apoderado35.setApoApellidoPat("Fuenzalida");
            apoderado35.setApoApellidoMat("Urrutia");
            apoderado35.setApoEmail("x.fuenzalidau@apoderado.myohiggins.cl");
            apoderado35.setApoPassword("apoderado123");
            apoderado35.setApoTel("+56950000035");
            apoderado35.setApoDireccion("Calle San Martin");
            apoderado35.setApoNumeroDireccion(555);
            apoderado35.setApoTipoCasa("Casa");
            apoderado35.setIdComuna(12);
            apoderadoService.crearApoderado(apoderado35);

            // Apoderado 36 (Estudiante 36: Hija - Escobar Jara)
            CrearApoderadoRequest apoderado36 = new CrearApoderadoRequest();
            apoderado36.setApoRut(15000036);
            apoderado36.setApoDvRut('K');
            apoderado36.setApoPrimerNombre("Victor");
            apoderado36.setApoApellidoPat("Escobar");
            apoderado36.setApoApellidoMat("Acuña");
            apoderado36.setApoEmail("v.escobara@apoderado.myohiggins.cl");
            apoderado36.setApoPassword("apoderado123");
            apoderado36.setApoTel("+56950000036");
            apoderado36.setApoDireccion("Avenida Pedro de Valdivia");
            apoderado36.setApoNumeroDireccion(912);
            apoderado36.setApoTipoCasa("Departamento");
            apoderado36.setIdComuna(3);
            apoderadoService.crearApoderado(apoderado36);

            // Apoderado 37 (Estudiante 37: Hermana - Orellana Martinez)
            CrearApoderadoRequest apoderado37 = new CrearApoderadoRequest();
            apoderado37.setApoRut(15000037);
            apoderado37.setApoDvRut('8');
            apoderado37.setApoPrimerNombre("Bárbara");
            apoderado37.setApoApellidoPat("Orellana");
            apoderado37.setApoApellidoMat("Martinez");
            apoderado37.setApoEmail("b.orellanam@apoderado.myohiggins.cl");
            apoderado37.setApoPassword("apoderado123");
            apoderado37.setApoTel("+56950000037");
            apoderado37.setApoDireccion("Pasaje Los Abedules");
            apoderado37.setApoNumeroDireccion(77);
            apoderado37.setApoTipoCasa("Casa");
            apoderado37.setIdComuna(15);
            apoderadoService.crearApoderado(apoderado37);

            // Apoderado 38 (Estudiante 38: Hija - Guzman Carvajal)
            CrearApoderadoRequest apoderado38 = new CrearApoderadoRequest();
            apoderado38.setApoRut(15000038);
            apoderado38.setApoDvRut('6');
            apoderado38.setApoPrimerNombre("Tatiana");
            apoderado38.setApoApellidoPat("Carvajal");
            apoderado38.setApoApellidoMat("Pinto");
            apoderado38.setApoEmail("t.carvajalp@apoderado.myohiggins.cl");
            apoderado38.setApoPassword("apoderado123");
            apoderado38.setApoTel("+56950000038");
            apoderado38.setApoDireccion("Calle Los Maitenes");
            apoderado38.setApoNumeroDireccion(620);
            apoderado38.setApoTipoCasa("Casa");
            apoderado38.setIdComuna(5);
            apoderadoService.crearApoderado(apoderado38);

            // Apoderado 39 (Estudiante 39: Abuela - Pino Valdes)
            CrearApoderadoRequest apoderado39 = new CrearApoderadoRequest();
            apoderado39.setApoRut(15000039);
            apoderado39.setApoDvRut('4');
            apoderado39.setApoPrimerNombre("Elsa");
            apoderado39.setApoApellidoPat("Valdes");
            apoderado39.setApoApellidoMat("Contreras");
            apoderado39.setApoEmail("e.valdesc@apoderado.myohiggins.cl");
            apoderado39.setApoPassword("apoderado123");
            apoderado39.setApoTel("+56950000039");
            apoderado39.setApoDireccion("Avenida Bicentenario");
            apoderado39.setApoNumeroDireccion(112);
            apoderado39.setApoTipoCasa("Departamento");
            apoderado39.setIdComuna(9);
            apoderadoService.crearApoderado(apoderado39);

            // Apoderado 40 (Estudiante 40: Tutor Institucional - Santana Roman)
            CrearApoderadoRequest apoderado40 = new CrearApoderadoRequest();
            apoderado40.setApoRut(15000040);
            apoderado40.setApoDvRut('8');
            apoderado40.setApoPrimerNombre("Roberto");
            apoderado40.setApoApellidoPat("Santana");
            apoderado40.setApoApellidoMat("Leal");
            apoderado40.setApoEmail("r.santanal@apoderado.myohiggins.cl");
            apoderado40.setApoPassword("apoderado123");
            apoderado40.setApoTel("+56950000040");
            apoderado40.setApoDireccion("Pasaje La Amistad");
            apoderado40.setApoNumeroDireccion(49);
            apoderado40.setApoTipoCasa("Casa");
            apoderado40.setIdComuna(13);
            apoderadoService.crearApoderado(apoderado40);

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
