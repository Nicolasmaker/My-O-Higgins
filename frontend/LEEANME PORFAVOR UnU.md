# ESTRUCTURA

ya xavales, replique la estructura de carpetas del proyecto karma del profe. Cada carpeta servira para esto:

# 1. CARPETA ASSETS
En teoria se guarda todo lo que son imagenes, iconos y fuentes, si necesitamos algo de aqui lo llamamos nomas, no tiene nada de logica de programacion por decirlo asi. 

Desconozco si las imagenes tenemos que tenerlas almacenadas en la base de datos ya que el profesor lo hizo asi (hay que cachar que es lo mejor supongo)

## EJEMPLO DE ESTRUCTURA
assets/
├── logo.png
├── banner-hero.jpg
└── icons/
    └── flecha.svg

<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


# 2. CARPETEA COMPONENTS Y QUE SON LOS COMPONENTES
Aqui van archivos que son reutilizables, que quiero decir con esto?

Un componente es un bloque de interfaz reutilizable, Como piezas de LEGO. En vez de escribir toda la página de una vez en un solo archivo enorme, la partes en pedazos. Cada pedazo es un componente. EJEMPLO DE IA

┌─────────────────────────────────┐
│  < Navbar />                    │  ← Componente                                   // Botón.jsx — un componente simple
├─────────────────────────────────┤                                                 function Boton() {
│                                 │                                                      return (
│  < HeroSection />               │  ← Componente                                           <button>Haz clic aquí</button>
│                                 │                                                      )
├────────────┬────────────────────┤                                                 }
│            │                    │
│ <Card />   │  <Card />  <Card/> │  ← Mismo componente, usado 3 veces
│            │                    │
├─────────────────────────────────┤
│  < Footer />                    │  ← Componente
└─────────────────────────────────┘

Al crear un componente boton por ejemplo. Lo escribes UNA VEZ y lo usas en 50 páginas distintas. Si se cambia algo en Boton.jsx, cambia en todos lados automáticamente.


## 2.1 QUE ES UN PROPS (ESTO TIENE QUE VER CON CODIGO, ES MAS QUE NADA PARA QUE SEPAN QUE ES YA QUE VA RELACIONADO CON LOS COMPONENTES)

Las props son los parámetros que le pasas a un componente. Como configurar algo al comprarlo. 
Si tienes un componente Boton, quizás en una página quieres que diga "Guardar" y en otra "Eliminar". Las props te permiten eso:

// Definición del componente — recibe "texto" y "color" como props

function Boton({ texto, color }) {
  return (
    <button style={{ background: color }}>
      {texto}
    </button>
  )
}

// Uso en diferentes páginas (ejemplo de lo q colocarias en otro archivo q necesitas usar el boton)
<Boton texto="Guardar"   color="green" />
<Boton texto="Eliminar"  color="red"   />
<Boton texto="Cancelar"  color="gray"  />


## EJEMPLO DE ESTRUCTURA
components/
├── Navbar/         ← La barra de navegación (aparece en todas las páginas)
├── Footer/         ← El pie de página (ídem)
├── Modal/          ← Una ventana emergente genérica
└── UI/
    ├── Button/     ← Tu botón con tu diseño (lo reutilizas en todo el proyecto)
    └── Input/      ← Tu input estilizado


<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


# 3. CARPETA CONTEXT (IA) (AKI IRIA LA WEA DE LA AUTENTICACION, COMO EL TOKEN QUE SE GENERA POR USUARIO)
El context resuelve el problema de pasar props (2.1) a través de muchos niveles. Imagina que haces login. Tu nombre de usuario lo necesitan:

El Navbar (para mostrarlo arriba)
La página MiPerfil (para mostrar tus datos)
La página MisPedidos (para saber de quién son los pedidos)

Sin context, tendrías que pasar el usuario así:

