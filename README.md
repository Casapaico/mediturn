# 🏥 MediTurn - Sistema de Citas Médicas

## 📱 Descripción
MediTurn es una aplicación móvil desarrollada en Android con Kotlin y Jetpack Compose que permite a los pacientes buscar médicos por especialidad, agendar citas, gestionar su calendario médico y recibir recordatorios de sus consultas.

## 👥 Equipo de Desarrollo

| Nombre | Rol |
|--------|-----|
| **Alex Luis Casapaico Aquino** | Líder Técnico y Diseñador UI |
| **Gabriela Soto Huaman** | Tester/Documentador |

## 🎯 Alcance del Proyecto

### Funcionalidades Principales
- ✅ Búsqueda de médicos por especialidad, nombre y ciudad
- ✅ Visualización de perfil detallado del médico
- ✅ Agenda de citas con selección de fecha y hora
- ✅ Gestión de citas: crear, reprogramar y cancelar
- ✅ Calendario personal del paciente
- ✅ Sistema de recordatorios
- ✅ Soporte para teleconsultas

## 📋 Historias de Usuario

1. **Como paciente**, quiero buscar médicos por especialidad para encontrar el profesional adecuado para mi condición médica.

2. **Como paciente**, quiero ver el perfil completo del médico (experiencia, calificaciones, disponibilidad) antes de agendar una cita.

3. **Como paciente**, quiero agendar una cita seleccionando fecha y hora disponible para planificar mi consulta.

4. **Como paciente**, quiero ver todas mis citas próximas en un calendario para organizarme mejor.

5. **Como paciente**, quiero reprogramar o cancelar una cita si tengo algún impedimento para asistir.

6. **Como paciente**, quiero recibir recordatorios de mis citas próximas para no olvidarlas.

## 🎨 Prototipo de Diseño

**Figma:** https://www.figma.com/design/MAJvvF2McJeoIIlhNaqIqU/mediturn?node-id=0-1&t=XeP79zOqivzjcgPe-0

### Pantallas Diseñadas
- 🏠 Home
- 🔍 Búsqueda y Listado de Médicos
- 👨‍⚕️ Detalle de Médico
- 📅 Agendar Cita
- 📋 Mis Citas
- 👤 Perfil de Usuario

## 🛠️ Tecnologías

- **Lenguaje:** Kotlin
- **Framework UI:** Jetpack Compose
- **Navegación:** Navigation Compose
- **Arquitectura:** MVVM (en desarrollo)
- **IDE:** Android Studio
- **Control de versiones:** Git/GitHub
- **Diseño:** Figma
- **Imágenes:** Coil Compose
- **API Level:** 26+ (Android 8.0 Oreo+)

## 📂 Estructura del Proyecto
```
app/src/main/java/com/project/mediturn/
├── MainActivity.kt                    # Punto de entrada
├── data/
│   ├── model/                         # Modelos de datos
│   │   ├── Doctor.kt                  # Modelo de médico
│   │   ├── Appointment.kt             # Modelo de cita
│   │   ├── Patient.kt                 # Modelo de paciente
│   │   ├── TimeSlot.kt                # Modelo de horario
│   │   └── Specialty.kt               # Modelo de especialidad
│   └── DataSource.kt                  # Datos simulados
├── navigation/
│   ├── NavGraph.kt                    # Configuración de rutas
│   └── Routes.kt                      # Definición de pantallas
├── ui/
│   ├── components/                    # Componentes reutilizables
│   │   ├── SearchBar.kt               # Barra de búsqueda
│   │   ├── DoctorCard.kt              # Card de médico
│   │   ├── EmptyState.kt              # Estado vacío
│   │   ├── AppointmentCard.kt         # Card de cita
│   │   ├── SpecialtyChip.kt           # Chip de especialidad
│   │   └── TimeSlotButton.kt          # Botón de horario
│   ├── screens/                       # Pantallas de la app
│   │   ├── auth/                      # Login y Registro
│   │   │   ├── LoginScreen.kt
│   │   │   └── RegisterScreen.kt
│   │   ├── home/                      # Pantalla principal
│   │   │   └── HomeScreen.kt
│   │   ├── doctors/                   # Búsqueda y detalle
│   │   │   ├── DoctorListScreen.kt
│   │   │   └── DoctorDetailScreen.kt
│   │   ├── appointments/              # Gestión de citas
│   │   │   ├── BookAppointmentScreen.kt
│   │   │   ├── MyAppointmentsScreen.kt
│   │   │   └── AppointmentDetailScreen.kt
│   │   └── profile/                   # Perfil del usuario
│   │       └── ProfileScreen.kt
│   └── theme/                         # Colores y estilos
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
└── viewmodel/                         # (Próximo: lógica de negocio)
```

