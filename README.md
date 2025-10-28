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
- ✅ **Arquitectura MVVM con ViewModels** (Nuevo Día 4)
- ✅ **Validaciones en tiempo real** (Nuevo Día 4)
- ✅ **Repositorios con simulación de red** (Nuevo Día 4)

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
- **Arquitectura:** MVVM ✨
- **Gestión de Estado:** StateFlow & SharedFlow ✨
- **Concurrencia:** Kotlin Coroutines ✨
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
│   ├── repository/                    # ✨ Repositorios (Nuevo Día 4)
│   │   ├── DoctorRepository.kt        # Lógica de médicos
│   │   └── AppointmentRepository.kt   # Lógica de citas
│   ├── remote/                        # Para futuro API
│   │   └── ApiService.kt              # (Preparado)
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
├── util/                              # ✨ Utilidades (Nuevo Día 4)
│   ├── Constants.kt                   # Constantes de la app
│   ├── DateUtils.kt                   # Utilidades de fecha
│   └── ValidationUtils.kt             # Validaciones
└── viewmodel/                         # ✨ ViewModels (Nuevo Día 4)
    ├── DoctorViewModel.kt             # Lógica de médicos
    ├── AppointmentViewModel.kt        # Lógica de citas
    └── AuthViewModel.kt               # Lógica de autenticación
