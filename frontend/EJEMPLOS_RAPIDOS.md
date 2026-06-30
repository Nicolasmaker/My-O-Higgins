# 🚀 Ejemplos Rápidos de Uso - My O'Higgins Landing Page

## Cambiar Datos de Accesos Rápidos

**Archivo:** `src/pages/Home/Home.jsx`

```jsx
// Busca este array en Home.jsx:
const quickAccess = [
  {
    id: 1,
    icon: '📅',
    title: 'Calendario Escolar',
    description: 'Consulta fechas importantes, feriados y períodos académicos del año.',
    link: '#',
  },
  {
    id: 2,
    icon: '📚',
    title: 'Portal Académico',
    description: 'Acceso a calificaciones, horarios y material educativo de tus cursos.',
    link: '#',
  },
  {
    id: 3,
    icon: '📜',
    title: 'Certificados',
    description: 'Descarga certificados de conducta, asistencia y otros documentos.',
    link: '#',
  },
];

// Para agregar una 4ta tarjeta:
{
  id: 4,
  icon: '🎓',
  title: 'Mi Nombre Nuevo',
  description: 'Nueva descripción aquí.',
  link: '#',
}
```

---

## Cambiar Datos de Noticias

**Archivo:** `src/pages/Home/Home.jsx`

```jsx
const news = [
  {
    id: 1,
    image: 'https://images.unsplash.com/...', // Cambia la URL
    category: 'Académico',                     // Categoría (ej: Deportes)
    title: 'Título de la Noticia',             // Tu título
    excerpt: 'Extracto corto de la noticia...', // Descripción corta
    link: '#',
  },
  // Agrega más...
];
```

**Para usar imágenes locales:**
```jsx
image: '/images/mi-imagen.jpg' // Pon las imágenes en public/images/
```

---

## Agregar Nuevos Colores a la Paleta

**Archivo:** `src/styles/_variables.css`

```css
:root {
  /* Colores Nuevos */
  --color-success: #4CAF50;
  --color-warning: #FFC107;
  --color-error: #F44336;
  --color-info: #2196F3;
}
```

**Luego úsalos en cualquier CSS Module:**
```css
.element {
  background-color: var(--color-success);
}
```

---

## Crear un Nuevo Botón

**Archivo:** `src/pages/Home/Home.jsx` (o cualquier componente)

```jsx
import Button from '../../components/UI/Button';

// Variante Primary (Relleno Rojo)
<Button variant="primary" onClick={() => alert('Hola!')}>
  Haz clic aquí
</Button>

// Variante Outline (Borde Rojo)
<Button variant="outline">
  Botón de Borde
</Button>

// Variante Secondary (Gris)
<Button variant="secondary">
  Botón Secundario
</Button>

// Con estado disabled
<Button variant="primary" disabled>
  Botón Deshabilitado
</Button>

// Tipo submit (para formularios)
<Button type="submit" variant="primary">
  Enviar Formulario
</Button>
```

---

## Cambiar Colores de un Componente

**Ejemplo: Cambiar el color primario del sitio**

**Archivo:** `src/styles/_variables.css`

```css
:root {
  /* Cambiar de rojo a azul */
  --color-primary: #0066CC; /* Era: #6B2323 */
  --color-primary-dark: #0052A3;
  --color-primary-light: #3D99FF;
}
```

Se actualizará automáticamente en:
- Botones
- Navbar (logo y links activos)
- Títulos
- Footer (contacto)

---

## Cambiar Fondos de Secciones

**Archivo:** `src/styles/_variables.css`

```css
:root {
  /* Cambiar fondo Hero de verde a otro color */
  --color-bg-hero: #F0E5FF; /* Ahora es púrpura pastel */
  
  /* Cambiar fondo de Accesos Rápidos */
  --color-bg-white: #F5F5F5; /* Ahora es gris claro */
}
```

---

## Modificar Espaciado Global

**Archivo:** `src/styles/_variables.css`

