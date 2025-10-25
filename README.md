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

## 🛠️ Tecnologías

- **Lenguaje:** Kotlin
- **Framework UI:** Jetpack Compose
- **Navegación:** Navigation Compose
- **Arquitectura:** MVVM (en desarrollo)
- **IDE:** Android Studio
- **Control de versiones:** Git/GitHub
- **Diseño:** Figma
- **Imágenes:** Coil Compose

## 📂 Estructura del Proyecto
```
app/src/main/java/com/project/mediturn/
├── MainActivity.kt                    # Punto de entrada
├── data/
│   ├── model/                         # Modelos de datos
│   │   ├── Doctor.kt
│   │   ├── Appointment.kt
│   │   ├── Patient.kt
│   │   ├── TimeSlot.kt
│   │   └── Specialty.kt
│   └── DataSource.kt                  # Datos simulados
├── navigation/
│   ├── NavGraph.kt                    # Configuración de rutas
│   └── Routes.kt                      # Definición de pantallas
├── ui/
│   ├── components/                    # Componentes reutilizables
│   │   ├── SearchBar.kt
│   │   ├── SpecialtyChip.kt
│   │   ├── DoctorCard.kt
│   │   ├── AppointmentCard.kt
│   │   ├── EmptyState.kt
│   │   └── TimeSlotButton.kt
│   ├── screens/                       # Pantallas de la app
│   │   ├── auth/                      # Login y Registro
│   │   ├── home/                      # Pantalla principal
│   │   ├── doctors/                   # Búsqueda y detalle de médicos
│   │   ├── appointments/              # Gestión de citas
│   │   └── profile/                   # Perfil del usuario
│   └── theme/                         # Colores y estilos
└── viewmodel/                         # (Próximo: lógica de negocio)
```

## 🚀 Estado del Proyecto

**Versión actual:** v0.3.0 (Día 3 - UI/UX Completo)

### Cronograma
- ✅ **Día 1:** Planificación y Diseño (Figma)
- ✅ **Día 2:** Configuración del Proyecto y Estructura Base
- ✅ **Día 3:** Desarrollo de Interfaz (UI/UX) ← **COMPLETADO**
- ⏳ **Día 4:** Lógica y Datos Simulados
- ⏳ **Día 5:** Funcionalidades Clave y Pulido
- ⏳ **Día 6:** Presentación Final y Documentación

### Progreso Día 3 ✨
- ✅ 6 componentes reutilizables creados (SearchBar, DoctorCard, etc.)
- ✅ DataSource con datos simulados (6 médicos, 8 especialidades, 3 citas)
- ✅ HomeScreen con saludo personalizado y carrusel de especialidades
- ✅ DoctorListScreen con búsqueda y filtros funcionales
- ✅ DoctorDetailScreen completo con perfil, servicios y precios
- ✅ BookAppointmentScreen con selector de fecha/hora y validación
- ✅ MyAppointmentsScreen con tabs (Próximas/Pasadas)
- ✅ Material Design 3 aplicado en toda la app
- ✅ Navegación completa entre todas las pantallas
- ✅ Estados vacíos implementados

## 📝 Instalación y Uso

### Requisitos Previos
- Android Studio Hedgehog o superior
- JDK 11 o superior
- Android SDK API 24+ (Android 7.0+)

### Pasos de Instalación
```bash
# 1. Clonar el repositorio
git clone https://github.com/Casapaico/mediturn.git

# 2. Abrir en Android Studio
# File > Open > Seleccionar carpeta del proyecto

# 3. Sync Gradle
# Android Studio lo hará automáticamente

# 4. Ejecutar en emulador o dispositivo
# Run > Run 'app'
```

### Configurar Dispositivo Físico
1. Habilitar **Modo Desarrollador** en tu dispositivo Android
2. Activar **Depuración USB**
3. Conectar dispositivo por USB
4. Seleccionar dispositivo en Android Studio
5. Click en ▶️ Run

## 🎨 Paleta de Colores

- **Primary:** `#2196F3` (Azul Médico)
- **Secondary:** `#4CAF50` (Verde Salud)
- **Tertiary:** `#BBDEFB` (Azul Claro)
- **Error:** `#E53935` (Rojo)
- **Background:** `#F5F5F5` (Gris Claro)

## 🔄 Flujo de Navegación
```
Login → Home → Búsqueda de Médicos → Detalle → Agendar Cita
  ↓                ↓                      ↓           ↓
Register      Mis Citas              (Perfil)   Mis Citas
                 ↓
            Detalle de Cita
```

## ✨ Características Implementadas

### 🏠 HomeScreen
- Saludo personalizado con nombre del usuario
- Botones de acceso rápido (Buscar Médicos, Mis Citas)
- Carrusel de especialidades populares (6 chips)
- Diseño con Material Design 3

### 🔍 DoctorListScreen
- Barra de búsqueda en tiempo real
- Filtros por especialidad (8 opciones)
- Listado de 6 médicos con LazyColumn
- Cards con foto, rating, experiencia, precio
- Badge de teleconsulta disponible
- Estado vacío cuando no hay resultados

