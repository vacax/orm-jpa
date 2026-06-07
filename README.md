# Proyecto ORM con JPA

Proyecto de demostración de **ORM con JPA** sobre [Javalin](https://javalin.io/),
pensado para uso educativo (ICC352 - PUCMM). Expone una pequeña API REST de
estudiantes, profesores y fotos, persistiendo los datos mediante Hibernate.
Está preparado para desplegarse en Heroku con PostgreSQL o como contenedor Docker.

## Tecnologías

| Componente | Versión |
|------------|---------|
| Java       | 25      |
| Gradle     | 9.5.1 (Groovy DSL) |
| Javalin    | 7.2.2   |
| Hibernate ORM | 7.4.0.Final |
| JPA (Jakarta Persistence) | 3.x |
| Thymeleaf  | 3.1.5   |
| Jackson    | 2.22.0  |
| Base de datos | H2 (local) / PostgreSQL (producción) |

## Requisitos

* Java (JDK) 25
* Gradle 9.5.1 (incluido vía Gradle Wrapper, no requiere instalación)

## Ejecución local

Compilar y empaquetar la aplicación en un único JAR ejecutable (fat jar):

```bash
./gradlew shadowJar
java -jar build/libs/app.jar
```

La aplicación queda disponible en `http://localhost:7000`.

Durante el arranque se crean automáticamente 50 estudiantes y 50 profesores de
ejemplo sobre una base de datos H2 en memoria.

## Endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET    | `/` | Mensaje de bienvenida |
| GET    | `/api/estudiante` | Lista todos los estudiantes |
| POST   | `/api/estudiante` | Crea un estudiante (body JSON) |
| PUT    | `/api/estudiante` | Actualiza un estudiante (body JSON) |
| GET    | `/api/estudiante/{matricula}` | Estudiante por matrícula |
| DELETE | `/api/estudiante/{matricula}` | Elimina un estudiante |
| GET    | `/api/profesor` | Lista todos los profesores |
| POST   | `/api/profesor` | Crea un profesor (body JSON) |
| PUT    | `/api/profesor` | Actualiza un profesor (body JSON) |
| GET    | `/api/profesor/{id}` | Profesor por id |
| DELETE | `/api/profesor/{id}` | Elimina un profesor |
| GET    | `/fotos/listar` | Listado de fotos |

## Ejemplos con curl

Con la aplicación corriendo en `http://localhost:7000`, puedes probar la API
REST de `/api/` con los siguientes comandos.

### Estudiantes

```bash
# Listar todos los estudiantes
curl http://localhost:7000/api/estudiante

# Obtener un estudiante por su matrícula
curl http://localhost:7000/api/estudiante/5

# Crear un estudiante (la matrícula es el id, se asigna manualmente)
curl -X POST http://localhost:7000/api/estudiante \
  -H "Content-Type: application/json" \
  -d '{"matricula": 1001, "nombre": "Juan Pérez"}'

# Actualizar un estudiante existente
curl -X PUT http://localhost:7000/api/estudiante \
  -H "Content-Type: application/json" \
  -d '{"matricula": 1001, "nombre": "Juan Antonio Pérez"}'

# Eliminar un estudiante por su matrícula
curl -X DELETE http://localhost:7000/api/estudiante/1001
```

> Nota: el nombre se almacena en mayúsculas automáticamente y la matrícula
> `20011137` está reservada para demostrar el manejo de excepciones.

### Profesores

```bash
# Listar todos los profesores
curl http://localhost:7000/api/profesor

# Obtener un profesor por su id
curl http://localhost:7000/api/profesor/5

# Crear un profesor (el id se genera automáticamente)
curl -X POST http://localhost:7000/api/profesor \
  -H "Content-Type: application/json" \
  -d '{"nombre": "María García", "otroCampo": "Departamento de Computación"}'

# Actualizar un profesor existente (incluir el id a modificar)
curl -X PUT http://localhost:7000/api/profesor \
  -H "Content-Type: application/json" \
  -d '{"id": 5, "nombre": "María García López", "otroCampo": "Coordinación"}'

# Eliminar un profesor por su id
curl -X DELETE http://localhost:7000/api/profesor/5
```

## Docker

Construir y ejecutar la imagen (compilación todo-en-uno):

```bash
docker build -f Dockerfile-todoenuno -t orm-jpa .
docker run -p 7000:7000 orm-jpa
```

## Despliegue en Heroku

El proyecto incluye `Procfile` y `system.properties` para desplegarse en Heroku.
La tarea `stage` de Gradle genera el JAR mediante ShadowJar:

```bash
./gradlew stage
```

El `Procfile` ejecuta `java -jar build/libs/app.jar`.
