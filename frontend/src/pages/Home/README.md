# Landing Page - My O'Higgins 🎓

## 📋 Descripción General

La landing page principal de My O'Higgins es una página moderna y responsive que presenta la plataforma de gestión institucional del Colegio Bernardo O'Higgins. Está construida con React + Vite y utiliza CSS Modules para mantener los estilos organizados y sin conflictos.

## 🎨 Estructura y Componentes Creados

### 1. **Variables de Estilo** (`src/styles/_variables.css`)
Define toda la paleta de colores, espaciado, tipografía y otras propiedades CSS utilizadas en toda la aplicación. Estas variables se pueden reutilizar en cualquier componente importando este archivo.

**Variables principales:**
```css
--color-primary: #6B2323 (Rojo Granate)
--color-bg-hero: #F3F9F1 (Verde Claro/Beige)
--color-footer: #172033 (Azul Marino Oscuro)
--color-bg-white: #FFFFFF
```

### 2. **Componente Button** (`src/components/UI/Button.jsx`)
Componente reutilizable de botones con dos variantes principales.

**Props:**
- `variant`: `'primary'` (relleno) | `'outline'` (borde) | `'secondary'`
- `children`: Contenido del botón
- `onClick`: Función de callback
- `disabled`: Deshabilitar botón
- `type`: `'button'` | `'submit'` | `'reset'`

**Ejemplo de uso:**
```jsx
<Button variant="primary">Ingresar a My O'Higgins</Button>
<Button variant="outline">Proceso de Matrícula</Button>
```

### 3. **Navbar** (`src/components/Navbar/Navbar.jsx`)
Barra de navegación fija (sticky) con:
- Logo "My O'Higgins" a la izquierda
- Enlaces de navegación en el centro (Inicio, Admisión, Nosotros, Contacto)
- Botón de "Iniciar Sesión" a la derecha

**Características:**
- Indicador visual de enlace activo (subrayado rojo)
- Responsive con men´u adaptado en mobile
- Fondo en color `--color-bg-hero`

### 4. **Footer** (`src/components/Footer/Footer.jsx`)
Pie de página con información completa:
- **Columna 1**: Branding, descripción y redes sociales
- **Columna 2**: Enlaces rápidos (Portal Apoderados, Portal Alumnos, etc.)
- **Columna 3**: Contacto (Dirección y Teléfono)
- **Barra inferior**: Copyright y enlaces legales

**Características:**
- Fondo azul marino oscuro (`--color-footer`)
- Íconos SVG personalizados
- Totalmente responsive

### 5. **Página Home** (`src/pages/Home/Home.jsx`)
Integra todos los componentes anteriores en una landing page completa con tres secciones principales:

#### **5.1 Sección Hero**
- Título principal: "Colegio Bernardo O'Higgins"
- Descripción de la plataforma
- Dos botones de acción: "Ingresar" y "Proceso de Matrícula"
- Imagen decorativa de estudiantes
- Fondo: Verde claro (`--color-bg-hero`)

#### **5.2 Accesos Rápidos**
Grid de 3 tarjetas con:
- Icono emoji
- Título
- Descripción
- Enlace "Ver →"

**Tarjetas incluidas:**
1. Calendario Escolar
2. Portal Académico
3. Certificados

#### **5.3 Noticias Destacadas**
Grid de 2 columnas (4 tarjetas totales) con:
- Imagen destacada
- Categoría (ej. "ACADÉMICO")
- Título y extracto
- Enlace "Leer más"

Incluye datos de ejemplo de noticias escolares.

---

## 🚀 Cómo Usar

### Integración en la Aplicación

El componente Home ya está integrado en el `App.jsx` en la ruta raíz (`/`):

```jsx
<Route path="/" element={<Home />} />
```

### Estructura de Archivos Creados

```
frontend/src/
├── styles/
│   └── _variables.css          (Variables globales de colores y tipografía)
├── components/
│   ├── UI/
│   │   ├── Button.jsx          (Componente reutilizable de botón)
│   │   └── Button.module.css
│   ├── Navbar/
│   │   ├── Navbar.jsx
│   │   └── Navbar.module.css
│   └── Footer/
│       ├── Footer.jsx
│       └── Footer.module.css
└── pages/
    └── Home/
        ├── Home.jsx            (Página principal)
        ├── Home.module.css
        └── README.md           (Este archivo)
```

---

## 🎯 Paleta de Colores

| Nombre | Código | Uso |
|--------|--------|-----|
| Primario | `#6B2323` | Botones, títulos, logo |
| Fondo Hero | `#F3F9F1` | Secciones Hero y Noticias |
| Blanco | `#FFFFFF` | Fondo Accesos Rápidos |
| Footer | `#172033` | Pie de página |
| Texto Principal | `#1a1a1a` | Textos generales |
| Texto Secundario | `#666666` | Descripciones |

---

## 📱 Responsive Design

Todos los componentes son completamente responsive con breakpoints en:
- **1024px**: Tablets grandes
- **768px**: Tablets y mobile landscape
- **480px**: Mobile pequeño

**Cambios principales en mobile:**
- Navbar: Se convierte en stack vertical
- Hero: Grid de 2 columnas → 1 columna
- Botones: Se expanden al 100% del ancho
- Noticias: Grid 2 columnas → 1 columna

---

## 🔧 Personalización

### Cambiar Colores

Edita las variables en `src/styles/_variables.css`:

```css
--color-primary: #TU_COLOR_AQUÍ;
--color-bg-hero: #TU_COLOR_AQUÍ;
/* etc... */
```

### Agregar Más Tarjetas

En `Home.jsx`, modifica el array `quickAccess` o `news`:

```jsx
const quickAccess = [
  { id: 1, icon: '📅', title: '...', description: '...', link: '#' },
  // Agrega más aquí
];
```

### Crear Nuevos Botones

Usa el componente Button con props:

```jsx
<Button variant="primary" onClick={handleClick}>
  Mi Botón
</Button>
```

---

## 📦 Dependencias

- React 18+
- CSS Modules (nativo en Vite)
- No requiere librerías de estilos externas (CSS puro)

---

## ✨ Características Clave

✅ Diseño moderno y limpio
✅ Totalmente responsive
✅ CSS Modules para evitar conflictos de estilos
✅ Componentes reutilizables
✅ Variables de color centralizadas
✅ Navegación sticky
✅ Animaciones y transiciones suaves
✅ Accesibilidad mejorada (semantic HTML, aria labels)
✅ Performance optimizado

---

## 🎬 Próximos Pasos

Para continuar desarrollando la plataforma:

1. **Conectar con Backend**: Reemplazar datos estáticos con llamadas a API
2. **Crear Páginas Adicionales**: Portal Académico, Calendario, Noticias completa
3. **Sistema de Autenticación**: Integrar con el Login existente
4. **Animaciones**: Agregar más transiciones con Framer Motion (opcional)
5. **Temas**: Implementar modo oscuro (light/dark theme)

---

## 👨‍💻 Autor

Desarrollado como parte del proyecto My O'Higgins
Frontend: React + Vite + CSS Modules

**Última actualización:** 2024