## 🚀 Estado del Proyecto

**Versión actual:** v1.0.0-day3 (Día 3 - UI/UX Completo)

### Cronograma
- ✅ **Día 1:** Planificación y Diseño (Figma)
- ✅ **Día 2:** Configuración del Proyecto y Estructura Base
- ✅ **Día 3:** Desarrollo de Interfaz (UI/UX) ← **COMPLETADO ✨**
- ⏳ **Día 4:** Lógica y Datos Simulados
- ⏳ **Día 5:** Funcionalidades Clave y Pulido
- ⏳ **Día 6:** Presentación Final y Documentación

### Progreso Día 3 ✨
- ✅ **6 componentes reutilizables** creados y funcionales
- ✅ **DataSource completo** con 6 médicos, 8 especialidades, 3 citas
- ✅ **HomeScreen** con saludo personalizado y 4 botones de acción
- ✅ **DoctorListScreen** con búsqueda en tiempo real y filtros por especialidad
- ✅ **DoctorDetailScreen** con perfil completo, rating y servicios
- ✅ **BookAppointmentScreen** con selector de fecha/hora y validación
- ✅ **MyAppointmentsScreen** con tabs (Próximas/Pasadas) y estados
- ✅ **Material Design 3** aplicado consistentemente
- ✅ **Navegación completa** entre todas las pantallas (11 rutas)
- ✅ **Estados vacíos** implementados en todas las listas
- ✅ **Imágenes con Coil** funcionando correctamente
- ✅ **Validación de formularios** en BookAppointmentScreen

## 📝 Instalación y Uso

### Requisitos Previos
- Android Studio Hedgehog (2023.1.1) o superior
- JDK 11 o superior
- Android SDK API 26+ (Android 8.0 Oreo+)
- Dispositivo físico o emulador con Android 8.0+

### Pasos de Instalación
```bash
# 1. Clonar el repositorio
git clone https://github.com/Casapaico/mediturn.git

# 2. Abrir en Android Studio
# File > Open > Seleccionar carpeta del proyecto

# 3. Verificar minSdk en build.gradle.kts
# android { defaultConfig { minSdk = 26 } }

# 4. Sync Gradle
# Android Studio lo hará automáticamente

# 5. Agregar permiso de Internet (si no existe)
# En AndroidManifest.xml:
# <uses-permission android:name="android.permission.INTERNET" />

# 6. Ejecutar en emulador o dispositivo
# Run > Run 'app'
```

### Configurar Dispositivo Físico
1. Habilitar **Modo Desarrollador** en tu dispositivo Android
2. Activar **Depuración USB**
3. Conectar dispositivo por USB
4. Seleccionar dispositivo en Android Studio
5. Click en ▶️ Run

## 🎨 Paleta de Colores

