package com.cahuinlabs.autenticacion.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cahuinlabs.autenticacion.models.entities.usuarios.Directivo;
import com.cahuinlabs.autenticacion.models.entities.usuarios.Docente;
import com.cahuinlabs.autenticacion.models.entities.usuarios.Funcionario;
import com.cahuinlabs.autenticacion.models.entities.usuarios.Inspector;
import com.cahuinlabs.autenticacion.models.entities.usuarios.Rol;
import com.cahuinlabs.autenticacion.models.entities.usuarios.Usuario;
import com.cahuinlabs.autenticacion.models.request.funcionarios.directivos.ActualizarDirectivoRequest;
import com.cahuinlabs.autenticacion.models.request.funcionarios.directivos.CrearDirectivoRequest;
import com.cahuinlabs.autenticacion.models.request.funcionarios.docente.ActualizarDocenteRequest;
import com.cahuinlabs.autenticacion.models.request.funcionarios.docente.CrearDocenteRequest;
import com.cahuinlabs.autenticacion.models.request.funcionarios.inspector.ActualizarInspectorRequest;
import com.cahuinlabs.autenticacion.models.request.funcionarios.inspector.CrearInspectorRequest;
import com.cahuinlabs.autenticacion.repository.FuncionarioRepository;
import com.cahuinlabs.autenticacion.repository.RolRepository;
import com.cahuinlabs.autenticacion.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
public class FuncionarioService {

 //Variables de los repositories y el servicio de direccion
   private final FuncionarioRepository funcionarioRepository;
   private final UsuarioRepository usuarioRepository;
   private final DireccionService direccionService;
   private final RolRepository rolRepository;
   private final PasswordEncoder passwordEncoder;

 //Inyeccion de los repositories y el service a traves del constructor
   public FuncionarioService(UsuarioRepository usuarioRepository, DireccionService direccionService, RolRepository rolRepository, FuncionarioRepository funcionarioRepository, PasswordEncoder passwordEncoder){
      this.usuarioRepository = usuarioRepository;
      this.direccionService = direccionService;
      this.rolRepository = rolRepository;
      this.funcionarioRepository = funcionarioRepository;
      this.passwordEncoder = passwordEncoder;
   }

   public Funcionario buscarFuncionarioPorRut(Integer rut){
      Usuario usuario = usuarioRepository.findById(rut)
         .orElseThrow(() -> new RuntimeException("No se encontro un funcionario con el rut: " + rut));

      if (usuario instanceof Funcionario){
         return (Funcionario) usuario;
      } else {
         throw new RuntimeException("El usuario con rut: " + rut + " no es un funcionario.");
      }
   }

   public List<Funcionario> listarFuncionarios(){
      return funcionarioRepository.findAll();   
    }

 /*====================================================
                   METODOS DE CREACION
   ====================================================*/

   @Transactional
   public Docente crearDocente(CrearDocenteRequest requestDocente){
        
     //Verificar si el docente ya existe por su rut
      validarExistenciaFuncionario(requestDocente.getDcteRut(), "Docente");

      Docente nuevoDocente = new Docente();

     //Datos de la entidad usuario
      llenarDatosFuncionario(
        nuevoDocente, 
        requestDocente.getDcteRut(), 
        requestDocente.getDcteDvRut(), 
        requestDocente.getDctePrimerNombre(), 
        requestDocente.getDcteSegundoNombre(), 
        requestDocente.getDcteApellidoPat(), 
        requestDocente.getDcteApellidoMat(), 
        requestDocente.getDcteEmail(), 
        requestDocente.getDcteTel(), 
        requestDocente.getDctePassword(), 
        requestDocente.getDcteTitulo() //Dato de la entidad Funcionario
      );

     //Dato de la entidad docente
      nuevoDocente.setDcteEspecialidad(requestDocente.getDcteEspecialidad());

      Docente docenteGuardado = usuarioRepository.save(nuevoDocente); //Guarda al docente

     //Asignar rol
      asignarRolFuncionario(docenteGuardado, "ROLE_DOCENTE");

     //Gestionar la direccion
      direccionService.guardarDireccionUsuario(docenteGuardado.getUsuRut(), requestDocente.getDcteDireccion(), requestDocente.getDcteNumeroDireccion(), 
                                                 requestDocente.getDcteTipoCasa(), requestDocente.getIdComuna());
      return docenteGuardado;
    }