```css
:root {
  /* Espaciado más grande */
  --spacing-md: 20px;  /* Era: 16px */
  --spacing-lg: 32px;  /* Era: 24px */
  --spacing-xl: 48px;  /* Era: 32px */
}
```

Esto afectará:
- Padding de secciones
- Gaps entre elementos
- Padding de tarjetas

---

## Cambiar Tipografía

**Archivo:** `src/styles/_variables.css`

```css
:root {
  /* Cambiar tamaño base de letra */
  --font-size-base: 18px; /* Era: 16px */
  
  /* Cambiar tamaño de título principal */
  --font-size-4xl: 56px; /* Era: 48px */
}
```

---

## Agregar una Nueva Sección en Home

**Archivo:** `src/pages/Home/Home.jsx`

```jsx
export default function Home() {
  const quickAccess = [...]
  const news = [...]

  return (
    <div className={styles.page}>
      <Navbar />
      <section className={styles.heroSection}>...</section>
      <section className={styles.quickAccessSection}>...</section>
      
      {/* NUEVA SECCIÓN AQUÍ */}
      <section className={styles.newSection}>
        <div className={styles.container}>
          <h2>Nueva Sección</h2>
          <p>Contenido aquí...</p>
        </div>
      </section>
      
      <section className={styles.newsSection}>...</section>
      <Footer />
    </div>
  );
}
```

**Agrega estilos en Home.module.css:**

```css
.newSection {
  background-color: var(--color-bg-white);
  padding: var(--spacing-3xl) 0;
}
```

---

## Cambiar Enlaces del Navbar

**Archivo:** `src/components/Navbar/Navbar.jsx`

```jsx
const navLinks = [
  { id: 'inicio', label: 'Inicio', href: '/' },
  { id: 'admision', label: 'Admisión', href: '/admision' },
  { id: 'nosotros', label: 'Nosotros', href: '/nosotros' },
  { id: 'contacto', label: 'Contacto', href: '/contacto' },
  // Agregar nuevo enlace:
  { id: 'estudiantes', label: 'Para Estudiantes', href: '/estudiantes' },
];
```

---

## Cambiar Enlaces del Footer

**Archivo:** `src/components/Footer/Footer.jsx`

```jsx
// Busca este array y modifica:
<li>
  <a href="/portal-apoderados" className={styles.link}>
    Portal Apoderados
  </a>
</li>
<li>
  <a href="/portal-alumnos" className={styles.link}>
    Portal Alumnos
  </a>
</li>
// Agrega nuevos enlaces aquí...
```

---

## Usar Imágenes Locales en lugar de URLs

**Archivo:** `src/pages/Home/Home.jsx`

```jsx
// ANTES (URLs externas):
image: 'https://images.unsplash.com/photo-...'

// DESPUÉS (Imágenes locales):
image: '/images/noticia-1.jpg'
```

**Pasos:**
1. Crea carpeta `public/images/`
2. Pon tus imágenes ahí (noticia-1.jpg, etc)
3. Usa `/images/noticia-1.jpg` en el código

---

## Hacer que un Botón sea un Enlace

**Archivo:** Cualquier lugar donde uses Button

```jsx
// Opción 1: Usar onClick con navegación
<Button variant="primary" onClick={() => window.location.href = '/login'}>
  Ir a Login
</Button>

// Opción 2: Usar anchor tag (si usas React Router):
import { Link } from 'react-router-dom';

<Link to="/login" style={{ textDecoration: 'none' }}>
  <Button variant="primary">Ir a Login</Button>
</Link>
```

---

## Cambiar Ícono de Accesos Rápidos

**Archivo:** `src/pages/Home/Home.jsx`

```jsx
const quickAccess = [
  {
    id: 1,
    icon: '📅', // Cambia esto a cualquier emoji
    // Opciones: 📚 📜 🎓 👥 💻 📊 🎯 ✉️ 🔔 ⚙️
    title: 'Calendario Escolar',
    description: '...',
    link: '#',
  },
  // ...
];
```