```kotlin
// Colores principales
val MedicalBlue = Color(0xFF2196F3)      // Azul Médico
val MedicalGreen = Color(0xFF4CAF50)     // Verde Salud
val MedicalTeal = Color(0xFF00897B)      // Teal Profesional

// Colores secundarios
val LightBlue = Color(0xFFBBDEFB)        // Azul Claro
val LightGreen = Color(0xFFC8E6C9)       // Verde Claro

// Estados
val ErrorRed = Color(0xFFE53935)         // Rojo Error
val WarningOrange = Color(0xFFFB8C00)    // Naranja Advertencia
val SuccessGreen = Color(0xFF43A047)     // Verde Éxito

// Neutros
val BackgroundGray = Color(0xFFF5F5F5)   // Gris Fondo
val SurfaceWhite = Color(0xFFFFFFFF)     // Blanco Superficie
val TextPrimary = Color(0xFF212121)      // Texto Principal
val TextSecondary = Color(0xFF757575)    // Texto Secundario
```

## 🔄 Flujo de Navegación
```
Login ────────────────────────┐
  │                            │
  ↓                            │
Home ──┬─→ Búsqueda ──→ Detalle ──→ Agendar ──┐
  │    │                                       │
  │    ├─→ Mis Citas ──→ Detalle de Cita      │
  │    │                     ↓                 │
  │    ├─→ Perfil        Reprogramar          │
  │    │                     ↓                 │
  │    └─→ Agendar      Cancelar              │
  │                                            │
  └────────────────────────────────────────────┘
```

## ✨ Características Implementadas

### 🏠 HomeScreen
- **Saludo personalizado** con nombre del paciente extraído de DataSource
- **4 botones principales** en grid 2x2:
  - 👤 Mi Perfil (Tertiario)
  - 🔍 Búsqueda (Primario)
  - ➕ Agendar Cita (Secundario)
  - 📅 Mis Citas (Error - destaca)
- **Card informativa** con icono de ayuda
- **Diseño centrado** y espaciado Material Design 3

### 🔍 DoctorListScreen
- **Barra de búsqueda** en tiempo real con icono de lupa
- **Botón de filtros** con badge mostrando cantidad de filtros activos
- **Dialog de filtros** con checkboxes por especialidad (8 opciones)
- **Card de filtros activos** con botón "Limpiar"
- **LazyColumn** con 6 médicos:
  - Foto circular (80dp) cargada con Coil
  - Nombre, especialidad, experiencia
  - Rating con estrellas y conteo de reviews
  - Badge "Video" si tiene teleconsulta
  - Precio destacado en color primario
- **Estado vacío** con emoji 🔍 cuando no hay resultados
- **HorizontalDivider** separando búsqueda de resultados

### 👨‍⚕️ DoctorDetailScreen
- **Header grande** con foto del médico (120dp)
- **Información profesional:**
  - Nombre y especialidad
  - Rating con estrellas (⭐ 4.8)
  - Años de experiencia
  - Colegiatura (CMP)
  - Ciudad
- **Descripción completa** del médico en Card
- **Services Cards:**
  - 🏥 Consulta Presencial
  - 📹 Teleconsulta (si disponible)
- **Precio destacado** en Card separada
- **Badge de disponibilidad** (7 días)
- **Bottom bar** con botón "Agendar Cita"

### 📅 BookAppointmentScreen
- **Card del médico** con foto mini y datos básicos
- **Selector de fecha** visual (7 días):
  - Día de la semana
  - Número del día
  - Mes abreviado
  - Selección con color primario
- **Grid de horarios** 3x3:
  - Mañana (9:00-12:00)
  - Tarde (15:00-18:00)
  - Estados: disponible/ocupado/seleccionado
- **Campo de motivo** de consulta (TextField multilinea)
- **Toggle de tipo:** Presencial/Virtual
- **Card de resumen** con toda la info de la cita
- **Validación completa:**
  - Fecha requerida
  - Hora requerida
  - Motivo mínimo 10 caracteres
  - Botón "Confirmar" solo habilitado si todo está completo
- **ScrollState** para pantallas pequeñas

### 📋 MyAppointmentsScreen
- **TabRow** con 2 tabs:
  - 🔜 Próximas (2 citas)
  - ✅ Pasadas (1 cita)
