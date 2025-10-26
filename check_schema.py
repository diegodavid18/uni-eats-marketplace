#!/usr/bin/env python3
import psycopg2

# Database connection
DB_HOST = "localhost"
DB_PORT = 5432
DB_NAME = "unieats_marketplace"
DB_USER = "postgres"
DB_PASSWORD = "2010"

try:
    conn = psycopg2.connect(
        host=DB_HOST,
        port=DB_PORT,
        database=DB_NAME,
        user=DB_USER,
        password=DB_PASSWORD
    )
    cursor = conn.cursor()
    
    # Get all tables
    cursor.execute("""
        SELECT table_name 
        FROM information_schema.tables 
        WHERE table_schema='public' 
        ORDER BY table_name
    """)
    
    tables = cursor.fetchall()
    print("\n📋 TABLAS EN LA BD:\n")
    for table in tables:
        print(f"  - {table[0]}")
    
    # Get table structures
    print("\n" + "=" * 70)
    print("ESTRUCTURA DE TABLAS")
    print("=" * 70)
    
    for table in tables:
        table_name = table[0]
        cursor.execute(f"""
            SELECT column_name, data_type, is_nullable
            FROM information_schema.columns
            WHERE table_name = '{table_name}'
            ORDER BY ordinal_position
        """)
        
        columns = cursor.fetchall()
        print(f"\n📊 {table_name.upper()}:")
        for col in columns:
            col_name, col_type, nullable = col
            print(f"  - {col_name}: {col_type} ({'NULL' if nullable == 'YES' else 'NOT NULL'})")
    
    cursor.close()
    conn.close()
    
except Exception as e:
    print(f"✗ Error: {e}")
