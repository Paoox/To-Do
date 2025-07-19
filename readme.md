# 🌐 Mini Red Social (Blah) - Backend Spring Boot + PostgreSQL + Docker

Este es el **backend completo** de una mini red social desarrollada con **Java + Spring Boot 3.5**, conectada a **PostgreSQL** y contenerizada con **Docker**, como parte del portafolio profesional de **Paola Arreola (@PaooxDev)**.

🧠 Esta app simula el funcionamiento básico de una red social: publicaciones con imagen, perfiles de usuario, likes y reacciones. Perfecta como base para proyectos más grandes.

---

## ✅ Funcionalidades implementadas en el BACKEND

### 🧍‍♂️ Gestión de usuarios (`/usuarios`)

- **Registro de usuario nuevo** con:
    - Validación de email y nickname duplicados
    - Encriptación de contraseña (BCrypt)
    - Avatar aleatorio (`https://i.pravatar.cc`)
    - Visualizaciones aleatorias
    - Fecha de registro (`LocalDateTime`)

- **Login de usuario** con:
    - Verificación de credenciales
    - Generación de JWT con `id`, `nombre`, `email`
    - Respuesta con `usuarioSeguro` (sin password)

- **Obtener todos los usuarios** (`GET /usuarios`)
- **Obtener usuario por ID** (`GET /usuarios/{id}`)
- **Actualizar usuario** (`PUT /usuarios/{id}`), incluyendo:
    - Nombre, alias, teléfono, avatar, descripción
    - Contraseña (solo si se envía una nueva)
- **Eliminar usuario** (`DELETE /usuarios/{id}`)
- **Reset de contraseña** (`PUT /usuarios/reset-password`)
- **Verificación de existencia de email** (`GET /usuarios/email/{email}`)

---

### ✨ Validaciones importantes

- Email y nickname únicos (`existsByEmail`, `existsByNickname`)
- Manejo correcto de errores `409 Conflict` por datos duplicados
- Manejo de errores `500` con `try/catch` y logs detallados en consola

---

### 🐳 Infraestructura (Docker)

- Contenerización con:
    - `Dockerfile` para Spring Boot
    - `docker-compose.yml` para backend + PostgreSQL

---

### 📂 Extras implementados en el backend

- Formato limpio en controladores (con separación de lógica y DTOs mínimos como `LoginRequest`)
- Control de CORS para permitir conexión desde el frontend (`http://localhost:5173`)
- Rutas protegidas con JWT (excepto `/usuarios` y `/usuarios/login`)
- Carpeta `uploads/` para manejo futuro de imágenes


## 🧰 Tecnologías utilizadas

| Herramienta               | Rol en el proyecto                                 |
|---------------------------|----------------------------------------------------|
| `Java 17`                 | Lenguaje base                                      |
| `Spring Boot 3.5`         | Framework backend principal                        |
| `Spring Web + JPA`        | Creación de API REST y conexión a la base de datos |
| `Spring Security 6.5`     | Seguridad                                          |
| `Autenticación con JWT`   | Verificación de usuarios                           |
| `PasswordEncoder (BCrypt)`| Encriptación de contraseñas                        |
| `PostgreSQL`              | Base de datos relacional                           |
| `Docker / Docker Compose` | Entorno de desarrollo portable y automatizado      |
| `Maven`                   | Sistema de construcción y dependencias             |
| `React + Material UI`     | Frontend separado (repositorio aparte)             |
| `@emoji-mart/react`       | Selector de emojis en publicaciones                |

---

## 📁 Estructura del backend

```text
red-social/
├── src/
│   └── main/
│       ├── java/com/paola/todo/
│       │   ├── controller/          # Endpoints REST (usuarios, publicaciones)
│       │   ├── model/               # Entidades: Usuario, Publicacion
│       │   ├── repository/          # Interfaces JPA
│       │   └── RedSocialApp.java    # Clase principal (main)
│       └── resources/
│           └── application.properties  # Configuración DB y puertos
├── Dockerfile                # Imagen del backend
├── docker-compose.yml        # Orquesta backend + PostgreSQL
├── rebuild.ps1               # Script PowerShell para limpiar, compilar y correr
├── pom.xml                   # Dependencias Maven
└── README.md                 # Este archivo ✨
```




---

## 🧪 Cómo levantar el proyecto (modo local remoto 🐳)

1. Clona el repositorio:

```
git clone https://github.com/paola-arreola/red-social-backend.git
cd red-social-backend 
```
2. Usa el script para compilar y levantar la app (si usas Windows PowerShell):
```
./rebuild.ps1
```
3. La app quedará disponible en:
```
🔗 API backend: http://localhost:8080
📁 Carpeta de imágenes: http://localhost:8080/uploads/
```
4. Para resetear los contenedores, puedes usar:
```
docker-compose down -v
```
🧠 ¿Por qué es parte de mi portafolio?
Demuestra habilidades fullstack reales, desde backend, base de datos hasta frontend.

Uso profesional de herramientas como Spring Boot, Docker y PostgreSQL.

Pensado como proyecto base para apps sociales, portfolios avanzados o MVPs.

Desarrollado con 💙 por Paola Arreola (@PaooxDev)
🌎 Buscando oportunidades como desarrolladora backend o fullstack Java / React.

📬 Contacto: [LinkedIn](https://www.linkedin.com/in/paola-arreola-6442a9375/) | [GitHub](https://github.com/Paoox)