- **Citas filtradas** por fecha actual
- **AppointmentCard** con:
  - Foto del médico (60dp)
  - Nombre y especialidad
  - Fecha formateada ("25 Oct, 2024")
  - Hora formateada ("10:00 AM")
  - Motivo de consulta
  - Tipo: Presencial/Teleconsulta con icono
  - Badge de estado con colores:
    - 🟡 Pendiente (Warning)
    - 🟢 Confirmada (Success)
    - ⚪ Completada (Surface)
    - 🔴 Cancelada (Error)
- **Estado vacío** personalizado por tab:
  - "No tienes citas próximas" (Próximas)
  - "No tienes historial" (Pasadas)

### 👤 ProfileScreen
- **En desarrollo** (placeholder)
- Navegación funcional desde Home

## 📦 Dependencias Principales
```kotlin
android {
    compileSdk = 36
    defaultConfig {
        minSdk = 26  // Android 8.0+ para java.time
        targetSdk = 36
    }
}

dependencies {
    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.10.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.4")

    // Lifecycle & ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Coil para imágenes
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Retrofit (preparado para futuro)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
}
```

## 🎯 Componentes Reutilizables

| Componente | Descripción | Props Principales |
|------------|-------------|-------------------|
| **SearchBar** | Barra de búsqueda con clear | `query`, `onQueryChange`, `placeholder` |
| **DoctorCard** | Card completa de médico | `doctor: Doctor`, `onClick` |
| **EmptyState** | Estado vacío personalizado | `icon`, `title`, `message` |
| **AppointmentCard** | Card de cita | `appointment: Appointment`, `onClick` |
| **SpecialtyChip** | Chip de especialidad | `specialty: String`, `isSelected`, `onClick` |
| **TimeSlotButton** | Botón de horario | `time: String`, `isAvailable`, `isSelected` |

## 👨‍💻 Convención de Commits

Seguimos **Conventional Commits** para mantener un historial limpio:

```
feat: nueva funcionalidad
fix: corrección de bug
docs: cambios en documentación
style: formato, estilos (sin cambios de código)
refactor: refactorización sin cambiar funcionalidad
test: añadir o modificar tests
chore: tareas de mantenimiento
```

### Ejemplos del Proyecto:

**Día 1-2 (Estructura):**
SOTO:
```bash
git commit -m "feat: configuración inicial del proyecto Mediturn"
git commit -m "feat: implementar modelos de datos principales: Doctor.kt con especialidad y horarios, Appointment.kt con estados de cita, Patient.kt con datos del paciente, TimeSlot.kt para gestión de horarios, Specialty.kt para especialidades médicas"
git commit -m "feat: implementar sistema de navegación con rutas, estructura y parámetros"
git commit -m "feat: crear estructura base de pantallas con rutas, parámetros y composables"
git commit -m "feat: crear paleta de colores médicos con primarios, estados y esquema neutro"
```

CASAPAICO:
```bash
git commit -m "screens"
git commit -m "corrección Theme"
```

**Día 3 (UI/UX Completo):**
```bash
git commit -m "feat(day3): complete UI implementation - all screens functional

SCREENS UPDATED:
- HomeScreen: added 4 action buttons grid, personalized greeting
- DoctorListScreen: search bar, filters dialog, badge counter
- DoctorDetailScreen: complete profile, services cards, bottom bar
- BookAppointmentScreen: date/time picker, validation, summary card
- MyAppointmentsScreen: tabs (upcoming/past), filtered appointments

COMPONENTS CREATED:
- SearchBar.kt: real-time search with clear button
- DoctorCard.kt: photo, rating, experience, price, telemedicine badge
- EmptyState.kt: customizable empty states for all lists
- AppointmentCard.kt: appointment info with status badge
- TimeSlotButton.kt: available/occupied/selected states
- SpecialtyChip.kt: selectable specialty chips

DATA:
- DataSource.kt: 6 doctors, 8 specialties, 3 appointments
- TimeSlots generation for next 7 days
- Search and filter functions

FEATURES:
- Material Design 3 applied consistently
- Image loading with Coil
- Form validation in BookAppointmentScreen
- Navigation between all 11 screens
- Empty states for all lists
- Date/time formatting with java.time

FIXES:
- minSdk updated to 26 for java.time support
- Internet permission added to manifest
- All imports corrected

STATUS: Day 3 objectives 100% complete ✅"
```

