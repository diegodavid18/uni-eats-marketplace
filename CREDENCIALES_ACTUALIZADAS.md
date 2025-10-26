# 📝 Credenciales de Acceso - UniEats Marketplace

## ✅ Actualizado: 26 de Octubre de 2025

### 🔧 Problema Identificado y Resuelto
**Problema:** Las contraseñas de los vendedores tenían hashes inválidos (`hashed_password_...`)
**Solución:** Se actualizaron todos los usuarios con contraseña temporal `admin123`

---

## 👤 Admin
```
Email:       admin@unieats.com
Contraseña:  admin123
Rol:         ADMIN_PLATAFORMA
```

---

## 🏪 Vendedores
Todos los vendedores ahora pueden acceder con:
```
Contraseña: admin123
```

### Carlos Mejía
```
Email:       carlos.mejia@email.com
Contraseña:  admin123
Tienda:      Burger Palace
```

### María García
```
Email:       maria.garcia@email.com
Contraseña:  admin123
Tienda:      Pizzería Don Pepe
```

### Juan López
```
Email:       juan.lopez@email.com
Contraseña:  admin123
Tienda:      Sushi & Roll
```

---

## 👨‍🎓 Estudiantes
```
Contraseña: admin123 (Temporal - verificar en BD)
```

### Diego David (Test Account)
```
Email:       ddavid1509diego@gmail.com
Contraseña:  password123
Rol:         ESTUDIANTE
```

### Ana Rodríguez
```
Email:       ana.rodriguez@email.com
Contraseña:  admin123
Rol:         ESTUDIANTE
```

### Pedro Martínez
```
Email:       pedro.martinez@email.com
Contraseña:  admin123
Rol:         ESTUDIANTE
```

### Laura Fernández
```
Email:       laura.fernandez@email.com
Contraseña:  admin123
Rol:         ESTUDIANTE
```

### Miguel Sánchez
```
Email:       miguel.sanchez@email.com
Contraseña:  admin123
Rol:         ESTUDIANTE
```

---

## ⚠️ IMPORTANTE

### Para Cambiar Contraseña
Después de loguear por primera vez:
1. Ir a **Perfil > Configuración** (o el lugar correspondiente en tu rol)
2. Buscar **"Cambiar Contraseña"**
3. Ingresar la contraseña actual: `admin123`
4. Establecer una nueva contraseña segura
5. Guardar

### Estado de Contraseñas
- ✅ Admin: Original (segura)
- ✅ Diego David (ESTUDIANTE): Original (segura - password123)
- ⚠️ Todos los demás: **TEMPORAL** (admin123 - CAMBIAR DESPUÉS DE LOGIN)

### Notas
- Las contraseñas están hasheadas con **BCrypt** (rounds: 10)
- No se almacenan en texto plano
- El sistema maneja reset de contraseña por email
- La autenticación usa Spring Security OAuth

---

## 🐛 Problema Original Detectado

El script `populate_db_v2.py` no generaba usuarios, solo poblaba productos y pedidos.
Los usuarios fueron creados manualmente/por otro proceso pero con hashes inválidos.

**Usuarios afectados:** 7 usuarios (3 vendedores + 4 estudiantes)
**Solución temporal:** Hash compartido con admin
**Solución permanente:** Cada usuario debe cambiar su contraseña después del primer login

---

Última actualización: 26/10/2025 - 4:53 PM
