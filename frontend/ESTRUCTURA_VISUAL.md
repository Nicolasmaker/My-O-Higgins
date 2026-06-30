# 🎨 Estructura Visual - Landing Page My O'Higgins

## Vista General de la Página

```
┌─────────────────────────────────────────────────────────────┐
│                        NAVBAR                               │
│  My O'Higgins  │ Inicio  Admisión  Nosotros  Contacto │ Login
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                    HERO SECTION                             │
│                    [Fondo Verde Claro]                      │
│                                                              │
│  Colegio Bernardo O'Higgins              [IMAGEN:            │
│  Plataforma de gestión institucional...   Estudiantes]      │
│                                                              │
│  [Botón Primary] [Botón Outline]                            │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                  ACCESOS RÁPIDOS                            │
│                [Fondo Blanco]                               │
│                                                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │     📅      │  │     📚      │  │     📜      │      │
│  │ Calendario  │  │   Portal    │  │   Certificados│     │
│  │   Escolar   │  │  Académico  │  │              │      │
│  │             │  │             │  │              │      │
│  │   Ver →     │  │   Ver →     │  │   Ver →      │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│              NOTICIAS DESTACADAS                            │
│             [Fondo Verde Claro]                             │
│  Ver todas las noticias →                                   │
│                                                              │
│  ┌──────────────────┐  ┌──────────────────┐               │
│  │   [IMAGEN]      │  │   [IMAGEN]      │               │
│  │ ACADÉMICO       │  │ ACTIVIDADES     │               │
│  │ Inicio del...   │  │ Jornada de...   │               │
│  │ Leer más        │  │ Leer más        │               │
│  └──────────────────┘  └──────────────────┘               │
│                                                              │
│  ┌──────────────────┐  ┌──────────────────┐               │
│  │   [IMAGEN]      │  │   [IMAGEN]      │               │
│  │ TECNOLOGÍA      │  │ EVENTOS         │               │
│  │ Nueva Plataforma│  │ Ceremonia de... │               │
│  │ Leer más        │  │ Leer más        │               │
│  └──────────────────┘  └──────────────────┘               │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                   FOOTER                                    │
│              [Fondo Azul Marino #172033]                    │
│                                                              │
│  My O'Higgins  │  Enlaces Rápidos  │  Contacto             │
│  Descripción   │  • Portal Apoderados│  📍 Dirección...    │
│  [Redes]       │  • Portal Alumnos │  📞 +56 2 1234...    │
│                │  • Calendario     │                       │
│                │  • etc...         │                       │
│                                                              │
│  © 2024 Colegio... │ Privacidad | Términos | Cookies      │
└─────────────────────────────────────────────────────────────┘
```

---

## Estructura JSX Detallada

### Navbar
```jsx
<nav className={styles.navbar}>
  <div className={styles.container}>
    <div className={styles.logo}>
      "My O'Higgins"
    </div>
    <div className={styles.navLinks}>
      <a className={styles.active}>Inicio</a>
      <a>Admisión</a>
      <a>Nosotros</a>
      <a>Contacto</a>
    </div>
    <div className={styles.navActions}>
      <Button variant="primary">Iniciar Sesión</Button>
    </div>
  </div>
</nav>
```

### Hero Section
```jsx
<section className={styles.heroSection}>
  <div className={styles.heroContainer}>
    <div className={styles.heroContent}>
      <h1>Colegio Bernardo O'Higgins</h1>
      <p>Plataforma de gestión institucional...</p>
      <div className={styles.heroButtons}>
        <Button variant="primary">Ingresar →</Button>
        <Button variant="outline">Proceso de Matrícula</Button>
      </div>
    </div>
    <div className={styles.heroImage}>
      <img src="..." />
    </div>
  </div>
</section>
```

### Accesos Rápidos (Map)
```jsx
<div className={styles.cardsGrid}>
  {quickAccess.map(item => (
    <div key={item.id} className={styles.card}>
      <div className={styles.cardIcon}>{item.icon}</div>
      <h3>{item.title}</h3>
      <p>{item.description}</p>
      <a href={item.link}>Ver →</a>
    </div>
  ))}
</div>
```

