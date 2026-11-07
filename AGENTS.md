# 🧘 MindfulDays — Directrices y Contexto para Agentes de IA

Este archivo actúa como la fuente de verdad para agentes de IA y herramientas de asistencia de código que operan en este repositorio.

## 📱 Visión General del Proyecto
**MindfulDays** es una aplicación Android nativa enfocada en mindfulness y bienestar mental, basada en las **9 Actitudes de Mindfulness de Jon Kabat-Zinn**, con soporte para reflexiones diarias generadas mediante la **Google Gemini API** y un temporizador circular de meditación.

## 🏗️ Patrones de Arquitectura y Stack Técnico
- **Lenguaje**: Kotlin 1.9+ (uso idiomático de coroutines, StateFlow e inmutabilidad).
- **UI Toolkit**: Jetpack Compose con Material Design 3 (M3). **Prohibido el uso de layouts XML tradicionales**.
- **Patrón de Arquitectura**: MVVM (Model-View-ViewModel) alineado con Clean Architecture:
  - `data/model/`: Data classes inmutables.
  - `data/repository/`: Repositorios para fuente de datos local y lógica de las 9 actitudes.
  - `data/remote/`: Clientes y servicios para comunicación con la Gemini API.
  - `ui/`: Pantallas y componentes en Compose divididos por feature (`home`, `timer`, `settings`).
  - `ui/theme/`: Tokens visuales (Color, Theme, Type).
  - `ui/viewmodel/`: ViewModels que exponen estados mediante `StateFlow`.
- **Paquete Base**: `dev.mindfuldays.app`.

## 🎨 Design Tokens & Temas (Material Design 3 — Light & Dark Mode)
Respeta estrictamente los tokens semánticos de colores de Material 3, garantizando soporte perfecto y contraste adecuado tanto en tema claro como oscuro:

### Tema Claro (Light Theme):
- `primary`: Verde Salvia (`#6B8E23`)
- `primaryContainer`: Salvia Suave (`#8FA853`)
- `secondary`: Arena Muted (`#E8DFD8`)
- `background`: Blanco Cálido (`#FBF9F5`)
- `surface`: Blanco Puro (`#FFFFFF`)
- `onBackground` / `onSurface`: Verde Bosque Oscuro (`#2C3E35`)
- `onSurfaceVariant`: Verde Bosque Suave (`#5C6F64`)
- `outlineVariant`: Arena Muted (`#E8DFD8`)

### Tema Oscuro (Dark Theme):
- `primary`: Salvia Luminoso (`#A8C673`)
- `primaryContainer`: Bosque Oscuro Elevado (`#2E4239`)
- `secondary`: Salvia Suave (`#A3B5A8`)
- `background`: Bosque Nocturno Profundo (`#15201A`)
- `surface`: Bosque Nocturno Elevado (`#22322A`)
- `onBackground` / `onSurface`: Blanco Cálido Claro (`#F1EFEA`)
- `onSurfaceVariant`: Salvia Muted Claro (`#A3B5A8`)
- `outlineVariant`: Borde Sutil de Bosque (`#3B5045`)

## 🧠 Reglas de Negocio Fundamentales
1. **Regla de las 9 Actitudes**: La lista contiene exactamente 9 actitudes fijas. La actitud del día se calcula determinísticamente a partir del día del año:
   `val index = (dayOfYear - 1).mod(attitudes.size)`
2. **Gemini API (Reflexiones Diarias)**:
   - Utilizar el endpoint del modelo `gemini-3.5-flash:generateContent` (o `gemini-3.5-flash-lite:generateContent`).
   - Enviar la clave en el header `x-goog-api-key` (no en la URL) para que no aparezca en Logcat.
   - Configurar OkHttpClient con timeouts holgados adecuados para LLM (`connectTimeout`: 30s, `readTimeout`: 60s, `writeTimeout`: 30s). No fijar `callTimeout` agresivos ni `pingInterval` para evitar errores de SocketTimeoutException.
   - Generar reflexiones concisas e inspiradoras con un máximo de 2 oraciones por actitud, respetando el idioma del usuario (pt, en, es).
   - Manejar la ausencia de clave de API y errores de red con un mensaje amigable offline sin romper la app.

## 🛠️ Comandos de Verificación y Validación
Verifica siempre los cambios utilizando los siguientes comandos en la terminal:
- **Compilación de la app**: `./gradlew assembleDebug`
- **Pruebas Unitarias**: `./gradlew testDebugUnitTest`
- **Pruebas de UI (Robot Pattern)**: `./gradlew connectedDebugAndroidTest`

## 🚫 Restricciones Estrictas (Guardrails)
- **NUNCA utilices colores hexadecimales hardcodeados en Composables** (`Color(0xFF...)`). Utiliza siempre `MaterialTheme.colorScheme.background`, `MaterialTheme.colorScheme.surface`, `MaterialTheme.colorScheme.onSurface`, etc., para evitar problemas de contraste en el Modo Oscuro.
- **NUNCA** hagas commit ni dejes credenciales o claves de API (`apiKey`) en el código versionado. La clave debe configurarse en `local.properties` y consumirse mediante `BuildConfig.GEMINI_API_KEY`.
- **NO** mezcles lógica de negocio o llamadas asíncronas de red directamente dentro de funciones `@Composable`.
- Mantén las funciones Composable limpias y desacopladas, utilizando parámetros para eventos y lambdas (`onAction: () -> Unit`) para facilitar pruebas de UI.
- Utiliza recursos internacionalizados (`stringResource(R.string...)`) para textos visibles e inglés para código técnico, métodos e interfaces.