```

## 🚀 Estado del Proyecto

**Versión actual:** v1.0.0-day4 (Día 4 - Lógica y ViewModels) ✨

### Cronograma
- ✅ **Día 1:** Planificación y Diseño (Figma)
- ✅ **Día 2:** Configuración del Proyecto y Estructura Base
- ✅ **Día 3:** Desarrollo de Interfaz (UI/UX)
- ✅ **Día 4:** Lógica y Datos Simulados ← **COMPLETADO ✨**
- ⏳ **Día 5:** Funcionalidades Clave y Pulido
- ⏳ **Día 6:** Presentación Final y Documentación

### Progreso Día 4 ✨

#### 🏗️ Arquitectura MVVM Completa
- ✅ **3 ViewModels** creados con StateFlow
- ✅ **2 Repositorios** con simulación de latencia de red
- ✅ **3 Utils** con funciones reutilizables
- ✅ **Manejo de estados** (Loading, Success, Error)
- ✅ **Result<T>** para manejo robusto de errores
- ✅ **Kotlin Coroutines** para operaciones asíncronas

#### 📦 Repositorios (2 archivos, ~350 líneas)

**DoctorRepository.kt** (130 líneas)
- ✅ `getAllDoctors()` - Obtener todos los médicos
- ✅ `searchDoctors()` - Búsqueda con filtros
- ✅ `getDoctorById()` - Detalle de médico
- ✅ `getDoctorsBySpecialty()` - Filtrar por especialidad
- ✅ `searchDoctorsFlow()` - Búsqueda reactiva en tiempo real
- ✅ `checkDoctorAvailability()` - Verificar disponibilidad
- ✅ Simulación de latencia de red (300-800ms)

**AppointmentRepository.kt** (220 líneas)
- ✅ `createAppointment()` - Crear nueva cita
- ✅ `getPatientAppointments()` - Todas las citas del paciente
- ✅ `getUpcomingAppointments()` - Próximas citas
- ✅ `getPastAppointments()` - Historial de citas
- ✅ `getAppointmentById()` - Detalle de cita
- ✅ `rescheduleAppointment()` - Reprogramar cita
- ✅ `cancelAppointment()` - Cancelar cita
- ✅ `confirmAppointment()` - Confirmar cita
- ✅ `completeAppointment()` - Completar cita
- ✅ `canReschedule()` - Validar si puede reprogramar
- ✅ `canCancel()` - Validar si puede cancelar
- ✅ Validación de disponibilidad de slots
- ✅ Actualización en tiempo real de DataSource

#### 🎯 ViewModels (3 archivos, ~615 líneas)

**DoctorViewModel.kt** (135 líneas)
- ✅ Estados: Loading, Success, DoctorDetail, Error
- ✅ `loadAllDoctors()` - Cargar lista completa
- ✅ `searchDoctors()` - Búsqueda con filtros
- ✅ `updateSearchQuery()` - Actualizar búsqueda en tiempo real
- ✅ `toggleSpecialty()` - Filtrar por especialidad
- ✅ `clearFilters()` - Limpiar todos los filtros
- ✅ `loadDoctorById()` - Cargar detalle de médico
- ✅ Debounce automático (300ms) en búsqueda
- ✅ StateFlow para estados reactivos

**AppointmentViewModel.kt** (280 líneas)
- ✅ Estados: Loading, Processing, AppointmentsList, AppointmentDetail, Success, Error
- ✅ `loadAppointments()` - Cargar citas del paciente
- ✅ `loadAppointmentById()` - Cargar detalle de cita
- ✅ `updateSelectedDoctor()` - Actualizar médico seleccionado
- ✅ `updateSelectedDateTime()` - Actualizar fecha/hora
- ✅ `updateReason()` - Actualizar motivo de consulta
- ✅ `toggleTelemedicine()` - Toggle tipo de consulta
- ✅ `clearForm()` - Limpiar formulario de cita
- ✅ `isFormValid()` - Validar formulario completo
- ✅ `createAppointment()` - Crear cita nueva
- ✅ `rescheduleAppointment()` - Reprogramar cita existente
- ✅ `cancelAppointment()` - Cancelar cita con validación
- ✅ `confirmAppointment()` - Confirmar cita pendiente
- ✅ `canCancel()` - Validar si puede cancelar
- ✅ `canReschedule()` - Validar si puede reprogramar
- ✅ StateFlow + SharedFlow para mensajes

**AuthViewModel.kt** (200 líneas)
- ✅ Estados: Idle, Loading, Success, Error
- ✅ `updateEmail()` - Actualizar email
- ✅ `updatePassword()` - Actualizar contraseña
- ✅ `updateName()` - Actualizar nombre completo
- ✅ `updatePhone()` - Actualizar teléfono
- ✅ `updateDNI()` - Actualizar DNI
- ✅ `login()` - Login con validación completa
- ✅ `quickLogin()` - Login rápido (temporal)
- ✅ `register()` - Registro con validación
- ✅ `logout()` - Cerrar sesión
- ✅ `clearLoginForm()` - Limpiar formulario de login
- ✅ `clearRegisterForm()` - Limpiar formulario de registro
- ✅ `isLoginFormValid()` - Validar formulario de login
- ✅ `isRegisterFormValid()` - Validar formulario de registro
- ✅ Preparado para integración con API real

#### 🛠️ Utils (3 archivos, ~480 líneas)

**Constants.kt** (95 líneas)
- ✅ Información de la app (nombre, versión)
- ✅ Formatos de fecha y hora
- ✅ Restricciones de citas (mínimo 2h anticipación)
- ✅ Horarios de trabajo (9-12h, 15-18h)
- ✅ Duración de slots (30 minutos)
- ✅ Configuración de búsqueda (debounce 300ms)
- ✅ URLs para futuro API
- ✅ Mensajes de validación estandarizados
- ✅ Mensajes de éxito y confirmación
- ✅ Icons de especialidades (emojis)
- ✅ Valores por defecto
- ✅ Keys de SharedPreferences

**DateUtils.kt** (190 líneas)
- ✅ **18+ funciones de utilidad** para manejo de fechas
- ✅ `formatFullDate()` - "25 Oct 2024"
- ✅ `formatShortDate()` - "25/10/2024"
- ✅ `formatTime()` - "10:30"
- ✅ `formatDateTime()` - "25/10/2024 10:30"
- ✅ `formatDayOfWeek()` - "Lunes"
- ✅ `formatShortDayOfWeek()` - "Lun"
- ✅ `formatRelativeDate()` - "Hoy", "Mañana", "En 2 días"
- ✅ `isPastDate()` - Verificar si la fecha es pasada
- ✅ `isPastDateTime()` - Verificar si datetime es pasado
- ✅ `isWithinBookingRange()` - Verificar rango válido de reserva
- ✅ `hasMinimumAdvance()` - Verificar anticipación mínima
- ✅ `isWorkingHour()` - Verificar horario laboral
- ✅ `generateNextDays()` - Generar próximos N días
- ✅ `generateTimeSlots()` - Generar slots de horario
- ✅ `getTimeUntilAppointment()` - Tiempo relativo hasta cita
- ✅ `isSameDay()` - Comparar si son el mismo día
- ✅ `getStartOfDay()` - Obtener inicio del día
- ✅ `getEndOfDay()` - Obtener fin del día
- ✅ Locale español configurado
- ✅ Formato relativo de fechas

**ValidationUtils.kt** (195 líneas)
- ✅ **15+ funciones de validación**
- ✅ `isValidEmail()` - Validar formato de email
- ✅ `validateEmail()` - Validar email completo con mensaje
- ✅ `isValidPhone()` - Validar formato de teléfono
- ✅ `validatePhone()` - Validar teléfono completo
- ✅ `isValidDNI()` - Validar DNI peruano (8 dígitos)
- ✅ `validateDNI()` - Validar DNI completo
- ✅ `validateReason()` - Validar motivo de consulta (10-200 chars)
- ✅ `validateDate()` - Validar fecha seleccionada
- ✅ `validateDateTime()` - Validar fecha y hora completa
- ✅ `validateAppointmentForm()` - Validar formulario completo
- ✅ `validateNotEmpty()` - Campo no vacío
- ✅ `validateMinLength()` - Longitud mínima
- ✅ `validateMaxLength()` - Longitud máxima
- ✅ `validatePassword()` - Validar contraseña segura
- ✅ `validatePasswordMatch()` - Validar coincidencia de contraseñas
- ✅ Data classes: `ValidationResult`, `FormValidationResult`
- ✅ Regex patterns para email, teléfono, DNI
- ✅ Mensajes descriptivos de error

#### 📊 Estadísticas del Día 4

| Métrica | Cantidad |
|---------|----------|
| **Archivos creados** | 8 |
| **Líneas de código** | ~1,445 |
| **Funciones totales** | 57+ |
| **ViewModels** | 3 |
| **Repositorios** | 2 |
| **Utils** | 3 |
| **Estados UI definidos** | 8 |
| **Validaciones** | 15+ |
| **Funciones de fecha** | 18+ |

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

## 🏗️ Arquitectura MVVM (Nuevo Día 4)

```
┌─────────────────────────────────────────────────┐
│                    VIEW (UI)                    │
│           Jetpack Compose Screens               │
│  HomeScreen | DoctorListScreen | BookScreen    │
└──────────────────┬──────────────────────────────┘
                   │ observa StateFlow
                   ↓