---

## Agregar Efectos Hover Personalizados

**Archivo:** `src/pages/Home/Home.module.css`

```css
.card:hover {
  /* Cambiar este efecto */
  transform: translateY(-8px);      /* Sube 8px */
  box-shadow: var(--shadow-lg);     /* Sombra grande */
  border-color: var(--color-primary);
  background-color: #f9f9f9;        /* Fondo gris claro */
}
```

---

## Cambiar el Año en el Footer

**Archivo:** `src/components/Footer/Footer.jsx`

```jsx
// YA ESTÁ AUTOMATIZADO:
const currentYear = new Date().getFullYear();

// Se actualiza automáticamente cada año
<p>&copy; {currentYear} Colegio Bernardo O'Higgins...</p>
```

---

## Hacer que el Navbar no sea Sticky

**Archivo:** `src/components/Navbar/Navbar.module.css`

```css
.navbar {
  /* Cambiar de: */
  position: sticky;
  
  /* A: */
  position: static; /* o absolute, relative, fixed */
}
```

---

## Cambiar el Tamaño de Border-Radius

**Archivo:** `src/styles/_variables.css`

```css
:root {
  /* Más redondeado */
  --radius-md: 16px; /* Era: 8px */
  
  /* Menos redondeado */
  --radius-md: 4px;
}
```

Afecta:
- Botones
- Tarjetas
- Imágenes

---

## Agregar Animación a Tarjetas (Framer Motion)

**Opcional - Requiere instalar:**
```bash
npm install framer-motion
```

**Archivo:** `src/pages/Home/Home.jsx`

```jsx
import { motion } from 'framer-motion';

{quickAccess.map((item, index) => (
  <motion.div
    key={item.id}
    className={styles.card}
    initial={{ opacity: 0, y: 20 }}
    animate={{ opacity: 1, y: 0 }}
    transition={{ delay: index * 0.1 }}
  >
    {/* Contenido del card */}
  </motion.div>
))}
```

---

## Cambiar Tipografía (Font Family)

**Archivo:** `src/styles/_variables.css`

```css
:root {
  /* Cambiar fuente predeterminada */
  --font-family: 'Poppins', sans-serif; /* Era system-ui */
}
```

**Luego importa en index.css:**
```css
@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap');
```

---

## Ejemplos de Títulos

```jsx
// H1 (Más grande)
<h1 className={styles.heroTitle}>Colegio Bernardo O'Higgins</h1>

// H2 (Sección)
<h2 className={styles.sectionTitle}>Accesos Rápidos</h2>

// H3 (Tarjeta)
<h3 className={styles.cardTitle}>Calendario Escolar</h3>
```

---

## Debug: Ver Todos los Colores Disponibles

**En tu navegador, abre la consola y ejecuta:**

```javascript
const root = document.documentElement;
const colors = ['--color-primary', '--color-bg-hero', '--color-footer'];

colors.forEach(color => {
  console.log(`${color}: ${getComputedStyle(root).getPropertyValue(color)}`);
});
```

---

## Resumen Rápido

| Qué cambiar | Archivo |
|------------|---------|
| Colores | `src/styles/_variables.css` |
| Datos de Accesos | `src/pages/Home/Home.jsx` |
| Datos de Noticias | `src/pages/Home/Home.jsx` |
| Enlaces del Navbar | `src/components/Navbar/Navbar.jsx` |
| Enlaces del Footer | `src/components/Footer/Footer.jsx` |
| Tipografía | `src/styles/_variables.css` |
| Espaciado | `src/styles/_variables.css` |
| Efectos Hover | `src/pages/Home/Home.module.css` |

---

**¡Listo para editar! 🚀**

Para cualquier duda, revisa:
- `GUIA_IMPLEMENTACION.md` - Instrucciones de setup
- `ESTRUCTURA_VISUAL.md` - Diagrama visual
- `src/pages/Home/README.md` - Documentación detallada
