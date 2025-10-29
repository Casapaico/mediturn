# 🏥 MediTurn - Sistema de Citas Médicas

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.20-blue.svg)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5.4-green.svg)](https://developer.android.com/jetpack/compose)
[![Android](https://img.shields.io/badge/Android-26%2B-brightgreen.svg)](https://developer.android.com)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**MediTurn** es una aplicación móvil nativa de Android que permite a los pacientes buscar médicos, agendar citas y gestionar su calendario médico de forma simple e intuitiva.

---

## 📱 Capturas de Pantalla

<table>
  <tr>
    <td><img src="screenshots/home.png" width="200"/></td>
    <td><img src="screenshots/search.png" width="200"/></td>
    <td><img src="screenshots/doctor_detail.png" width="200"/></td>
    <td><img src="screenshots/book_appointment.png" width="200"/></td>
  </tr>
  <tr>
    <td align="center"><b>Home</b></td>
    <td align="center"><b>Búsqueda</b></td>
    <td align="center"><b>Detalle Médico</b></td>
    <td align="center"><b>Agendar Cita</b></td>
  </tr>
</table>

---

## 🎯 Características Principales

✅ **Búsqueda Avanzada de Médicos**
- Búsqueda por nombre, especialidad o ciudad
- Filtros múltiples (especialidad, ciudad, teleconsulta)
- Búsqueda reactiva con debounce
- Contador de resultados en tiempo real

✅ **Perfil Detallado del Médico**
- Información completa: experiencia, calificación, precio
- Indicador de disponibilidad para teleconsulta
- Colegiatura y departamento médico
- Botón directo "Agendar Cita"

✅ **Agendar Citas Inteligente**
- Selección de especialidad y médico
- Selector de fecha (próximos 7 días)
- Horarios disponibles (9:00-18:00)
- Validaciones en tiempo real
- Confirmación con resumen de cita

✅ **Gestión Completa de Citas**
- Calendario interactivo
- Vista de lista (próximas y pasadas)
- Reprogramar, cancelar o confirmar citas
- Estados visuales con badges de colores

✅ **UX Excepcional**
- Validaciones en tiempo real
- SnackBars para feedback
- Diálogos de confirmación
- Loading states en todas las acciones
- Prevención de pérdida de datos

---

## 🏗️ Arquitectura

El proyecto sigue el patrón **MVVM (Model-View-ViewModel)** con separación clara de responsabilidades:

```
┌─────────────────┐
│      VIEW       │  ← Jetpack Compose (UI)
│   (Screens)     │
└────────┬────────┘
         │ observa StateFlow
         ▼
┌─────────────────┐
│   VIEWMODEL     │  ← Lógica de presentación
│                 │     Estados reactivos
└────────┬────────┘
         │ llama suspend functions
         ▼
┌─────────────────┐
│   REPOSITORY    │  ← Lógica de negocio
│                 │     Validaciones
└────────┬────────┘
         │ accede a datos
         ▼
┌─────────────────┐
│   DATA SOURCE   │  ← Datos simulados
│                 │     (preparado para API)
└─────────────────┘
```

### Estructura del Proyecto

```
app/src/main/java/com/project/mediturn/
├── MainActivity.kt
│
├── data/
│   ├── model/               # Modelos de datos
│   │   ├── Appointment.kt
│   │   ├── Doctor.kt
│   │   ├── Patient.kt
│   │   ├── Specialty.kt
│   │   └── TimeSlot.kt
│   ├── repository/          # Lógica de negocio
│   │   ├── AppointmentRepository.kt
│   │   └── DoctorRepository.kt
│   └── DataSource.kt        # Datos simulados
│
├── viewmodel/               # ViewModels
│   ├── AppointmentViewModel.kt
│   ├── AuthViewModel.kt
│   └── DoctorViewModel.kt
│
├── ui/
│   ├── screens/             # Pantallas principales
│   │   ├── auth/
│   │   │   ├── LoginScreen.kt
│   │   │   └── RegisterScreen.kt
│   │   ├── home/
│   │   │   └── HomeScreen.kt
│   │   ├── doctors/
│   │   │   ├── DoctorListScreen.kt
│   │   │   └── DoctorDetailScreen.kt
│   │   ├── appointments/
│   │   │   ├── BookAppointmentScreen.kt
│   │   │   ├── MyAppointmentsScreen.kt
│   │   │   └── AppointmentDetailScreen.kt
│   │   └── profile/
│   │       └── ProfileScreen.kt
│   │
│   ├── components/          # Componentes reutilizables
│   │   ├── SearchBar.kt
│   │   ├── DoctorCard.kt
│   │   ├── AppointmentCard.kt
│   │   ├── SpecialtyChip.kt
│   │   ├── TimeSlotButton.kt
│   │   └── EmptyState.kt
│   │
│   └── theme/               # Tema y estilos
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
│
├── navigation/              # Navegación
│   ├── NavGraph.kt
│   └── Routes.kt
│
└── util/                    # Utilidades
    ├── Constants.kt
    ├── DateUtils.kt
    └── ValidationUtils.kt
```

---

## 🛠️ Tecnologías Utilizadas

| Categoría | Tecnología |
|-----------|------------|
| **Lenguaje** | Kotlin 1.9.20 |
| **UI** | Jetpack Compose |
| **Diseño** | Material Design 3 |
| **Arquitectura** | MVVM + StateFlow |
| **Navegación** | Navigation Compose |
| **Concurrencia** | Kotlin Coroutines + Flow |
| **Imágenes** | Coil |
| **Gestión de Estado** | ViewModel + StateFlow |
| **Control de Versiones** | Git + GitHub |

---

## 📦 Dependencias Principales

```kotlin
// Jetpack Compose
implementation(platform("androidx.compose:compose-bom:2024.10.00"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.material:material-icons-extended")

// Navigation
implementation("androidx.navigation:navigation-compose:2.8.4")

// ViewModel
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

// Coil para imágenes
implementation("io.coil-kt:coil-compose:2.5.0")
```

---

## 🚀 Instalación y Ejecución

### Requisitos Previos

- Android Studio Hedgehog (2023.1.1) o superior
- JDK 11 o superior
- Android SDK 26+
- Emulador o dispositivo físico Android

### Pasos de Instalación

1. **Clonar el repositorio:**
```bash
git clone https://github.com/Casapaico/mediturn.git
cd mediturn
```

2. **Abrir en Android Studio:**
- Abrir Android Studio
- File → Open → Seleccionar carpeta del proyecto
- Esperar a que Gradle sincronice

3. **Ejecutar la aplicación:**
- Conectar dispositivo o iniciar emulador
- Click en "Run" (▶️) o presionar Shift + F10
- La app se instalará automáticamente

---

## 👥 Equipo de Desarrollo

| Avatar | Nombre | Rol | GitHub |
|--------|--------|-----|--------|
| 👨‍💻 | **Alex Luis Casapaico Aquino** | Líder Técnico & Diseñador UI | [@Casapaico](https://github.com/Casapaico) |
| 👩‍💻 | **Gabriela Soto Huaman** | Tester & Documentador | [@GabrielaSoto](https://github.com/GabrielaSoto) |

### División de Responsabilidades

**Alex Casapaico** (Líder Técnico):
- Arquitectura MVVM
- ViewModels y Repositorios
- UI en Jetpack Compose
- Sistema de navegación
- Integración de componentes

**Gabriela Soto** (Tester/Documentador):
- Testing QA exhaustivo
- Validaciones de formularios
- Documentación técnica
- Capturas de pantalla
- Video demo y presentación

---

## 📊 Estadísticas del Proyecto

```
┌───────────────────────────────────┐
│   MEDITURN - ESTADÍSTICAS         │
├───────────────────────────────────┤
│ Duración:           6 días        │
│ Archivos:           35            │
│ Líneas de código:   ~3,500        │
│ Pantallas:          10            │
│ Componentes:        6             │
│ ViewModels:         3             │
│ Repositorios:       2             │
│ Validaciones:       15+           │
│ Tests manuales:     100+          │
└───────────────────────────────────┘
```

---

## 🎨 Diseño

El diseño de la aplicación fue realizado en **Figma** siguiendo las guías de Material Design 3.

**Ver Diseño Completo:**  
👉 [Figma - MediTurn](https://www.figma.com/design/MAJvvF2McJeoIIlhNaqIqU/mediturn)

### Paleta de Colores

| Color | Hex | Uso |
|-------|-----|-----|
| Primary (Azul Médico) | `#2196F3` | Botones, headers |
| Secondary (Verde) | `#4CAF50` | Acciones secundarias |
| Tertiary (Azul Claro) | `#BBDEFB` | Chips, estados |
| Error | `#E53935` | Alertas, errores |

---

## 📖 Funcionalidades Detalladas

### 1. Autenticación
- Login simulado (desarrollo)
- Registro con validaciones de email, teléfono y DNI
- Gestión de sesión

### 2. Búsqueda de Médicos
- **Búsqueda Reactiva:** Resultados instantáneos mientras escribes
- **Filtros Múltiples:**
  - Por especialidad (8 especialidades)
  - Por ciudad (Lima, Callao)
  - Por tipo de consulta (presencial/teleconsulta)
- **Contador de Resultados:** "X médico(s) encontrado(s)"
- **Badges:** Indicadores visuales de filtros activos

### 3. Perfil del Médico
- Información completa: nombre, especialidad, experiencia
- Calificación con estrellas (0-5) y número de reseñas
- Precio de consulta
- Disponibilidad para teleconsulta
- Ciudad y departamento
- Colegiatura médica (CMP)

### 4. Agendar Cita
- **Validaciones en Tiempo Real:**
  - Especialidad requerida
  - Médico requerido
  - Fecha válida (no pasada, máximo 7 días)
  - Hora disponible
  - Motivo (10-200 caracteres)
- **Confirmación:** Diálogo con resumen de la cita
- **Feedback:** SnackBar de éxito tras agendar
- **Navegación:** Redirección automática a "Mis Citas"

### 5. Mis Citas
- **Vista Calendario:** Interactivo con indicadores de citas
- **Vista Lista:** Próximas y pasadas separadas
- **Estados de Cita:**
  - 🟡 Pendiente
  - 🟢 Confirmada
  - 🔵 Completada
  - 🔴 Cancelada

### 6. Gestión de Citas
- **Reprogramar:** Cambiar fecha/hora con validaciones
- **Cancelar:** Confirmación antes de cancelar
- **Confirmar:** Confirmar citas pendientes
- **Validaciones:** Solo citas futuras pueden modificarse

---

## 🧪 Testing

### Testing Manual Realizado

✅ **Flujos Principales:**
- Login → Home → Búsqueda → Detalle → Agendar → Mis Citas ✅
- Home → Agendar Cita → Selección Completa → Confirmación ✅
- Búsqueda con Filtros → Limpiar Filtros → Nueva Búsqueda ✅

✅ **Validaciones:**
- Formulario incompleto → Botón deshabilitado ✅
- Motivo corto → Mensaje de error ✅
- Fecha pasada → Validación ✅
- Salir con cambios → Diálogo de confirmación ✅

✅ **Navegación:**
- Botón atrás → Funciona correctamente ✅
- Navegación profunda → Sin pérdida de estado ✅
- Backstack limpio → Navegación fluida ✅

✅ **UI/UX:**
- Loading states → Visibles en todas las acciones ✅
- SnackBars → Mensajes claros ✅
- Diálogos → Confirmaciones apropiadas ✅
- Filtros → Badges con contadores ✅

---

## 🐛 Problemas Conocidos

Actualmente no hay bugs conocidos. Si encuentras alguno, por favor [reportarlo aquí](https://github.com/Casapaico/mediturn/issues).

---

## 🔮 Roadmap

### v1.1 (Futuro)
- [ ] Integración con API REST real
- [ ] Autenticación JWT
- [ ] Notificaciones push
- [ ] Recordatorios de citas

### v2.0 (Largo plazo)
- [ ] Perfil de paciente con historial médico
- [ ] Chat con el médico
- [ ] Recetas médicas digitales
- [ ] Pagos en línea
- [ ] Geolocalización de consultorios

---

## 📄 Licencia

Este proyecto es parte de un trabajo académico para el curso de **Aplicaciones Móviles con Android** en Tecsup.

**Institución:** Tecsup  
**Docente:** Juan León  
**Fecha:** Octubre 2025  

---

## 🎓 Agradecimientos

- **Profesor Juan León** - Por la guía y apoyo durante el proyecto
- **Tecsup** - Por proporcionar los recursos y el ambiente de aprendizaje
- **Android Developers Community** - Por la documentación y tutoriales

---

## 📞 Contacto

**Repositorio:** [github.com/Casapaico/mediturn](https://github.com/Casapaico/mediturn)  
**Diseño Figma:** [Ver Diseño](https://www.figma.com/design/MAJvvF2McJeoIIlhNaqIqU/mediturn)  
**Video Demo:** [Ver Demo en YouTube](#) *(Por subir)*

---

## ⭐ Si te gustó este proyecto

Dale una estrella ⭐ en GitHub y compártelo con la comunidad de Android Developers.

---

<div align="center">

**🚀 MediTurn - Tu salud en buenas manos**

Desarrollado con ❤️ por Alex Casapaico & Gabriela Soto

</div>
