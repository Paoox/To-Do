# 📝 To-do App con Spring Boot + PostgreSQL + Docker

Este proyecto es una aplicación **CRUD de tareas (todo app)** desarrollada con **Spring Boot** y **PostgreSQL**, completamente contenida con **Docker**. Su objetivo es servir como base para proyectos backend más complejos, permitiendo practicar buenas prácticas de desarrollo, configuración de base de datos y despliegue con contenedores.

---

## 🚀 ¿Qué hace esta app?

Esta aplicación permite:
- Crear, leer, actualizar y eliminar tareas.
- Conectar a una base de datos PostgreSQL desde Spring Boot.
- Usar contenedores para evitar instalaciones locales complicadas.

---

## 🧠 ¿Para qué sirve?

Este proyecto está diseñado para:
- Practicar integración de backend con base de datos.
- Aprender a contenerizar proyectos Java con Docker.
- Construir una base sólida para proyectos reales o para portafolio profesional.

---

## 🧰 Tecnologías usadas

| Herramienta         | Uso principal                                 |
|---------------------|-----------------------------------------------|
| **Java 17**         | Lenguaje base de la aplicación                |
| **Spring Boot**     | Framework principal del backend (versión 3.5) |
| **PostgreSQL**      | Base de datos relacional                      |
| **Docker / Compose**| Contenerización de app y base de datos        |
| **Maven**           | Manejo de dependencias y construcción         |
| **GitHub**          | Control de versiones y repositorio remoto     |
| **IntelliJ IDEA**   | IDE de desarrollo principal                   |

---

## 🏗️ Estructura del proyecto

```to-do/
├── src/
│ ├── main/
│ │ ├── java/com/paola/todo/
│ │ │ ├── controller/ # Endpoints REST (CRUD)
│ │ │ ├── model/ # Entidades (tablas de BD)
│ │ │ ├── repository/ # Acceso a datos (JPA)
│ │ │ └── TodoApplication.java # Clase principal (main)
│ │ └── resources/
│ │ └── application.properties # Configuración de Spring y DB
├── Dockerfile # Imagen de la app
├── docker-compose.yml # App + PostgreSQL juntos en Docker
├── pom.xml # Dependencias con Maven
└── README.md # Este archivo ✨


✨ Autor
Desarrollado por Paola Arreola (PaooxDev) 🚀