### Noticias (Map)
```jsx
<div className={styles.newsGrid}>
  {news.map(item => (
    <article key={item.id} className={styles.newsCard}>
      <img src={item.image} />
      <div className={styles.newsContent}>
        <span className={styles.newsCategory}>{item.category}</span>
        <h3>{item.title}</h3>
        <p>{item.excerpt}</p>
        <a href={item.link}>Leer más</a>
      </div>
    </article>
  ))}
</div>
```

---

## Datos Arrays (Ejemplo)

### quickAccess Array
```javascript
[
  {
    id: 1,
    icon: '📅',
    title: 'Calendario Escolar',
    description: 'Consulta fechas importantes...',
    link: '#'
  },
  {
    id: 2,
    icon: '📚',
    title: 'Portal Académico',
    description: 'Acceso a calificaciones...',
    link: '#'
  },
  {
    id: 3,
    icon: '📜',
    title: 'Certificados',
    description: 'Descarga certificados...',
    link: '#'
  }
]
```

### news Array
```javascript
[
  {
    id: 1,
    image: 'https://...',
    category: 'Académico',
    title: 'Inicio del Nuevo Período Académico',
    excerpt: 'Nos complace anunciar...',
    link: '#'
  },
  // ... 3 noticias más
]
```

---

## Componentes Importados en Home.jsx

```jsx
import Button from '../../components/UI/Button';
import Navbar from '../../components/Navbar/Navbar';
import Footer from '../../components/Footer/Footer';
import styles from './Home.module.css';

export default function Home() {
  // Datos locales
  const quickAccess = [...]
  const news = [...]
  
  return (
    <div className={styles.page}>
      <Navbar />
      <section className={styles.heroSection}>...</section>
      <section className={styles.quickAccessSection}>...</section>
      <section className={styles.newsSection}>...</section>
      <Footer />
    </div>
  )
}
```

---

## Estados de Componentes

### Navbar.jsx
```javascript
const [activeLink, setActiveLink] = useState('inicio');

// Cambia cuando:
<a onClick={() => setActiveLink('admision')}>Admisión</a>

// Se aplica estilo:
className={activeLink === 'admision' ? styles.active : ''}
```

---

## Responsive Transformations

### Desktop (1024px+)
```
Hero: 2 columnas (contenido | imagen)
Noticias: 2 columnas
Navbar: Horizontal completo
```

### Tablet (768px - 1024px)
```
Hero: 1 columna (imagen debajo)
Noticias: 2 columnas (puede ajustarse)
Navbar: Se adapta con ajustes de gap
```

### Mobile (<768px)
```
Hero: 1 columna
Botones Hero: Stack vertical (100% ancho)
Accesos: 1 columna
Noticias: 1 columna
Navbar: Stack en mobile (orden ajustado)
```

---

## Integración en App.jsx

```jsx
import Home from './pages/Home/Home'

// En las rutas:
<Route path="/" element={<Home />} />
```

---

## Archivos CSS Modules Usados

| Archivo | Clases Principales |
|---------|-------------------|
| Button.module.css | `.button`, `.primary`, `.outline` |
| Navbar.module.css | `.navbar`, `.navLink`, `.active`, `.logo` |
| Footer.module.css | `.footer`, `.column`, `.link`, `.contactItem` |
| Home.module.css | `.heroSection`, `.cardsGrid`, `.newsGrid`, `.card`, `.newsCard` |

---

## Flujo de Render

1. App.jsx carga
2. App.jsx renderiza `<Home />`
3. Home.jsx renderiza:
   - `<Navbar />` ← Importado
   - Hero Section con contenido y imagen
   - Quick Access con map de array
   - News Section con map de array
   - `<Footer />` ← Importado
4. Todos los estilos vienen de CSS Modules

---

## Performance Consideraciones

✅ Imágenes: Unsplash CDN (externas, no cargan en build)
✅ Sin animaciones pesadas (solo transiciones CSS)
✅ Componentes simples sin state complejo
✅ Array maps sin lógica pesada
✅ CSS Modules evitan recálculos de estilos

---

**Versión:** 1.0
**Última actualización:** 2024
**Estado:** Completado ✅