┌─────────────────────────────────────────────────┐
│                  VIEWMODEL                      │
│     DoctorViewModel | AppointmentViewModel     │
│   - StateFlow (estados UI)                     │
│   - SharedFlow (eventos únicos)                │
│   - Lógica de presentación                     │
│   - Validaciones                                │
└──────────────────┬──────────────────────────────┘
                   │ llama funciones suspend
                   ↓
┌─────────────────────────────────────────────────┐
│                 REPOSITORY                      │
│  DoctorRepository | AppointmentRepository      │
│   - Result<T> para manejo de errores          │
│   - Simulación de latencia de red              │
│   - Lógica de negocio                          │
└──────────────────┬──────────────────────────────┘
                   │ accede a datos
                   ↓
┌─────────────────────────────────────────────────┐
│                 DATA SOURCE                     │
│              DataSource (simulado)              │
│   - Médicos, Citas, Especialidades             │
│   - Funciones de búsqueda y filtrado           │
└─────────────────────────────────────────────────┘
```

## ✨ Características Implementadas (Día 4)

### 🎯 Gestión de Médicos
- ✅ Búsqueda en tiempo real con debounce (300ms)
- ✅ Filtros por especialidad (múltiples)
- ✅ Carga de detalle de médico
- ✅ Verificación de disponibilidad de horarios
- ✅ Estados de carga (Loading, Success, Error)

### 📅 Gestión de Citas
- ✅ Crear cita con validación completa
- ✅ Listar citas (próximas y pasadas)
- ✅ Ver detalle de cita
- ✅ Reprogramar cita (con validaciones)
- ✅ Cancelar cita (con validaciones)
- ✅ Confirmar cita pendiente
- ✅ Completar cita realizada
- ✅ Validación de disponibilidad en tiempo real
- ✅ Actualización automática de DataSource

### ✔️ Validaciones Implementadas

#### Formulario de Agendar Cita
```
✓ Médico seleccionado (requerido)
✓ Especialidad seleccionada (requerida)
✓ Fecha seleccionada (requerida)
✓ Fecha no pasada
✓ Fecha dentro de 7 días
✓ Hora seleccionada (requerida)
✓ Anticipación mínima 2 horas
✓ Hora en horario laboral (9-12, 15-18)
✓ Slot disponible (no ocupado)
✓ Motivo (10-200 caracteres)
```

#### Formulario de Login
```
✓ Email formato válido (regex)
✓ Email no vacío
✓ Contraseña no vacía
```

#### Formulario de Registro
```
✓ Nombre no vacío
✓ Email formato válido
✓ Teléfono formato válido (9-15 dígitos)
✓ DNI válido (8 dígitos)
✓ Contraseña segura (8+ caracteres, mayúsculas, minúsculas, números)
✓ Contraseñas coinciden
```

### 🔄 Estados de UI Implementados

#### DoctorViewModel
```kotlin
sealed class DoctorUiState {
    object Loading
    data class Success(doctors, specialties)
    data class DoctorDetail(doctor)
    data class Error(message)
}
```

#### AppointmentViewModel
```kotlin
sealed class AppointmentUiState {
    object Loading
    object Processing
    data class AppointmentsList(upcoming, past)
    data class AppointmentDetail(appointment)
    data class Success(appointment)
    data class Error(message)
}

