# Referencia PostgreSQL Local

## Conexión a la Base de Datos

```powershell
# Configurar contraseña en variable de entorno
$env:PGPASSWORD='2010'

# Conectarse a la base de datos
& "C:\Program Files\PostgreSQL\17\bin\psql.exe" -h localhost -U postgres -d unieats_marketplace
```

## Comandos psql útiles

```sql
-- Ver todas las tablas
\dt

-- Ver estructura de una tabla
\d usuarios

-- Ver todas las secuencias (auto-incrementos)
\ds

-- Ver columnas de una tabla
\d+ productos

-- Listar bases de datos
\l

-- Cambiar a otra base de datos
\c nombrebd

-- Ejecutar query
SELECT * FROM usuarios;

-- Ver índices
\di

-- Ver constraints
\d usuarios
```

## Queries útiles

```sql
-- Contar registros por tabla
SELECT tablename, (SELECT COUNT(*) FROM ONLY <table>) as count
FROM pg_tables WHERE schemaname='public';

-- Ver estructura de la tabla usuarios
SELECT column_name, data_type, is_nullable FROM information_schema.columns 
WHERE table_name = 'usuarios';

-- Ver todas las columnas de usuarios
\d usuarios

-- Listar todas las tablas con sus tamaños
SELECT tablename, pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename))
FROM pg_tables WHERE schemaname='public' ORDER BY tablename;
```

## Backup y Restore

```bash
# Hacer backup
pg_dump -h localhost -U postgres unieats_marketplace > backup.sql

# Restaurar
psql -h localhost -U postgres unieats_marketplace < backup.sql
```

## Reset de secuencias (si es necesario)

```sql
-- Reiniciar contador de ID de una tabla
ALTER SEQUENCE usuarios_id_seq RESTART WITH 1;
```

## Información de conexión

- **Host:** localhost
- **Puerto:** 5432
- **Usuario:** postgres
- **Contraseña:** 2010
- **Base de datos:** unieats_marketplace
