# 🎯 ACCESO RÁPIDO - UniEats Marketplace

## 🚀 INICIAR APLICACIÓN

### Opción 1: Línea de Comandos (Recomendado)
```bash
cd c:\Users\jero\Desktop\marketplace
.\mvnw.cmd spring-boot:run
```

**Espera:** ~15-20 segundos para que compile y arranque

### Opción 2: IDE (VS Code)
1. Abre el proyecto en VS Code
2. Presiona `F5` para ejecutar
3. Terminal mostrará: "Tomcat started on port 8092"

---

## 🌐 ACCESO AL SISTEMA

| Servicio | URL | Usuario | Contraseña |
|----------|-----|---------|------------|
| **Marketplace** | http://localhost:8092 | (no requerido) | - |
| **H2 Console** | http://localhost:8092/h2-console | sa | (vacío) |

---

## 👤 USUARIOS DE PRUEBA

### Crear nuevos usuarios
1. Ve a: http://localhost:8092/registro
2. Completa formulario
3. Elige rol: ESTUDIANTE, VENDEDOR, o ADMIN

### Usuario Admin (si existe)
```
Email: admin@unieats.com
Contraseña: [verificar en BD]
```

---

## 🗄️ BASE DE DATOS

### Conectarse a PostgreSQL
```powershell
$env:PGPASSWORD='2010'
& "C:\Program Files\PostgreSQL\17\bin\psql.exe" -h localhost -U postgres -d unieats_marketplace
```

### Comandos Útiles
```sql
-- Ver todas las tablas
\dt

-- Ver estructura de tabla
\d usuarios

-- Contar registros
SELECT COUNT(*) FROM usuarios;

-- Ver últimas compras
SELECT * FROM pedidos ORDER BY created_at DESC LIMIT 10;
```

---

## 📊 DASHBOARDS

### Power BI (si está configurado)
```
URL: http://localhost:8092/analytics/dashboard
```

---

## 🔍 LOGS Y DEBUGGING

### Ver logs en tiempo real
1. La terminal mostrará todos los logs
2. Busca por "ERROR", "WARN" si hay problemas

### Debug en VS Code
1. Presiona `Ctrl+Shift+D` para abriri Debug
2. Click en Play para ejecutar en modo debug

---

## ❌ PROBLEMAS COMUNES

### "Puerto 8092 ya está en uso"
```bash
# Encontrar proceso en puerto 8092
netstat -ano | findstr :8092

# Terminar proceso (reemplaza PID)
taskkill /PID <PID> /F
```

### "No se puede conectar a PostgreSQL"
1. Verifica que PostgreSQL esté corriendo:
   - Busca "Services" en Windows
   - Busca "PostgreSQL Database Server"
   - Debe estar en estado "Running"

2. Prueba conexión:
   ```bash
   $env:PGPASSWORD='2010'
   & "C:\Program Files\PostgreSQL\17\bin\psql.exe" -h localhost -U postgres
   ```

### "Base de datos no existe"
```bash
# Crear la base de datos
$env:PGPASSWORD='2010'
& "C:\Program Files\PostgreSQL\17\bin\psql.exe" -h localhost -U postgres -c "CREATE DATABASE unieats_marketplace;"
```

---

## 📁 ESTRUCTURA IMPORTANTE

```
marketplace/
├── src/main/
│   ├── java/                    # Código Java
│   │   └── com/remington/unieats/marketplace/
│   │       ├── controller/      # REST Controllers
│   │       ├── service/         # Lógica de negocio
│   │       ├── repository/      # Acceso a datos
│   │       └── entity/          # Modelos JPA
│   └── resources/
│       ├── application.properties  # ⭐ CONFIG ACTIVA
│       ├── static/              # JS, CSS, imágenes
│       └── templates/           # HTML Thymeleaf
├── pom.xml                      # Dependencias Maven
└── mvnw.cmd                     # Maven Wrapper (para Windows)
```

---

## 📚 DOCUMENTACIÓN

Archivos de referencia creados:
- **ESTADO_PROYECTO.md** - Estado actual completo
- **POSTGRESQL_COMMANDS.md** - Comandos SQL útiles
- **CRM_IMPLEMENTATION_PLAN.md** - Plan de implementación CRM
- **STATUS_FINAL.md** - Resumen de sesión

---

## ⌨️ ATAJOS ÚTILES

| Acción | Comando |
|--------|---------|
| Compilar | `mvn clean compile` |
| Hacer tests | `mvn test` |
| Package JAR | `mvn clean package` |
| Limpiar | `mvn clean` |
| Ver dependencias | `mvn dependency:tree` |

---

## 💡 TIPS

1. **Hot Reload:** Spring DevTools reinicia automáticamente con cambios
2. **Logs SQL:** Si necesitas ver queries: `logging.level.org.hibernate.SQL=DEBUG`
3. **Performance:** Usa `show_sql=true` en application.properties para debugging

---

## 🆘 SUPPORT

Si algo no funciona:
1. ✅ Verifica que PostgreSQL esté corriendo
2. ✅ Verifica que el puerto 8092 esté libre
3. ✅ Limpia Maven: `mvn clean`
4. ✅ Reconstruye: `mvn install`
5. ✅ Reinicia la aplicación

---

**Última actualización:** 26-Oct-2025  
**Versión:** 1.0-SNAPSHOT  
**Status:** ✅ Operacional