sealed class ActionMessage {
    data class Success(message)
    data class Error(message)
}
```

#### AuthViewModel
```kotlin
sealed class AuthUiState {
    object Idle
    object Loading
    object Success
    data class Error(message)
}

sealed class AuthMessage {
    data class Success(message)
    data class Error(message)
}
```

## 🧪 Testing Manual (Día 4)

### ✅ Flujo CRUD de Citas Funcional
```
1. Login → Home ✅
2. Click "AGENDAR CITA" → Formulario vacío ✅
3. Seleccionar especialidad → Dropdown funciona ✅
4. Seleccionar médico → Filtrado por especialidad ✅
5. Seleccionar fecha → Calendario con 7 días ✅
6. Seleccionar hora → Grid de horarios ✅
7. Escribir motivo (15 chars) → Validación pasa ✅
8. Click "Agendar" → Simula delay de 1s ✅
9. Navega a "Mis Citas" → Nueva cita visible ✅
10. Click en cita → Ver detalle ✅
11. Click "Reprogramar" → Permite cambiar fecha/hora ✅
12. Click "Cancelar" → Cambia estado a CANCELLED ✅
```

### 🎯 Pruebas de Validación
- ❌ Agendar sin médico → Botón deshabilitado ✅
- ❌ Agendar sin fecha → Botón deshabilitado ✅
- ❌ Agendar sin hora → Botón deshabilitado ✅
- ❌ Agendar sin motivo → Botón deshabilitado ✅
- ❌ Motivo < 10 caracteres → Botón deshabilitado ✅
- ✅ Todos los campos completos → Botón habilitado ✅
- ✅ Fecha pasada → Rechazo con mensaje ✅
- ✅ Hora pasada → Rechazo con mensaje ✅
- ✅ Slot ocupado → Rechazo con mensaje ✅

### 🔍 Pruebas de Búsqueda con ViewModel
- Buscar "Dr" → 300ms delay → 3 resultados ✅
- Buscar "María" → Filtrado reactivo → 1 resultado ✅
- Filtrar "Cardiología" → StateFlow actualiza → 1 resultado ✅
- Limpiar filtros → Restaura todos los médicos ✅
- Búsqueda mientras escribe → Debounce funciona ✅

### 📅 Pruebas de Estados de Carga
- Cargar médicos → Muestra Loading → Success ✅
- Crear cita → Muestra Processing → Success ✅
- Error de red simulado → Muestra Error con mensaje ✅
- Cancelar cita → Confirma → Actualiza estado ✅

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

### 📅 Citas (3 iniciales + dinámicas)
- **Próximas:**
  1. Dr. Carlos Mendoza - 2 días (Confirmada)
  2. Dra. María Fernández - 5 días (Pendiente)
- **Pasadas:**
  1. Dra. Ana Torres - hace 10 días (Completada)
- **Dinámicas:** Las citas creadas por el usuario se persisten en DataSource

### ⏰ TimeSlots
- **Horarios generados:** 7 días siguientes
- **Turnos mañana:** 9:00 - 12:00 (slots cada 30 min)
- **Turnos tarde:** 15:00 - 18:00 (slots cada 30 min)
- **Disponibilidad:** Simulada con algoritmo (70% disponibles)
- **Actualización:** Slots ocupados se marcan al crear cita

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

### ❌ Error: "Unresolved reference: viewmodel"
**Causa:** Falta carpeta `viewmodel/`

**Solución:**
1. Click derecho en `com.project.mediturn`
2. New > Package
3. Nombre: `viewmodel`
4. Copiar los 3 archivos de ViewModels

### ❌ Error: "Unresolved reference: repository"
**Causa:** Falta carpeta `data/repository/`

**Solución:**
1. Click derecho en `data/`
2. New > Package
3. Nombre: `repository`
4. Copiar los 2 archivos de Repositorios

### ❌ Las validaciones no funcionan
**Causa:** Falta Utils

**Solución:**
1. Crear carpeta `util/`
2. Copiar `Constants.kt`, `DateUtils.kt`, `ValidationUtils.kt`
3. Rebuild Project

### ❌ Build falla después de agregar ViewModels
**Solución:**
```
1. Build > Clean Project
2. File > Invalidate Caches > Invalidate and Restart
3. Sync Project with Gradle Files
4. Build > Rebuild Project
```

## 💡 Aprendizajes del Día 4

### 🏗️ Arquitectura MVVM
- ✅ Separación clara de responsabilidades
- ✅ Testabilidad mejorada
- ✅ Reutilización de código de negocio
- ✅ Gestión de estados limpia y predecible

### 📊 StateFlow vs SharedFlow
- ✅ **StateFlow:** Estados persistentes de UI (lista de médicos, citas)
- ✅ **SharedFlow:** Eventos únicos (mensajes de éxito/error, navegación)
- ✅ **collectAsState():** Observar StateFlow en Composables
- ✅ **LaunchedEffect:** Reaccionar a SharedFlow

### 🎯 Result<T> Pattern
- ✅ Manejo explícito de éxito y error
- ✅ Sin excepciones no controladas
- ✅ `onSuccess` / `onFailure` para branching claro
- ✅ Propagación limpia de errores

### ⚡ Kotlin Coroutines
- ✅ `suspend fun` para operaciones asíncronas
- ✅ `viewModelScope` para lanzar coroutines
- ✅ `delay()` para simulación de latencia
- ✅ Cancelación automática al destruir ViewModel

## 📄 Resumen de Cambios - Día 4

```
ARCHIVOS CREADOS (8 archivos, ~1,445 líneas):

