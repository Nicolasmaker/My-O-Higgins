import { useMemo, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import Button from '../../components/UI/Button';
import ProcesoMatriculaModal from '../../components/Home/ProcesoMatriculaModal';
import MuralDigital from '../../components/Home/MuralDigital';
import styles from './Home.module.css';
import fachadaColegioBO from '../../assets/fachadaColegioBO.png';
import iconoCalendario from '../../assets/iconoCalendario.png';
import iconoPortal from '../../assets/iconoPortal.png';
import iconoCertificado from '../../assets/iconoCertificado.png';
import academicoImg from '../../assets/academico.png';
import { getRecommendedQuickAccess, initializeQuickAccessItems, updateQuickAccessUsage } from './quickAccessUtils';

const baseQuickAccess = [
  {
    id: 1,
    icon: iconoCalendario,
    title: 'Calendario Escolar',
    description: 'Consulta fechas importantes, feriados y períodos académicos del año.',
    link: '/calendario',
  },
  {
    id: 2,
    icon: iconoPortal,
    title: 'Portal Académico',
    description: 'Acceso a calificaciones, horarios y material educativo de tus cursos.',
    link: '/academico',
  },
  {
    id: 3,
    icon: iconoPortal,
    title: 'Anotaciones',
    description: 'Revisa y administra tareas, notas y material de tus clases.',
    link: '/anotaciones',
  },
  {
    id: 5,
    icon: iconoCalendario,
    title: 'Reuniones',
    description: 'Consulta y organiza juntas entre docentes, apoderados y directivos.',
    link: '/reuniones',
  },
  {
    id: 6,
    icon: iconoPortal,
    title: 'Mensajería',
    description: 'Envía y recibe comunicaciones oficiales del colegio.',
    link: '/mensajeria',
  },
  {
    id: 7,
    icon: iconoCertificado,
    title: 'Matrículas',
    description: 'Gestiona el proceso de matrícula de tu curso o tu pupilo.',
    link: '/matriculas',
  },
];

export default function Home() {
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();
  const [quickAccess, setQuickAccess] = useState(() => initializeQuickAccessItems(baseQuickAccess));
  const [showProceso, setShowProceso] = useState(false);

  const recommendedQuickAccess = useMemo(() => getRecommendedQuickAccess(quickAccess), [quickAccess]);

  const handleQuickAccessClick = (item) => {
    setQuickAccess((currentItems) => updateQuickAccessUsage(currentItems, item.id));
    navigate(item.link);
  };

  // Usuario logueado: la portada pública (hero/accesos/noticias) se reemplaza por el Mural
  // Digital — un tablero personalizado por rol (evaluaciones propias, eventos que le
  // corresponden ver, avisos/talleres para todos).
  if (isAuthenticated) {
    return <MuralDigital />;
  }

  const news = [
    {
      id: 1,
      image: academicoImg,
      category: 'Académico',
      title: 'Inicio del Nuevo Período Académico 2026',
      excerpt:
        'Nos complace anunciar el inicio del nuevo período académico. Todos los estudiantes deben presentarse con sus útiles y uniformes completos.',
      link: '#',
    },
    {
      id: 2,
      image: 'https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=600&h=400&fit=crop',
      category: 'Actividades',
      title: 'Jornada de Integración 2026',
      excerpt:
        'Se realizará una jornada de integración para todos los cursos con actividades recreativas y deportivas en el patio principal.',
      link: '#',
    },
    {
      id: 3,
      image: 'https://images.unsplash.com/photo-1523580494863-6f3031224c94?w=600&h=400&fit=crop',
      category: 'Tecnología',
      title: 'Nueva Plataforma de Aula Virtual',
      excerpt:
        'Hemos implementado una nueva plataforma de educación a distancia con mejores herramientas de comunicación y seguimiento académico.',
      link: '#',
    },
    {
      id: 4,
      image: 'https://images.unsplash.com/photo-1513364776144-60967b0f800f?w=600&h=400&fit=crop',
      category: 'Eventos',
      title: 'Ceremonia de Premiación a la Excelencia',
      excerpt:
        'Se llevará a cabo la ceremonia anual de premiación a los estudiantes destacados académica y deportivamente.',
      link: '#',
    },
  ];

  return (
    <div className={styles.page}>
      <section className={styles.heroSection} id="inicio">
        <div className={styles.heroContainer}>
          <div className={styles.heroContent}>
            <h1 className={styles.heroTitle}>Colegio Bernardo O'Higgins</h1>
            <p className={styles.heroDescription}>
              Plataforma de gestión institucional que conecta a estudiantes, apoderados y 
              docentes en un único espacio digital. Acceso fácil a calificaciones, calendarios, 
              certificados y toda la información académica importante.
            </p>

            <div className={styles.heroButtons}>
              <Button variant="primary" onClick={() => navigate('/login')}>
                Ingresar a My O'Higgins →
              </Button>
              <Button variant="outline" onClick={() => setShowProceso(true)}>
                Proceso de matrícula →
              </Button>
            </div>
          </div>

          <div className={styles.fachadaColegioBO}>
            <img
              src={fachadaColegioBO}
              alt="Estudiantes en el colegio"
              className={styles.fachadaColegioBO}
            />
          </div>
        </div>
      </section>

      <section className={styles.quickAccessSection} id="admision">
        <div className={styles.container}>
          <div className={styles.sectionHeader}>
            <div>
              <h2 className={styles.sectionTitle}>Accesos Rápidos</h2>
              <p className={styles.sectionSubtitle}>
                Accede rápidamente a las herramientas más utilizadas del portal
              </p>
            </div>
          </div>

          <div className={styles.cardsGrid}>
            {recommendedQuickAccess.map((item) => (
              <div key={item.id} className={styles.card}>
                <div className={styles.cardIcon}>
                  <img
                    src={item.icon}
                    alt={`Icono de ${item.title}`}
                    className={styles.iconImage}
                  />
                </div>
                <h3 className={styles.cardTitle}>{item.title}</h3>
                <p className={styles.cardDescription}>{item.description}</p>
                <button
                  type="button"
                  className={styles.cardLinkButton}
                  onClick={() => handleQuickAccessClick(item)}
                  aria-label={`Ir a ${item.title}`}
                >
                  Ir a {item.title} →
                </button>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className={styles.newsSection} id="noticias">
        <div className={styles.container}>
          <div className={styles.newsHeader}>
            <div>
              <h2 className={styles.sectionTitle}>Noticias Destacadas</h2>
              <p className={styles.sectionSubtitle}>
                Mantente informado de todas las novedades del colegio
              </p>
            </div>
            <a href="#" className={styles.viewAllLink}>
              Ver todas las noticias →
            </a>
          </div>

          <div className={styles.newsGrid}>
            {news.map((item) => (
              <article key={item.id} className={styles.newsCard}>
                <div className={styles.newsImageWrapper}>
                  <img
                    src={item.image}
                    alt={item.title}
                    className={styles.newsImage}
                  />
                </div>

                <div className={styles.newsContent}>
                  <span className={styles.newsCategory}>{item.category}</span>
                  <h3 className={styles.newsTitle}>{item.title}</h3>
                  <p className={styles.newsExcerpt}>{item.excerpt}</p>
                  <a href={item.link} className={styles.newsLink}>
                    Leer más
                  </a>
                </div>
              </article>
            ))}
          </div>
        </div>
      </section>

      <ProcesoMatriculaModal show={showProceso} onClose={() => setShowProceso(false)} />
    </div>
  );
}