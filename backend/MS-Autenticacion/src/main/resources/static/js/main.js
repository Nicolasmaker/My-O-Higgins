// Script principal
console.log('Aplicación Spring Boot cargada correctamente');

document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM cargado completamente');
    
    // Aquí puedes agregar lógica JavaScript adicional
    // Ejemplo: manejo de eventos, peticiones AJAX, etc.
    
    const navLinks = document.querySelectorAll('.nav-menu a');
    navLinks.forEach(link => {
        link.addEventListener('click', function() {
            console.log('Navegando a:', this.href);
        });
    });
});