App
└── Layout (recibe usuario, no lo usa, solo lo pasa)
    └── Navbar (recibe usuario, no lo usa, solo lo pasa)
        └── MenuUsuario (recibe usuario, no lo usa, solo lo pasa)
            └── NombreUsuario ← el único que lo necesita de verdad

Basicamente es pasar la wea de usuario entre los componentes para que un solo kuliao lo use si no usamos el context. Con Context, lo guardas en un lugar central y cualquier componente lo toma directamente:

AuthContext ──────────────────────────────────┐
                                              ↓
App → Layout → Navbar → MenuUsuario → NombreUsuario
                                        (toma el usuario
                                         directo del context,
                                         sin que nadie se lo pase)


## EJEMPLO DE ESTRUCTURA
context/
└── AuthContext.jsx    ← Guarda: ¿quién está logueado? ¿cuál es su token?                                


<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

# 4. CARPETA HOOKS Y QUE SON LOS HOOKS

## 4.1 HOKS
Mas que nada son funciones que le dan un comportamiento a un componente por ejemplo el "useState" guarda datos que pueden cambiar 

function Contador() {
  const [numero, setNumero] = useState(0)  // numero empieza en 0
  return (
    <div>
      <p>Llevas {numero} clics</p>
      <button onClick={() => setNumero(numero + 1)}>
        Clic!
      </button>
    </div>
  )
}
Cada vez que haces clic, numero cambia y la pantalla SE ACTUALIZA SOLA.


El "useEffect" se usa para hacer algo cuando la pagina carga

function ListaProductos() {
  const [productos, setProductos] = useState([])
  useEffect(() => {
    // Esto corre cuando la página carga
    fetch('http://localhost:8080/api/productos')
      .then(res => res.json())
      .then(data => setProductos(data))
  }, [])  // El [] significa "solo hazlo una vez al cargar"
  return (
    <ul>
      {productos.map(p => <li key={p.id}>{p.nombre}</li>)}
    </ul>
  )
}

por lo que entiendo el codigo de arriba es que cada vez que quieras listar los productos usas el useEffect para que vaya al link a comunicarse con el backend y te lo muestra 


## 4.2 CARPETA HOOKS (AQUI IRIAN LOS QUE UNO CREA DE FORMA PERSONALIZADA)
Lógica reutilizable que varios componentes comparten. Son funciones especiales de React (empiezan con use). Sirven para no repetir el mismo código en múltiples páginas.

EJEMPLO: 

// useAuth.js — en vez de escribir esto en cada componente:
const context = useContext(AuthContext)  // <- repetir esto en 20 páginas


// Creas el hook una vez:
export function useAuth() {
  return useContext(AuthContext)
}


// Y en cualquier página simplemente escribes:
const { usuario, logout } = useAuth()

Basicamente es una wea parecida al context, aqui irian hooks personalizados para no repetir el mismo codigo en 20 weas y aqui lo creas para despues llamarlos 


<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


# 5. CARPETA PAGES
Aqui unicamente se guardan los archivos de las paginas web, como la pagina principal, la del login, etc. Como seran muuuuchos archivos, habra que agregar carpetas, ahi vemos como las nombramos. Podriamos simplemente llamar la carpeta del microservicio y dentro los archivos, pero noc si quede "logico" por ejemplo si suponemos que tenemos las carpetas con los nombres de los ms, donde chucha metes "paginaPrincipal.jsx" "Contacto.jsx" XD

## EJEMPLO DE ESTRUCTURA
pages/
├── Home/           ← Lo que ve el usuario en "/"
├── Login/          ← Pantalla de inicio de sesión
├── Registro/       ← Formulario de registro
└── MiPerfil/       ← Página del perfil del usuario


<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


# 6. CARPETA SERVICES
Son archivos dedicados a la comunicacion con el backend, aqui nada relacionado al front, solo comunicacion