REPOSITORIOS (2 archivos):
- DoctorRepository.kt: getAllDoctors(), searchDoctors(), getDoctorById(), 
  getDoctorsBySpecialty(), searchDoctorsFlow(), checkDoctorAvailability()
- AppointmentRepository.kt: createAppointment(), getPatientAppointments(),
  getUpcomingAppointments(), getPastAppointments(), getAppointmentById(),
  rescheduleAppointment(), cancelAppointment(), confirmAppointment(),
  completeAppointment(), canReschedule(), canCancel()

VIEWMODELS (3 archivos):
- DoctorViewModel.kt: loadAllDoctors(), searchDoctors(), updateSearchQuery(),
  toggleSpecialty(), clearFilters(), loadDoctorById()
- AppointmentViewModel.kt: loadAppointments(), loadAppointmentById(),
  updateSelectedDoctor(), updateSelectedDateTime(), updateReason(),
  toggleTelemedicine(), clearForm(), isFormValid(), createAppointment(),
  rescheduleAppointment(), cancelAppointment(), confirmAppointment(),
  canCancel(), canReschedule()
- AuthViewModel.kt: updateEmail(), updatePassword(), updateName(), updatePhone(),
  updateDNI(), login(), quickLogin(), register(), logout(), clearLoginForm(),
  clearRegisterForm(), isLoginFormValid(), isRegisterFormValid()

UTILS (3 archivos):
- Constants.kt: 95 líneas de constantes (app info, formatos, restricciones,
  horarios, configuración, URLs, mensajes, icons, valores por defecto, keys)
- DateUtils.kt: 18+ funciones de utilidad para fechas (formatters, validadores,
  generadores, comparadores, calculadores de tiempo relativo)
