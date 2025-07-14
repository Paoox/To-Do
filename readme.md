# 🌟 Mini Red Social con Spring Boot + PostgreSQL + Docker

Este proyecto es una **mini red social** construida desde cero como parte de mi portafolio **fullstack**, usando **Java con Spring Boot** en el backend, **PostgreSQL** como base de datos, y todo totalmente contenido con **Docker**.  
Aquí puedes crear publicaciones, dar corazones 💖, ver perfiles de usuario y (muy pronto) enviar solicitudes de amistad 🤝.

---

## 🚀 ¿Qué hace esta app?

Esta aplicación permite:
- Crear, leer, actualizar y eliminar publicaciones (`Post`).
- Asociar cada post a un usuario con imagen de perfil.
- Dar likes (💖) a publicaciones como en una red social.
- Gestionar usuarios y sus relaciones.
- Simular funciones sociales como agregar amistades. *(Próximamente)*

---

## 🧠 ¿Para qué sirve?

Este proyecto está diseñado para:
- Demostrar habilidades como **desarrolladora fullstack**.
- Mostrar relaciones entre entidades con JPA.
- Practicar APIs REST bien estructuradas.
- Usar Docker para desarrollo profesional.
- Servir como base para otros proyectos o MVPs más complejos.

---

## 🧰 Tecnologías usadas

| Herramienta         | Uso principal                                       |
|---------------------|-----------------------------------------------------|
| **Java 17**         | Lenguaje base de la aplicación                      |
| **Spring Boot**     | Framework backend (versión 3.5)                     |
| **PostgreSQL**      | Base de datos relacional                            |
| **Docker / Compose**| Contenerización de la app y base de datos           |
| **Maven**           | Gestión de dependencias y construcción del proyecto |
| **HTML + JS**       | Frontend básico para interacción                    |
| **GitHub**          | Control de versiones y visibilidad pública          |
| **IntelliJ IDEA**   | IDE de desarrollo principal                         |

---

## 🏗️ Estructura del proyecto

```red-social/
├── src/
│   └── main/
│       ├── java/com/paola/redsocial/
│       │   ├── controller/        # Endpoints REST (usuarios, posts, likes)
│       │   ├── model/             # Entidades: Usuario, Post, Like, Amistad
│       │   ├── repository/        # Interfaces JPA
│       │   └── RedSocialApp.java  # Clase main
│       └── resources/
│           └── application.properties  # Config DB y Spring
├── Dockerfile              # Imagen Docker de la app
├── docker-compose.yml      # Orquesta app + PostgreSQL
├── rebuild.ps1 / .sh       # Script para recompilar y levantar servicios
├── pom.xml                 # Dependencias Maven
└── README.md               # Este archivo ✨
```

🎨 Vista del frontend (en progreso)
Un frontend sencillo que permite:

Ver publicaciones en formato "feed"

Dar likes con 💖

Ver perfil de cada usuario

Agregar solicitudes de amistad (proximamente)

✨ Autor
Desarrollado por Paola Arreola (@PaooxDev) como parte de su portafolio fullstack 🚀
Buscando oportunidades para seguir creciendo como desarrolladora backend o fullstack 💼💻

