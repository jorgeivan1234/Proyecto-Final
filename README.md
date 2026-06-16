# DualHabit 
### *Cuidando de ti, cuidando de ellos.*

**DualHabit** es una aplicación móvil nativa para Android diseñada específicamente para la gestión integral y el bienestar de las mascotas y sus dueños. El proyecto nace de la necesidad de centralizar en una sola herramienta el control médico, la organización de hábitos diarios y la localización de servicios esenciales, promoviendo una vida saludable y activa para los animales de compañía.

---

##  Características Principales

La aplicación ofrece una interfaz intuitiva y moderna desarrollada con **Jetpack Compose**, organizada en los siguientes módulos:

###  Gestión de Salud y Bienestar
*   **Bitácora Médica:** Registro detallado de vacunas, desparasitaciones y tratamientos médicos.
*   **Calendario Inteligente:** Selección de fechas mediante `DatePicker` para evitar errores manuales y llevar un control cronológico exacto.
*   **Historial de Salud:** Visualización y edición de registros previos con filtros por tipo de evento.

###  Hidratación Dual
*   **Metas Personalizadas:** Cálculo dinámico de la meta diaria de agua basada en el peso, edad y nivel de actividad.
*   **Sincronización Meteorológica:** Ajuste automático de los requerimientos de hidratación mediante la integración de una API de clima en tiempo real.
*   **Seguimiento para Mascota:** Módulo específico para monitorear el consumo de agua de la mascota de forma independiente al dueño.

###  Actividad Física y Paseos
*   **Registro de Paseos:** Control de duración y notas sobre el comportamiento durante las salidas diarias.
*   **Guía de Ejercicios:** Listado de actividades físicas recomendadas según la especie y energía del animal.

###  Alimentación y Nutrición
*   **Recetario Local:** Base de datos persistente con recetas saludables y dietas específicas.
*   **Dietas por Especie:** Separación inteligente entre planes alimenticios para humanos y mascotas.

###  Integración con Servicios Externos
*   **Localizador de Clínicas:** Acceso directo a Google Maps para encontrar veterinarias, parques y tiendas de mascotas cercanas.
*   **Buzón de Sugerencias:** Canal directo para que los usuarios envíen feedback sobre la experiencia de uso.

---

## Arquitectura y Tecnologías

El proyecto sigue los principios de **Clean Architecture** y las guías oficiales de Google para el desarrollo moderno de Android.

### **Arquitectura: MVVM (Model-View-ViewModel)**
*   **View (Compose):** Pantallas declarativas que reaccionan a los cambios de estado.
*   **ViewModel:** Gestión de la lógica de negocio y comunicación con la capa de datos, manteniendo la integridad de la UI ante cambios de configuración.
*   **Model (Data Layer):** Repositorios que gestionan tanto la persistencia local como las peticiones remotas.

### **Stack Tecnológico:**
*   **Lenguaje:** [Kotlin 2.0.21](https://kotlinlang.org/) con compilador K2.
*   **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material Design 3).
*   **Persistencia:** [Room Database 2.7.0-alpha11](https://developer.android.com/training/data-storage/room) con soporte para Flows asíncronos.
*   **Networking:** [Retrofit 2.9.0](https://square.github.io/retrofit/) & GSON para el consumo de APIs REST.
*   **Imágenes:** [Coil 2.7.0](https://coil-kt.github.io/coil/) para carga eficiente y caché de fotografías de perfil.
*   **Backend:** [Firebase (Auth & Firestore)](https://firebase.google.com/) para autenticación segura y almacenamiento en la nube.
*   **Dependency Management:** Gradle Kotlin DSL con Version Catalog (`libs.versions.toml`).

---

##  Estructura del Proyecto

```text
com.fic.dualhabit10/
├── data/
│   ├── local/          # Entidades de Room, DAOs y AppDatabase.
│   ├── remote/         # Definición de interfaces API y servicios de red.
│   └── model/          # Clases de datos (POJOs) para la lógica de negocio.
├── ui/
│   ├── screens/        # Componibles de UI (Home, Salud, Perfil, etc.).
│   ├── viewmodels/     # State management y lógica de cada pantalla.
│   └── theme/          # Sistema de diseño: Colores, Tipografías y Dimensiones.
└── MainActivity.kt     # Orquestador principal y NavHost (Navegación).
```

---

## 🛠 Requisitos e Instalación

### **Requisitos del Sistema**
*   **Mínimo SDK:** Android 8.0 (API 26).
*   **Target SDK:** Android 15 (API 35).
*   **Entorno de Desarrollo:** Android Studio Ladybug o superior.

### **Pasos para Instalar**
1.  **Clonar el repositorio:**
    ```bash
    git clone https://github.com/tu-usuario/DualHabit.git
    ```
2.  **Configurar Firebase:**
    Colocar el archivo `google-services.json` generado en la consola de Firebase dentro de la carpeta `/app`.
3.  **Sincronizar Gradle:**
    Abrir el proyecto y esperar a que el catálogo de versiones descargue las dependencias.
4.  **Ejecutar:**
    Presionar `Shift + F10` o el botón de *Run* para instalar en un emulador o dispositivo físico.

---

##  Autores y Créditos

Este proyecto ha sido desarrollado como **Proyecto Final** por el equipo de estudiantes de la materia de **[Insertar Nombre de la Materia]**:

*    **Jorge Ivan Ojeda Cerrillo**
*    **Dulce Elsa Gloria**
*    **Jesus Antonio Hernandez Beltran**
*    **Lara Herrera Jesart Isaac**

---
*DualHabit 1.0 - Elevando el estándar de cuidado para los que no tienen voz.*
