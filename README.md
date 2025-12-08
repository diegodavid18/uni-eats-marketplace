# 🍕 Uni-Eats Marketplace

Un sistema de marketplace completo para la comunidad universitaria, desarrollado con Spring Boot y tecnologías web modernas.

## ✨ Características Principales

### 🔐 Sistema de Autenticación Completo
- **Registro de usuarios** con validaciones exhaustivas
- **Login seguro** con manejo de sesiones
- **Recuperación de contraseñas** por correo electrónico
- **Protección CSRF** configurada

### 📝 Validaciones Avanzadas del Formulario de Registro
- **Validación en tiempo real** para todos los campos
- **Contraseña segura** con indicadores visuales de fortaleza
- **Validación dual** (frontend + backend) para máxima seguridad
- **Feedback visual** con colores y mensajes específicos de error

#### Validaciones por Campo:
- **Nombre/Apellido**: Solo letras, 2-30/40 caracteres, sin espacios múltiples
- **Cédula**: Solo números, 6-12 dígitos, validación de formato
- **Teléfono**: Opcional, números colombianos válidos (móviles inician con 3)
- **Correo**: Formato email estándar, máximo 100 caracteres
- **Contraseña**: Mínimo 8 caracteres, mayúscula, minúscula, número y símbolo especial

### 🎨 Interfaz de Usuario Moderna
- **Diseño responsivo** con Tailwind CSS
- **Etiquetas flotantes** para mejor UX
- **Transiciones suaves** y animaciones CSS
- **Temas visuales** consistentes

## 🛠️ Tecnologías Utilizadas

### Backend
- **Spring Boot 3.5.6**
- **Spring Security** (autenticación y autorización)
- **Spring Data JPA** (persistencia de datos)
- **PostgreSQL** (base de datos)
- **Thymeleaf** (motor de plantillas)

### Frontend
- **HTML5** con validaciones nativas
- **JavaScript ES6+** para validaciones en tiempo real
- **Tailwind CSS** para estilos modernos
- **Font Awesome** para iconografía

### Herramientas de Desarrollo
- **Maven** (gestión de dependencias)
- **Git** (control de versiones)
- **Java 21** (lenguaje principal)

## 🚀 Instalación y Configuración

### Prerrequisitos
- Java 21 o superior
- PostgreSQL 12 o superior
- Maven 3.8 o superior

### Pasos de Instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/TU_USUARIO/uni-eats-marketplace.git
   cd uni-eats-marketplace
   ```

2. **Configurar la base de datos**
   - Crear una base de datos PostgreSQL
   - Actualizar `src/main/resources/application.properties` con tus credenciales:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/tu_base_de_datos
   spring.datasource.username=tu_usuario
   spring.datasource.password=tu_contraseña
   ```

3. **Compilar y ejecutar**
   ```bash
   ./mvnw clean compile
   ./mvnw spring-boot:run
   ```

4. **Acceder a la aplicación**
   - Abrir navegador en: `http://localhost:8092`

## 📱 Funcionalidades Implementadas

### ✅ Sistema de Usuarios
- [x] Registro de estudiantes con validaciones completas
- [x] Login con autenticación segura
- [x] Recuperación de contraseñas
- [x] Gestión de sesiones
- [x] Páginas de error personalizadas

### ✅ Validaciones del Formulario
- [x] Validación en tiempo real (JavaScript)
- [x] Validación del servidor (Spring Boot)
- [x] Indicadores visuales de error
- [x] Barra de progreso de contraseña segura
- [x] Mensajes de error específicos y descriptivos

### ✅ Seguridad
- [x] Protección CSRF
- [x] Validación de entrada (sanitización)
- [x] Encriptación de contraseñas
- [x] Sesiones seguras

## 🎯 Próximas Funcionalidades

- [ ] Dashboard de vendedores
- [ ] Catálogo de productos
- [ ] Sistema de pedidos
- [ ] Procesamiento de pagos
- [ ] Sistema de calificaciones
- [ ] Notificaciones en tiempo real

## 📝 Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/remington/unieats/marketplace/
│   │   ├── controller/     # Controladores web
│   │   ├── dto/           # Objetos de transferencia de datos
│   │   ├── entity/        # Entidades JPA
│   │   ├── service/       # Lógica de negocio
│   │   └── config/        # Configuraciones
│   └── resources/
│       ├── templates/     # Plantillas Thymeleaf
│       ├── static/        # Recursos estáticos (CSS, JS, imágenes)
│       └── application.properties
└── test/                  # Pruebas unitarias e integración
```

## 🤝 Contribuir

1. Fork el proyecto
2. Crear una rama para tu funcionalidad (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## 👥 Autores

- **Diego Alejandro David** - *Desarrollo principal* - [@diegodavid18]

## 🙏 Agradecimientos

- Corporación Universitaria Remington
- Comunidad Spring Boot
- Desarrolladores de Tailwind CSS

---

⭐ ¡No olvides dar una estrella al proyecto si te fue útil!