## 🐛 Solución de Problemas

### ❌ Error: "Call requires API level 26"
**Causa:** `java.time.LocalDateTime` requiere API 26+

**Solución:**
```kotlin
// En app/build.gradle.kts
android {
    defaultConfig {
        minSdk = 26  // Cambiar de 24 a 26
    }
}
```

### ❌ Error: "Unresolved reference: components"
**Causa:** Carpeta `ui/components/` no existe

**Solución:**
1. Click derecho en `ui/`
2. New > Package
3. Nombre: `components`
4. Copiar los 6 archivos de componentes

### ❌ Las imágenes no cargan
**Causa:** Falta permiso de INTERNET

**Solución:**
```xml
<!-- En AndroidManifest.xml -->
<manifest>
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <application>
        ...
    </application>
</manifest>
```

### ❌ App crashea al presionar "BÚSQUEDA"
**Causas posibles:**
1. Componentes no están en `/ui/components/`
2. Falta permiso de INTERNET
3. Import incorrecto de DataSource

**Solución:**
1. Verificar estructura de carpetas
2. Agregar permisos al manifest
3. Verificar imports: `import com.project.mediturn.data.DataSource`

### ❌ Build falla
**Solución:**
```
1. Build > Clean Project
2. Build > Rebuild Project
3. File > Invalidate Caches > Invalidate and Restart
4. Sync Project with Gradle Files
```

## 📊 Datos Simulados

### 👨‍⚕️ Médicos (6)
1. **Dr. Carlos Mendoza** - Cardiología (⭐4.8, 245 reviews, S/150)
2. **Dra. María Fernández** - Dermatología (⭐4.9, 312 reviews, S/120)
3. **Dr. Juan Pérez** - Pediatría (⭐4.7, 189 reviews, S/100)
4. **Dra. Ana Torres** - Neurología (⭐4.9, 278 reviews, S/180)
5. **Dr. Roberto Sánchez** - Traumatología (⭐4.6, 156 reviews, S/200)
6. **Dra. Patricia Rojas** - Oftalmología (⭐4.8, 203 reviews, S/140)

### 🏥 Especialidades (8)
❤️ Cardiología | 🧴 Dermatología | 👶 Pediatría | 🧠 Neurología  
🦴 Traumatología | 👁️ Oftalmología | 🌸 Ginecología | 🧘 Psiquiatría

### 📅 Citas (3)
- **Próximas:**
  1. Dr. Carlos Mendoza - 2 días (Confirmada)
  2. Dra. María Fernández - 5 días (Pendiente)
- **Pasadas:**
  1. Dra. Ana Torres - hace 10 días (Completada)

### ⏰ TimeSlots
- **Horarios generados:** 7 días siguientes
- **Turnos mañana:** 9:00 - 12:00 (slots cada 30 min)
- **Turnos tarde:** 15:00 - 18:00 (slots cada 30 min)
- **Disponibilidad:** Simulada con algoritmo (70% disponibles)

## 🧪 Testing Manual

### ✅ Flujo Completo Funcional
```
1. Login → Home ✅
2. Click "BÚSQUEDA" → Lista de 6 médicos ✅
3. Buscar "Carlos" → Filtrado correcto ✅
4. Click filtro → Dialog con 8 especialidades ✅
5. Seleccionar "Cardiología" → Badge muestra "1" ✅
6. Ver 1 resultado (Dr. Carlos) ✅
7. Click en médico → Detalle completo ✅
8. Click "Agendar Cita" → Formulario ✅
9. Seleccionar fecha → Horarios filtrados ✅
10. Seleccionar hora → Card de resumen ✅
11. Escribir motivo → Validación en tiempo real ✅
12. Confirmar → Navega a Mis Citas ✅
13. Ver cita en tab "Próximas" ✅
```

