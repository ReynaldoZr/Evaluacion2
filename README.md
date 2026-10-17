# Evaluación 2 - Catálogo de productos

Aplicación de escritorio para consultar y administrar productos de una papelería.
Fue realizada con JavaFX y utiliza PostgreSQL para guardar los datos.

## Tecnologías

- Java 21
- JavaFX 21.0.6
- PostgreSQL 18
- PostgreSQL JDBC 42.7.8
- Maven
- Scene Builder

## Carpetas principales

- `src`: código Java, archivo FXML y estilos.
- `sql`: creación de la tabla y datos iniciales.
- `evidencias`: capturas del funcionamiento.
- `pruebas.md`: lista de pruebas realizadas.

## Crear la base de datos

1. Abrir pgAdmin.
2. Crear una base de datos llamada `papeleria_campus`.
3. Abrir el Query Tool de esa base de datos.
4. Ejecutar primero `sql/estructura.sql`.
5. Ejecutar después `sql/datos.sql`.

También se puede crear escribiendo `CREATE DATABASE papeleria_campus;` en pgAdmin.

## Configurar la conexión

La contraseña no se guarda en el proyecto. En IntelliJ IDEA se deben agregar
estas variables en **Run > Edit Configurations > Environment variables**:

- `DB_URL=jdbc:postgresql://localhost:5432/papeleria_campus`
- `DB_USER=postgres`
- `DB_PASSWORD=SU_CONTRASEÑA`

El archivo `config.example.env` contiene el mismo ejemplo sin una contraseña real.

## Ejecutar el proyecto

1. Abrir la carpeta del proyecto en IntelliJ IDEA.
2. Esperar que Maven descargue las dependencias.
3. Configurar las variables de conexión.
4. Ejecutar la clase `Launcher`.

También se puede ejecutar desde una terminal con `mvnw.cmd javafx:run`.

## Funcionamiento

La interfaz recibe los datos y el controlador los valida. Después, el controlador
usa el DAO para consultar PostgreSQL y mostrar los resultados en el TableView.

## Herramientas y fuentes

- Guía de laboratorio proporcionada por el profesor.
- IntelliJ IDEA, Scene Builder, pgAdmin y PostgreSQL.
- Codex de OpenAI como apoyo para revisar el proyecto y la documentación.
