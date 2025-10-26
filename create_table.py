#!/usr/bin/env python3
import psycopg2
import sys

try:
    conn = psycopg2.connect(
        host="localhost",
        port=5432,
        database="unieats_marketplace",
        user="postgres",
        password="2010"
    )
    cursor = conn.cursor()
    
    sql = """
    CREATE TABLE IF NOT EXISTS email_templates (
        id BIGSERIAL PRIMARY KEY,
        nombre VARCHAR(100) UNIQUE NOT NULL,
        descripcion TEXT,
        asunto_template VARCHAR(200),
        contenido_html TEXT NOT NULL,
        variables_disponibles TEXT,
        categoria VARCHAR(50),
        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP
    )
    """
    
    cursor.execute(sql)
    conn.commit()
    cursor.close()
    conn.close()
    print("✓ Tabla email_templates creada exitosamente")
    sys.exit(0)
except Exception as e:
    print(f"✗ Error creando tabla: {e}")
    sys.exit(1)
