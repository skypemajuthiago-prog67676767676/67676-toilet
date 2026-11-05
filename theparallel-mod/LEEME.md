# The Parallel – Terror Analógico (Fabric 1.21.1)
Requisitos: Java 21 y Fabric API para 1.21.1.
Compilar: si no tienes el Gradle wrapper, genera una plantilla en https://fabricmc.net/develop/template (1.21.1, Yarn),
copia la carpeta `src` de este proyecto encima de la plantilla y ejecuta `./gradlew build`. El .jar queda en build/libs/.
Pruebas: en creativo, pestaña de huevos de aparición ("El Creador", "Criatura Roja", etc.).

## Compilar gratis con GitHub
1. Crea un repositorio en github.com y sube TODO el contenido de esta carpeta (incluida la carpeta oculta .github).
2. Pestaña "Actions" > "build" > espera el check verde.
3. Entra en esa ejecución, baja a "Artifacts" y descarga "theparallel-jar". Dentro está el .jar (usa el que NO termina en -sources).
