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

**Figma:** [Enlace al prototipo](URL_DE_FIGMA_AQUI)

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
- **Arquitectura:** MVVM (preparado para implementar)
- **IDE:** Android Studio
- **Control de versiones:** Git/GitHub
- **Diseño:** Figma

## 📂 Estructura del Proyecto
```
app/src/main/java/com/project/mediturn/
├── MainActivity.kt                    # Punto de entrada
├── navigation/
│   ├── NavGraph.kt                    # Configuración de rutas
│   └── Routes.kt                      # Definición de pantallas
├── ui/
│   ├── screens/                       # Pantallas de la app
│   │   ├── auth/                      # Login y Registro
│   │   ├── home/                      # Pantalla principal
│   │   ├── doctors/                   # Búsqueda y detalle de médicos
│   │   ├── appointments/              # Gestión de citas
│   │   └── profile/                   # Perfil del usuario
│   └── theme/                         # Colores y estilos
├── data/                              # (Próximo: modelos y repositorios)
└── viewmodel/                         # (Próximo: lógica de negocio)
```

## 🚀 Estado del Proyecto

**Versión actual:** v0.2.0 (Día 2 - Estructura Base)

### Cronograma
- ✅ **Día 1:** Planificación y Diseño (Figma)
- ✅ **Día 2:** Configuración del Proyecto y Estructura Base
- ⏳ **Día 3:** Desarrollo de Interfaz (UI/UX)
- ⏳ **Día 4:** Lógica y Datos Simulados
- ⏳ **Día 5:** Funcionalidades Clave y Pulido
- ⏳ **Día 6:** Presentación Final y Documentación

### Progreso Día 2
- ✅ Proyecto creado con Kotlin + Jetpack Compose
- ✅ Estructura de paquetes completa
- ✅ Sistema de navegación con 9 pantallas
- ✅ Pantallas base con Material3
- ✅ Tema personalizado con colores médicos
- ✅ Build exitoso sin errores

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
  ↓                ↓                                  ↓
Register      Mis Citas ←──────────────────────────────
                 ↓
            Detalle de Cita
```

## 📦 Dependencias Principales
```kotlin
// Compose
androidx.compose.material3:material3
androidx.navigation:navigation-compose:2.8.4

// Lifecycle
androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7

// Coroutines
org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1

// Retrofit (preparado para Día 4)
com.squareup.retrofit2:retrofit:2.9.0
```

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
git commit -m "feat: feat: crear estructura base de pantallas con rutas, parámetros y composables"
git commit -m "feat: crear paleta de colores médicos con primarios, estados y esquema neutro"
```

CASAPAICO:

```bash
git commit -m "screens"
git commit -m "correción Theme"
```



## 🐛 Solución de Problemas

### Error: "Unresolved reference"
- Verificar que todos los imports sean correctos
- File > Invalidate Caches > Invalidate and Restart

### Error: "Device not found"
- Verificar que Depuración USB esté activada
- Desconectar y reconectar el dispositivo

### Build falla
- Build > Clean Project
- Build > Rebuild Project

## 📄 Licencia

Proyecto académico - Tecsup  
Curso: Aplicaciones Móviles con Android (Kotlin + Jetpack Compose)  
Docente: Juan León

---

**Última actualización:** 23 de octubre de 2025  
**Versión:** 0.2.0 (Día 2 completado)