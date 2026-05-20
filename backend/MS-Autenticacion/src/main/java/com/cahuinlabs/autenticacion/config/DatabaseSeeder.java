package com.cahuinlabs.autenticacion.config;

import com.cahuinlabs.autenticacion.models.request.funcionarios.docente.CrearDocenteRequest;
import com.cahuinlabs.autenticacion.repository.UsuarioRepository;
import com.cahuinlabs.autenticacion.service.FuncionarioService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final FuncionarioService funcionarioService;

    public DatabaseSeeder(UsuarioRepository usuarioRepository, FuncionarioService funcionarioService) {
        this.usuarioRepository = usuarioRepository;
        this.funcionarioService = funcionarioService;
    }

    @Override
    public void run(String... args) throws Exception {
        //Si la base de datos está vacía, creamos el primer usuario administrador/docente
        if (usuarioRepository.count() == 0) {
            CrearDocenteRequest admin = new CrearDocenteRequest();
            admin.setDcteRut(11111111);
            admin.setDcteDvRut('1');
            admin.setDctePrimerNombre("Admin");
            admin.setDcteApellidoPat("Sistema");
            admin.setDcteApellidoMat("Fullstackeados");
            admin.setDcteEmail("admin@colegio.cl");
            admin.setDctePassword("admin123"); 
            admin.setDcteTel("+56912345678");
            admin.setDcteTitulo("Ingeniero");
            admin.setDcteEspecialidad("Fullstackear");
            admin.setDcteDireccion("Avenida Principal");
            admin.setDcteNumeroDireccion(123);
            admin.setDcteTipoCasa("drHouse");
            admin.setIdComuna(1); 
            funcionarioService.crearDocente(admin);
            System.out.println("====== USUARIO INICIAL CREADO EN LA BASE DE DATOS ======");
            System.out.println("Email: admin@colegio.cl | Password: admin123");
            System.out.println("========================================================");
        }
    }
}