   @Transactional
   public Inspector crearInspector(CrearInspectorRequest requestInspector){

    //Verificar si el inspector ya existe por su rut
      validarExistenciaFuncionario(requestInspector.getInsRut(), "Inspector");
    
      Inspector nuevoInspector = new Inspector();
    
    //Datos de la entidad usuario
      llenarDatosFuncionario(
         nuevoInspector, 
         requestInspector.getInsRut(), 
         requestInspector.getInsDvRut(), 
         requestInspector.getInsPrimerNombre(), 
         requestInspector.getInsSegundoNombre(), 
         requestInspector.getInsApellidoPat(), 
         requestInspector.getInsApellidoMat(), 
         requestInspector.getInsEmail(), 
         requestInspector.getInsTel(), 
         requestInspector.getInsPassword(), 
         requestInspector.getInsTitulo() //Dato de la entidad Funcionario
      );
    
    //Dato de la entidad inspector
      nuevoInspector.setInsNivel(requestInspector.getInsNivel());
    
      Inspector inspectorGuardado = usuarioRepository.save(nuevoInspector); //Guarda al inspector
    
    //Asignar rol
      asignarRolFuncionario(inspectorGuardado, "ROLE_INSPECTOR");
    
    //Gestionar la direccion
      direccionService.guardarDireccionUsuario(inspectorGuardado.getUsuRut(), requestInspector.getInsDireccion(), requestInspector.getInsNumeroDireccion(), 
                                                requestInspector.getInsTipoCasa(), requestInspector.getIdComuna());
      return inspectorGuardado;
   }

   @Transactional
   public Directivo crearDirectivo(CrearDirectivoRequest requestDirectivo){

    //Verificar si el directivo ya existe por su rut
      validarExistenciaFuncionario(requestDirectivo.getDirRut(), "Directivo");
    
      Directivo nuevoDirectivo = new Directivo();
    
    //Datos de la entidad usuario
      llenarDatosFuncionario(
         nuevoDirectivo, 
         requestDirectivo.getDirRut(), 
         requestDirectivo.getDirDvRut(), 
         requestDirectivo.getDirPrimerNombre(), 
         requestDirectivo.getDirSegundoNombre(), 
         requestDirectivo.getDirApellidoPat(), 
         requestDirectivo.getDirApellidoMat(), 
         requestDirectivo.getDirEmail(), 
         requestDirectivo.getDirTel(), 
         requestDirectivo.getDirPassword(), 
         requestDirectivo.getDirTitulo() //Dato de la entidad Funcionario
      );
    
      //Dato de la entidad directivo
      nuevoDirectivo.setDirCargo(requestDirectivo.getDirCargo());

      Directivo directivoGuardado = usuarioRepository.save(nuevoDirectivo); //Guarda al directivo
    
    //Asignar rol
      asignarRolFuncionario(directivoGuardado, "ROLE_DIRECTIVO");
    
    //Gestionar la direccion
      direccionService.guardarDireccionUsuario(directivoGuardado.getUsuRut(), requestDirectivo.getDirDireccion(), requestDirectivo.getDirNumeroDireccion(), 
                                                requestDirectivo.getDirTipoCasa(), requestDirectivo.getIdComuna());
      return directivoGuardado;
   }

 /*====================================================
                METODOS DE ACTUALIZACION
   ====================================================*/

   @Transactional
   public Docente actualizarDocente(Integer rut, ActualizarDocenteRequest requestActDocente){

    //Busqueda del docente existente
      Docente docenteExistente = (Docente) obtenerFuncionarioPorRut(rut, Docente.class);

      actualizarDatosFuncionario(docenteExistente, requestActDocente.getDctePrimerNombre(), requestActDocente.getDcteSegundoNombre(), 
                                 requestActDocente.getDcteApellidoPat(), requestActDocente.getDcteApellidoMat(), requestActDocente.getDcteEmail(), 
                                 requestActDocente.getDcteTel(), requestActDocente.getDcteTitulo());

      if(requestActDocente.getDcteEspecialidad() != null){
         docenteExistente.setDcteEspecialidad(requestActDocente.getDcteEspecialidad());
      }

      Docente docenteActualizado = usuarioRepository.save(docenteExistente);

      direccionService.actualizarDireccionUsuario(rut, requestActDocente.getDcteDireccion(), requestActDocente.getDcteNumeroDireccion(), 
                                                  requestActDocente.getDcteTipoCasa(), requestActDocente.getIdComuna());

      return docenteActualizado;
   }