### 👨‍⚕️ DoctorDetailScreen
- Header con foto grande del médico (120dp)
- Información profesional completa (colegiatura, experiencia, ciudad)
- Descripción detallada del médico
- Cards de servicios (Presencial/Teleconsulta)
- Precio destacado
- Badge de horarios disponibles
- Botón "Agendar Cita" en bottom bar

### 📅 BookAppointmentScreen
- Card con información del médico
- Selector visual de fecha (próximos 7 días)
- Grid de horarios disponibles (3x3)
- Filtrado automático de horarios por fecha
- Campo de motivo de consulta
- Toggle de tipo de consulta (Presencial/Virtual)
- Card de resumen de la cita
- Validación completa del formulario
- Botón "Confirmar" habilitado solo con datos completos

### 📋 MyAppointmentsScreen
- Tabs para "Próximas" y "Pasadas"
- Cards de cita con foto del médico y detalles
- Badge de estado (Confirmada, Pendiente, Completada, Cancelada)
- Fecha, hora, motivo y tipo de consulta
- Estados vacíos personalizados

## 📦 Dependencias Principales
```kotlin
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
```

## 🎯 Componentes Reutilizables

| Componente | Descripción |
|------------|-------------|
| **SearchBar** | Barra de búsqueda con ícono y botón limpiar |
| **SpecialtyChip** | Chip seleccionable con emoji de especialidad |
| **DoctorCard** | Card completa con foto, info y rating del médico |
| **AppointmentCard** | Card de cita con estado, fecha y detalles |
| **EmptyState** | Estado vacío con emoji y mensaje personalizado |
| **TimeSlotButton** | Botón de horario con estado disponible/ocupado |

## 👨‍💻 Convención de Commits

```
feat: nueva funcionalidad
fix: corrección de bug
docs: cambios en documentación
style: formato, estilos
refactor: refactorización de código
test: añadir o modificar tests
```

**Ejemplos:**

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

**Día 3:**
```bash
git commit -m "feat: Complete Day 3 - Full UI implementation

- Add complete DoctorDetailScreen with doctor info, services, pricing
- Add complete BookAppointmentScreen with date/time picker and validation
- Update HomeScreen with specialty carousel and quick actions
- Update DoctorListScreen with search and filters
- Update MyAppointmentsScreen with tabs and appointment cards
- Create 6 reusable components (SearchBar, DoctorCard, etc.)
- Add DataSource with simulated data (6 doctors, 8 specialties, 3 appointments)
- Apply Material Design 3 consistently across all screens
- Implement form validation in BookAppointmentScreen
- Add empty states for all lists
- Complete all Day 3 requirements (100%)"
```

## 🐛 Solución de Problemas

### Error: "Unresolved reference: components"
- **Solución:** Verificar que la carpeta `ui/components/` exista y contenga todos los archivos
- File > Invalidate Caches > Invalidate and Restart

### Error: "Unresolved reference: DataSource"
- **Solución:** Verificar que `DataSource.kt` esté en `data/DataSource.kt` con el package correcto

### Error: "Device not found"
- Verificar que Depuración USB esté activada
- Desconectar y reconectar el dispositivo

### Las imágenes no cargan
- **Solución:** Verificar que el dispositivo/emulador tenga conexión a internet
- Agregar permiso de INTERNET en `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### Build falla
- Build > Clean Project
- Build > Rebuild Project
- File > Invalidate Caches > Invalidate and Restart

## 📊 Datos Simulados

### Médicos (6)
- Dr. Carlos Mendoza (Cardiología)
- Dra. María Fernández (Dermatología)
- Dr. Juan Pérez (Pediatría)
- Dra. Ana Torres (Neurología)
- Dr. Roberto Sánchez (Traumatología)
- Dra. Patricia Rojas (Oftalmología)

### Especialidades (8)
Cardiología, Dermatología, Pediatría, Neurología, Traumatología, Oftalmología, Ginecología, Psiquiatría

### Citas (3)
- 1 próxima confirmada
- 1 próxima pendiente
- 1 pasada completada

## 🧪 Testing

### Flujo Completo Funcional
1. Login → Home
2. Buscar "Dr. Carlos" → Ver resultados filtrados
3. Click en médico → Ver detalle completo
4. Click "Agendar Cita"
5. Seleccionar fecha → Ver horarios disponibles
6. Seleccionar hora → Escribir motivo
7. Ver resumen → Confirmar cita
8. Ver cita en "Mis Citas" (Próximas)

### Pruebas de Validación
- ❌ Confirmar sin fecha → Botón deshabilitado
- ❌ Confirmar sin hora → Botón deshabilitado
- ❌ Confirmar sin motivo → Botón deshabilitado
- ✅ Completar todos los campos → Botón habilitado

## 📄 Licencia

Proyecto académico - Tecsup  
Curso: Aplicaciones Móviles con Android (Kotlin + Jetpack Compose)  
Docente: Juan León

---

**Última actualización:** 24 de octubre de 2025  
**Versión:** 0.3.0 (Día 3 completado - UI/UX 100%)

## 🎯 Próximos Pasos (Día 4)

- Implementar ViewModels (MVVM)
- Crear repositorios locales
- Agregar lógica de negocio
- Funciones de crear/editar/cancelar citas
- Estados de carga y manejo de errores