## EJEMPLO DE ESTRUCTURA
services/
├── axiosConfig.js          ← Configuración base de HTTP
├── authService.js          ← login, register
├── usuarioService.js       ← getUsuario, updateUsuario
└── productoService.js      ← getProductos, crearProducto
Un service es simplemente funciones que hacen peticiones HTTP. No saben nada de botones, pantallas ni estilos. Solo hablan con la API.


// productoService.js — esto es TODO lo que hace un service
import api from './axiosConfig'
export const getProductos = () => api.get('/productos')
export const crearProducto = (data) => api.post('/productos', data)
export const eliminarProducto = (id) => api.delete(`/productos/${id}`)



<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


# 7. CARPETA STYLES (CSS O SCSS)
Los archivos de los estilos duh. ya sea de una pagina completa o un boton, una casilla, etc etc. Como el color, fuente, tamaño, redondez, bla bla bla.

## EJEMPLO DE ESTRUCTURA
styles/
├── _variables.scss    ← Tus colores, tamaños de fuente, espaciados
├── _reset.scss        ← Elimina estilos por defecto del navegador
└── main.scss          ← Importa todo lo anterior + estilos base del body


// _variables.scss
color-primario: #6C63FF;
color-texto: #1A1A2E;
fuente-principal: 'Inter', sans-serif;


<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


# 8. CARPETA VALIDATORS
Son para hacer validaciones como de los formularios por ejemplo, envez de escribir las mismas restricciones en cada formulario creamos un archivo q ponte tu sea para verificar el @ de los correos entonces al crear eso lo llamamos en la pagina (carpeta pages) al archivo de esta carpeta (validators) para que aplique la validacion del @ en un formulario

ejemplo de un archivo de validacion

// fieldValidators.js
export const emailRules = {
  required: 'El email es obligatorio',
  pattern: { value: /\S+@\S+\.\S+/, message: 'Email inválido' }
}

entonces esta wea la llamamos nomas y listo


<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


# ASI SE VE EL PROYECTO EN RESUMEN (SI NOS BASAMOS EN LO Q HIZO EL PROFE CON EL PROYECTO KARMA)
src/
├── assets/              # Imágenes, íconos, fuentes
│
├── components/          # Componentes reutilizables
│   ├── Navbar/
│   │   └── Navbar.jsx
│   ├── Footer/
│   │   └── Footer.jsx
│   └── UI/              # Botones, inputs, modales genéricos
│       └── Button/
│
├── context/             # Estado global (autenticación, etc.)
│   └── AuthContext.jsx
│
├── hooks/               # Custom hooks reutilizables
│   └── useAuth.js
│
├── pages/               # Una carpeta por página/vista
│   ├── Home/
│   │   └── Home.jsx
│   ├── Login/
│   │   └── Login.jsx
│   └── ...
│
├── services/            # Toda la comunicación con el backend
│   ├── axiosConfig.js   ← SE CREA PRIMERO
│   └── [entidad]Service.js
│
├── styles/              # Estilos globales y variables
│   ├── _variables.scss
│   ├── _reset.scss
│   └── main.scss
│
├── validators/          # Reglas de validación de formularios
│   └── fieldValidators.js
│
├── App.jsx              # Router principal
└── main.jsx             # Punto de entrada


ESTO YA VENIA EN EL DOCUMENTO XD


# React + Vite

This template provides a minimal setup to get React working in Vite with HMR and some ESLint rules.

Currently, two official plugins are available:

- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react) uses [Oxc](https://oxc.rs)
- [@vitejs/plugin-react-swc](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react-swc) uses [SWC](https://swc.rs/)

## React Compiler

The React Compiler is not enabled on this template because of its impact on dev & build performances. To add it, see [this documentation](https://react.dev/learn/react-compiler/installation).

## Expanding the ESLint configuration

If you are developing a production application, we recommend using TypeScript with type-aware lint rules enabled. Check out the [TS template](https://github.com/vitejs/vite/tree/main/packages/create-vite/template-react-ts) for information on how to integrate TypeScript and [`typescript-eslint`](https://typescript-eslint.io) in your project.

