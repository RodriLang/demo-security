# 🔐 demo-security — Spring Security Progresivo

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-6DB33F?style=flat-square&logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring%20Security-✓-6DB33F?style=flat-square&logo=springsecurity)
![JWT](https://img.shields.io/badge/JWT-jjwt-purple?style=flat-square)
![Maven](https://img.shields.io/badge/Maven-✓-C71A36?style=flat-square&logo=apachemaven)

Proyecto de ejemplo desarrollado para la **Tecnicatura Universitaria en Programación — UTN**, pensado para enseñar seguridad en APIs REST con Spring Boot + Spring Security de manera progresiva.

La idea del repositorio es mostrar cómo evoluciona una aplicación desde una autenticación básica hasta una implementación moderna con JWT.

---

## 🎯 Objetivo del proyecto

Este proyecto fue armado con fines educativos para que los estudiantes puedan:

- Entender cómo funciona Spring Security internamente.
- Aprender autenticación y autorización paso a paso.
- Comparar distintos enfoques de seguridad.
- Ver la evolución desde una configuración mínima hasta JWT.
- Practicar roles, filtros y manejo de usuarios.

---

## 🛠️ Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| Java 21 | Lenguaje base |
| Spring Boot | Framework principal |
| Spring Security | Seguridad y autenticación |
| Spring Data JPA | Acceso a datos |
| Hibernate | ORM |
| Lombok | Reducción de boilerplate |
| JWT (jjwt) | Tokens de autenticación |
| Maven | Gestión de dependencias |
| MySQL | Base de datos |

---

## 🌿 Estructura de ramas

El proyecto está dividido en ramas para mostrar una evolución progresiva de la seguridad.

### `main` — Versión completa

Contiene la versión más completa y actual del proyecto.

Incluye:
- JWT
- Roles
- Filtros personalizados
- Manejo de usuarios
- Autorización por endpoints

---

### `basic-auth-default` — Etapa 1: Seguridad por defecto

Primer acercamiento a Spring Security.

**Contenido:**
- Dependencia `spring-boot-starter-security`
- Seguridad por defecto de Spring
- Login con usuario generado automáticamente
- Uso de Basic Auth

**Objetivo pedagógico:** entender qué sucede al agregar Spring Security, cómo protege automáticamente todos los endpoints y cómo funciona Basic Authentication.

**Credenciales:** Spring genera automáticamente:
- Usuario: `user`
- Password: se muestra en consola al iniciar la aplicación

```
Using generated security password: 4d8c6e7b-xxxx
```

---

### `basic-auth-credentials` — Etapa 2: Configuración manual

**Contenido:**
- Usuario y contraseña configurados manualmente
- Configuración explícita de `SecurityFilterChain`
- Uso de roles (`USER`, `ADMIN`)
- Restricción de endpoints según permisos

**Objetivo pedagógico:** aprender configuración básica de seguridad, autorización por roles, diferencia entre autenticación y autorización, y uso de `hasRole()` / `hasAnyRole()`.

**Ejemplo:**
```java
.requestMatchers(HttpMethod.GET, "/api/games")
    .hasAnyRole("USER", "ADMIN")

.requestMatchers("/api/games/**")
    .hasRole("ADMIN")
```

---

### `jwt-token` — Etapa 3: Autenticación con JWT

**Contenido:**
- Login con JWT
- Generación de tokens
- Filtro JWT personalizado (`OncePerRequestFilter`)
- Stateless Authentication
- Roles dentro del token
- Endpoints públicos y protegidos

**Objetivo pedagógico:** comprender qué es un JWT, cómo funciona una API stateless, diferencia entre Basic Auth y JWT, uso de filtros en Spring Security y seguridad moderna en APIs REST.

**Flujo de autenticación:**

```
1. El usuario hace POST /api/auth/login
2. El backend valida las credenciales
3. Se genera y devuelve un JWT
4. El cliente envía el token en cada request
```

**Header requerido:**
```
Authorization: Bearer eyJhbGciOi...
```

---
## `refresh-token` — Etapa 4: Refresh Tokens y renovación de sesión

**Contenido:**

- Access Token + Refresh Token
- Renovación automática de sesión
- Revocación de refresh tokens
- Logout con invalidación de tokens
- Persistencia de refresh tokens en base de datos
- JWT stateless
- Manejo centralizado de errores de autenticación
- AuthenticationEntryPoint
- Autorización con @PreAuthorize

**Objetivo pedagógico:** comprender cómo funcionan los refresh tokens en aplicaciones modernas, diferencia entre access token y refresh token, renovación segura de sesiones, invalidación de tokens y separación entre autenticación y autorización.

**Flujo de autenticación completo:**

```
1. El usuario realiza login
2. El backend devuelve:
   - access token
   - refresh token

3. El cliente utiliza el access token para acceder a endpoints protegidos

4. Cuando el access token expira:
   - el cliente envía el refresh token
   - el backend valida el refresh token
   - el backend genera nuevos tokens

5. Durante logout:
   - el refresh token es revocado
   - la sesión queda invalidada
```

**Diferencia entre tokens:**
| Tipo | Uso | Duración |
|---|---|---|
| Access Token | Acceder a endpoints protegidos | Corta |
| Refresh Token | Renovar sesión | Más larga |

## 📚 Conceptos trabajados

### Seguridad básica
- Authentication
- Authorization
- Basic Auth
- Usuarios y contraseñas

### Seguridad intermedia
- Roles y `GrantedAuthority`
- `UserDetails` / `UserDetailsService`
- `PasswordEncoder`

### Seguridad avanzada
- JWT y Claims
- Filtros personalizados con `OncePerRequestFilter`
- Stateless APIs
- Tokens en headers

### Seguridad nivel Produción
- Refresh Tokens
- Revocación de tokens
- Rotación de refresh tokens
- APIs stateless
- AuthenticationEntryPoint
- @PreAuthorize
- Manejo de excepciones en filtros
- Persistencia de sesiones seguras
- Seguridad moderna en APIs REST

---

## 🚀 Cómo ejecutar el proyecto

**1. Clonar el repositorio:**

```bash
git clone https://github.com/RodriLang/demo-security.git
cd demo-security
```

**2. Cambiar a la rama deseada:**

```bash
git checkout basic-auth-default
# o
git checkout basic-auth-credentials
# o
git checkout jwt-token
# o
git checkout refresh-token
```

**3. Ejecutar la aplicación:**

```bash
mvn spring-boot:run
```

---

## 🔌 Endpoints de ejemplo

### Públicos
```
POST /api/auth/login
POST /api/auth/register
```

### Protegidos
```
GET    /api/games
POST   /api/games
DELETE /api/games/{id}
```

---

## 🧪 Herramientas recomendadas para probar

- [Postman](https://www.postman.com/)
- [Insomnia](https://insomnia.rest/)
- [Bruno](https://www.usebruno.com/)
- Swagger / OpenAPI (integrado en el proyecto)

---

## 📋 Recomendación para estudiantes

Se recomienda recorrer las ramas en este orden para entender cómo evoluciona la seguridad paso a paso:

```
1️⃣  basic-auth-default
2️⃣  basic-auth-credentials
3️⃣  jwt-token
4️⃣  refresh-token
5️⃣  main
```

---

## 👨‍💻 Autor

**Rodrigo Lang** — [@RodriLang](https://github.com/RodriLang)

Desarrollado con fines educativos para la UTN — Tecnicatura Universitaria en Programación.
