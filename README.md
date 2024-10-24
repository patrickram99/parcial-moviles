# Quiz de League of Legends - Aplicación Android

## 📱 Descripción
Esta aplicación es un quiz interactivo sobre League of Legends que pone a prueba el conocimiento de los usuarios sobre los campeonatos mundiales del juego. La aplicación está desarrollada en Kotlin para Android, utilizando arquitectura MVVM y los componentes más modernos de Android.

## 🏗️ Estructura del Proyecto

```
app/
├── src/main/
│   ├── assets/                  # Archivos JSON con preguntas y tabla de clasificación
│   ├── java/.../
│   │   ├── fragments/          # Fragmentos para cada pantalla de la aplicación
│   │   ├── models/             # Modelos de datos
│   │   └── viewmodels/         # ViewModels para la lógica de negocio
│   └── res/                    # Recursos de la aplicación
```

### 📂 Componentes Principales

#### Assets
- `quiz_questions.json`: Contiene las preguntas del quiz sobre LoL
- `leaderboard.json`: Almacena la tabla de clasificación

#### Fragments
- `WelcomeFragment`: Pantalla de bienvenida
- `QuestionFragment`: Muestra las preguntas del quiz
- `AnswerFragment`: Feedback de respuesta correcta/incorrecta
- `EndFragment`: Resultados finales

#### ViewModels
- `QuizViewModel`: Maneja la lógica del quiz y el estado de la aplicación

## 🛠️ Tecnologías Utilizadas
- Kotlin
- Android Jetpack
  - Navigation Component
  - ViewModel
  - LiveData
- Coroutines
- Material Design

## 📌 Características
- Preguntas sobre la historia de los campeonatos mundiales de LoL
- Sistema de puntuación
- Tabla de clasificación
- Retroalimentación inmediata de respuestas
- Diseño intuitivo y atractivo
- Modo oscuro soportado

## 🚀 Configuración del Proyecto
1. Clona el repositorio
2. Abre el proyecto en Android Studio
3. Sincroniza con Gradle
4. Ejecuta la aplicación

## 📄 Requisitos
- Android Studio Arctic Fox o superior
- SDK mínimo: Android 6.0 (API level 23)
- SDK objetivo: Android 13 (API level 33)