### 🎯 Pruebas de Validación
- ❌ Confirmar sin fecha → Botón deshabilitado ✅
- ❌ Confirmar sin hora → Botón deshabilitado ✅
- ❌ Confirmar sin motivo → Botón deshabilitado ✅
- ❌ Motivo < 10 caracteres → Botón deshabilitado ✅
- ✅ Todos los campos completos → Botón habilitado ✅

### 🔍 Pruebas de Búsqueda y Filtros
- Buscar "Dr" → 3 resultados (todos los "Dr.") ✅
- Buscar "María" → 1 resultado ✅
- Buscar "Cardiología" → 1 resultado ✅
- Filtrar por "Pediatría" → 1 resultado ✅
- Filtrar múltiples especialidades → OR logic ✅
- Limpiar filtros → Todos los médicos ✅
- Buscar texto inexistente → Estado vacío ✅

### 📅 Pruebas de Calendario
- Ver tab "Próximas" → 2 citas ✅
- Ver tab "Pasadas" → 1 cita ✅
- Citas ordenadas por fecha ✅
- Estados correctos por tipo ✅
- Formato de fecha/hora correcto ✅

## 📸 Capturas de Pantalla

*(Agregar screenshots aquí después de Day 3)*

1. **HomeScreen** - 4 botones en grid 2x2
2. **DoctorListScreen** - Búsqueda y filtros
3. **DoctorListScreen** - Dialog de filtros con checkboxes
4. **DoctorDetailScreen** - Perfil completo del médico
5. **BookAppointmentScreen** - Selector de fecha y hora
6. **BookAppointmentScreen** - Card de resumen
7. **MyAppointmentsScreen** - Tab Próximas
8. **MyAppointmentsScreen** - Tab Pasadas

## 📄 Licencia

Proyecto académico - **Tecsup**  
Curso: **Aplicaciones Móviles con Android** (Kotlin + Jetpack Compose)  
Docente: **Juan León**  
Modalidad: Trabajo colaborativo (equipos de 2 estudiantes)

---

## 🎯 Próximos Pasos (Día 4)

### Objetivos Día 4:
- [ ] Implementar ViewModels (MVVM)
- [ ] Crear DoctorViewModel con StateFlow
- [ ] Crear AppointmentViewModel
- [ ] Agregar estados de carga (Loading, Success, Error)
- [ ] Implementar repositorios locales
- [ ] Funciones CRUD para citas (crear, editar, cancelar)
- [ ] Validación de disponibilidad en tiempo real
- [ ] Manejo de errores con SnackBar
- [ ] Agregar confirmaciones con AlertDialog

### Funcionalidades Día 4:
- Crear cita real que persista en DataSource
- Reprogramar cita existente
- Cancelar cita con confirmación
- Validar horarios ocupados
- Estados de carga en todas las pantallas
- Manejo de errores

---

**Última actualización:** 24 de octubre de 2025  
**Versión:** v1.0.0-day3  
**Estado:** ✅ Día 3 completado al 100%  
**Próximo hito:** Día 4 - Lógica y ViewModels

## 📧 Contacto

**GitHub:** [github.com/Casapaico/mediturn](https://github.com/Casapaico/mediturn)  
**Figma:** [Diseño MediTurn](https://www.figma.com/design/MAJvvF2McJeoIIlhNaqIqU/mediturn)

---

### 🌟 Agradecimientos

Agradecemos al docente **Juan León** por la guía durante el desarrollo del proyecto, y a **Tecsup** por proporcionar los recursos y el ambiente de aprendizaje para completar este proyecto.

---

**🚀 MediTurn - Tu salud en buenas manos**