- ValidationUtils.kt: 15+ funciones de validación (email, teléfono, DNI, reason,
  date, dateTime, appointmentForm, password, etc.)

FUNCIONALIDADES IMPLEMENTADAS:
- ✅ Arquitectura MVVM completa
- ✅ StateFlow para estados de UI
- ✅ SharedFlow para mensajes de acción
- ✅ Result<T> para manejo de errores
- ✅ Coroutines para operaciones asíncronas
- ✅ Simulación de latencia de red (300-1000ms)
- ✅ Validaciones en tiempo real
- ✅ Debounce en búsqueda (300ms)
- ✅ CRUD completo de citas
- ✅ Gestión de disponibilidad de slots
- ✅ Actualización reactiva de DataSource

VALIDACIONES AGREGADAS:
- ✅ Email formato válido (regex)
- ✅ Teléfono formato válido (9-15 dígitos)
- ✅ DNI válido (8 dígitos)
- ✅ Motivo de consulta (10-200 caracteres)
- ✅ Fecha no pasada
- ✅ Fecha dentro de 7 días
- ✅ Anticipación mínima (2 horas)
- ✅ Horario laboral (9-12, 15-18)
- ✅ Slot disponible
- ✅ Contraseña segura (8+ chars, mayús, minús, números)

ESTADOS UI DEFINIDOS:
- ✅ DoctorUiState: Loading, Success, DoctorDetail, Error
- ✅ AppointmentUiState: Loading, Processing, AppointmentsList,
     AppointmentDetail, Success, Error
- ✅ AuthUiState: Idle, Loading, Success, Error
- ✅ ActionMessage: Success, Error (para eventos únicos)

ESTADO: Objetivos del Día 4 completados al 100% ✅
```

## 📸 Capturas de Pantalla

*(Agregar screenshots aquí después de Day 4)*

1. **BookAppointmentScreen** - Loading state con CircularProgressIndicator
2. **DoctorListScreen** - Con ViewModels y búsqueda reactiva
3. **MyAppointmentsScreen** - Estados de carga y error
4. **Snackbar** - Mensajes de éxito al crear cita
5. **Validaciones** - Botón deshabilitado con campos incompletos
6. **Calendario** - Slots disponibles y ocupados en tiempo real

## 📄 Licencia

Proyecto académico - **Tecsup**  
Curso: **Aplicaciones Móviles con Android** (Kotlin + Jetpack Compose)  
Docente: **Juan León**  
Modalidad: Trabajo colaborativo (equipos de 2 estudiantes)

---

## 🎯 Próximos Pasos (Día 5)

### Objetivos Día 5:
- [ ] **Integrar ViewModels en todas las pantallas**
- [ ] **Agregar SnackBars para mensajes de éxito/error**
- [ ] **Implementar diálogos de confirmación (cancelar cita, salir)**
- [ ] **Pull-to-refresh en listas**
- [ ] **Swipe-to-delete en MyAppointmentsScreen**
- [ ] **Animaciones de transición entre pantallas**
- [ ] **Loading skeletons en lugar de CircularProgressIndicator**
- [ ] **Testing en diferentes dispositivos y tamaños**
- [ ] **Modo oscuro (Dark Mode) completo**
- [ ] **Accessibility (TalkBack, tamaños de fuente)**

### Funcionalidades Día 5:
- Refinar la experiencia de usuario (UX)
- Pulir animaciones y transiciones
- Agregar feedback visual en todas las acciones
- Optimizar rendimiento
- Testing exhaustivo
- Corrección de bugs

---

**Última actualización:** 27 de octubre de 2025  
**Versión:** v1.0.0-day4  
**Estado:** ✅ Día 4 completado al 100%  
**Próximo hito:** Día 5 - Pulido y Testing

## 📧 Contacto

**GitHub:** [github.com/Casapaico/mediturn](https://github.com/Casapaico/mediturn)  
**Figma:** [Diseño MediTurn](https://www.figma.com/design/MAJvvF2McJeoIIlhNaqIqU/mediturn)

---

### 🌟 Agradecimientos

Agradecemos al docente **Juan León** por la guía durante el desarrollo del proyecto, y a **Tecsup** por proporcionar los recursos y el ambiente de aprendizaje para completar este proyecto.

---

**🚀 MediTurn - Tu salud en buenas manos**