   @Transactional
   public Inspector actualizarInspector(Integer rut, ActualizarInspectorRequest requestActInspector){

    //Busqueda del inspector existente
      Inspector inspectorExistente = (Inspector) obtenerFuncionarioPorRut(rut, Inspector.class);

      actualizarDatosFuncionario(inspectorExistente, requestActInspector.getInsPrimerNombre(), requestActInspector.getInsSegundoNombre(), 
                                 requestActInspector.getInsApellidoPat(), requestActInspector.getInsApellidoMat(), requestActInspector.getInsEmail(), 
                                 requestActInspector.getInsTel(), requestActInspector.getInsTitulo());

      if(requestActInspector.getInsNivel() != null){
         inspectorExistente.setInsNivel(requestActInspector.getInsNivel());
      }

      Inspector inspectorActualizado = usuarioRepository.save(inspectorExistente);

      direccionService.actualizarDireccionUsuario(rut, requestActInspector.getInsDireccion(), requestActInspector.getInsNumeroDireccion(), 
                                                  requestActInspector.getInsTipoCasa(), requestActInspector.getIdComuna());

      return inspectorActualizado;
   }

   @Transactional
   public Funcionario actualizarDirectivo(Integer rut, ActualizarDirectivoRequest requestActDirectivo){

    //Busqueda del directivo existente
      Directivo directivoExistente = (Directivo) obtenerFuncionarioPorRut(rut, Directivo.class);

      actualizarDatosFuncionario(directivoExistente, requestActDirectivo.getDirPrimerNombre(), requestActDirectivo.getDirSegundoNombre(), 
                                 requestActDirectivo.getDirApellidoPat(), requestActDirectivo.getDirApellidoMat(), requestActDirectivo.getDirEmail(), 
                                 requestActDirectivo.getDirTel(), requestActDirectivo.getDirTitulo());

      if(requestActDirectivo.getDirCargo() != null){
         directivoExistente.setDirCargo(requestActDirectivo.getDirCargo());
      }

      Funcionario directivoActualizado = usuarioRepository.save(directivoExistente);

      direccionService.actualizarDireccionUsuario(rut, requestActDirectivo.getDirDireccion(), requestActDirectivo.getDirNumeroDireccion(), 
                                                  requestActDirectivo.getDirTipoCasa(), requestActDirectivo.getIdComuna());

      return directivoActualizado;
   }
   
 //============================================================================
 //                METODOS PRIVADOS GENERALES PARA FUNCIONARIOS
 //============================================================================

 //Validar existencia de un funcionario
   private void validarExistenciaFuncionario(Integer rut, String tipoFuncionario){
      if(usuarioRepository.existsById(rut)){
         throw new RuntimeException("Ya existe un " + tipoFuncionario + " asociado al rut: " + rut);
      }
   }

 //Asignar un rol
   private void asignarRolFuncionario(Usuario usuario, String nombreRol){
      Rol rol = new Rol();
      rol.setRolNombre(nombreRol);
      rol.setUsuario(usuario);
      rolRepository.save(rol);      
   }

 //Llenar datos comunes de los funcionarios
   private void llenarDatosFuncionario(Funcionario funcionario, Integer rut, Character dvRut, String pNombre, String sNombre, 
                                       String apePat, String apeMat, String email, String tel, String password, String titulo){
      funcionario.setUsuRut(rut);
      funcionario.setUsuDvRut(dvRut);
      funcionario.setUsuPNombre(pNombre);
      funcionario.setUsuSNombre(sNombre);
      funcionario.setUsuApePat(apePat);
      funcionario.setUsuApeMat(apeMat);
      funcionario.setUsuEmail(email);
      funcionario.setUsuTel(tel);

      String passwordEncriptada = passwordEncoder.encode(password); //Encriptacion de la contraseña
      funcionario.setUsuPassword(passwordEncriptada);

      funcionario.setUsuEstadoActividad(true);
      funcionario.setFunTitulo(titulo);
   }

 //Actualizar datos comunes de los funcionarios
   private void actualizarDatosFuncionario(Funcionario funcionario, String pNombre, String sNombre, String apePat, 
                                          String apeMat, String email, String tel, String titulo){
      if(pNombre != null) funcionario.setUsuPNombre(pNombre);
      if(sNombre != null) funcionario.setUsuSNombre(sNombre);
      if(apePat  != null) funcionario.setUsuApePat(apePat);
      if(apeMat  != null) funcionario.setUsuApeMat(apeMat);
      if(email   != null) funcionario.setUsuEmail(email);
      if(tel     != null) funcionario.setUsuTel(tel);
      if(titulo  != null) funcionario.setFunTitulo(titulo);
   }

 //Consulta de funcionario general
   private Funcionario obtenerFuncionarioPorRut(Integer rut, Class<?> tipoFuncionario){

      String tipoNombre = tipoFuncionario.getSimpleName();

      Usuario usuario = usuarioRepository.findById(rut)
         .orElseThrow(() -> new RuntimeException("No se encontro un" + tipoNombre + " con el rut: " + rut));

      if (!tipoFuncionario.isInstance(usuario)){
         throw new RuntimeException("El Funcionario con rut: " + rut + " no es del tipo esperado.");
      }

      return (Funcionario) usuario;
   }
}
