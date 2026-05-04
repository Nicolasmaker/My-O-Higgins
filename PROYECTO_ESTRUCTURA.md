# Estructura del Proyecto Spring Boot

## 📁 Descripción de Carpetas

```
proyecto/
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   ├── Application.java              # Clase principal de la aplicación
│   │   │   ├── controller/                    # Controladores REST/Web
│   │   │   │   └── HomeController.java
│   │   │   ├── service/                       # Lógica de negocio
│   │   │   │   └── UserService.java
│   │   │   ├── model/                         # Entidades y modelos JPA
│   │   │   │   └── User.java
│   │   │   ├── repository/                    # Acceso a datos (Spring Data JPA)
│   │   │   │   └── UserRepository.java
│   │   │   └── config/                        # Configuraciones de la aplicación
│   │   │
│   │   └── resources/
│   │       ├── application.properties         # Configuración principal
│   │       ├── templates/                     # Plantillas HTML (Thymeleaf)
│   │       │   ├── index.html
│   │       │   └── about.html
│   │       └── static/                        # Archivos estáticos
│   │           ├── css/
│   │           │   └── style.css
│   │           ├── js/
│   │           │   └── main.js
│   │           └── images/
│   │
│   └── test/
│       └── java/com/example/
│           └── ApplicationTests.java
│
├── pom.xml                                    # Configuración Maven
├── .gitignore                                 # Archivos a ignorar en Git
└── README.md                                  # Este archivo
```

## 🚀 Guía Rápida

### 1. Instalar Dependencias
```bash
mvn clean install
```

### 2. Ejecutar la Aplicación
```bash
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

### 3. Navegar
- **Inicio**: `http://localhost:8080/`
- **Acerca de**: `http://localhost:8080/about`

## 📚 Dependencias Principales

- **Spring Boot Web**: Para crear aplicaciones web
- **Spring Data JPA**: Para acceso a base de datos
- **Thymeleaf**: Motor de plantillas HTML
- **H2 Database**: Base de datos en memoria (desarrollo)
- **Lombok**: Reducir código repetitivo
- **Spring Boot DevTools**: Reinicio automático en desarrollo

## 🔧 Configuración

Los archivos de configuración se encuentran en `src/main/resources/application.properties`:

- **Puerto**: 8080
- **Base de datos**: H2 (en memoria)
- **Hibernate DDL**: update (crea/actualiza tablas automáticamente)

## 📝 Notas

- Puedes cambiar `com.example` por tu propio package name
- Para producción, configura una base de datos real (MySQL, PostgreSQL, etc.)
- Los templates HTML están en `templates/` y usan Thymeleaf
- Los archivos CSS y JS están en `static/`

## 🎯 Próximos Pasos

1. Adapta el nombre del package según tus necesidades
2. Crea nuevos controladores, servicios y modelos
3. Configura la base de datos real
4. Añade seguridad con Spring Security (opcional)
5. Implementa validaciones de datos
