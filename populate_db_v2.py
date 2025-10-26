#!/usr/bin/env python3
import psycopg2
from datetime import datetime, timedelta
import random
import json

# Database connection
DB_HOST = "localhost"
DB_PORT = 5432
DB_NAME = "unieats_marketplace"
DB_USER = "postgres"
DB_PASSWORD = "2010"

# Connect to database
conn = psycopg2.connect(
    host=DB_HOST,
    port=DB_PORT,
    database=DB_NAME,
    user=DB_USER,
    password=DB_PASSWORD
)
cursor = conn.cursor()

print("=" * 70)
print("POBLANDO BASE DE DATOS CON DATOS COHERENTES")
print("=" * 70)

try:
    # Get existing usuarios
    cursor.execute("SELECT id_usuario, correo FROM usuarios WHERE activo = true")
    usuarios_existentes = cursor.fetchall()
    usuario_ids = {correo: uid for uid, correo in usuarios_existentes}
    
    print(f"\n👥 Usuarios encontrados: {len(usuarios_existentes)}")
    for uid, correo in usuarios_existentes:
        print(f"  - {correo} (ID: {uid})")
    
    # Get existing tiendas
    cursor.execute("SELECT id_tienda, nombre FROM tiendas WHERE estado = 'ACTIVA'")
    tiendas = cursor.fetchall()
    tienda_ids = {nombre: tid for tid, nombre in tiendas}
    
    print(f"\n🏪 Tiendas encontradas: {len(tiendas)}")
    for tid, nombre in tiendas:
        print(f"  - {nombre} (ID: {tid})")
    
    # ==================== PRODUCTOS ====================
    print("\n🍔 Verificando productos...")
    
    productos_data = {
        "Burger Palace": [
            ("Hamburguesa Clásica", "Hamburguesa con carne, queso y verduras", 15000, "/uploads/productos/hamburguesa_clasica.jpg"),
            ("Hamburguesa Doble", "Doble carne, doble queso y salsas especiales", 18000, "/uploads/productos/hamburguesa_doble.jpg"),
            ("Papas Francesas", "Papas crujientes con sal y salsa", 8000, "/uploads/productos/papas_francesas.jpg"),
            ("Refresco Grande", "Bebida refrescante de 500ml", 5000, "/uploads/productos/refresco.jpg"),
            ("Combo Burger", "Hamburguesa + Papas + Refresco", 25000, "/uploads/productos/combo_burger.jpg"),
        ],
        "Pizzería Don Pepe": [
            ("Pizza Margarita", "Tomate, mozzarella y albahaca", 20000, "/uploads/productos/pizza_margarita.jpg"),
            ("Pizza Pepperoni", "Mozzarella, queso y pepperoni", 22000, "/uploads/productos/pizza_pepperoni.jpg"),
            ("Pizza Vegetariana", "Champiñones, cebolla, pimentón y olivas", 19000, "/uploads/productos/pizza_vegetariana.jpg"),
            ("Empanada de Carne", "Empanada rellena de carne molida", 7000, "/uploads/productos/empanada.jpg"),
            ("Garlic Bread", "Pan de ajo con queso derretido", 8000, "/uploads/productos/garlic_bread.jpg"),
        ],
        "Sushi & Roll": [
            ("Sushi Philadelphia", "Salmón, crema de queso y aguacate", 24000, "/uploads/productos/sushi_philadelphia.jpg"),
            ("Sushi California", "Cangrejo, aguacate y pepino", 22000, "/uploads/productos/sushi_california.jpg"),
            ("Sushi Picante", "Salmón, mayo picante y jalapeño", 23000, "/uploads/productos/sushi_picante.jpg"),
            ("Gyoza (6 pzas)", "Dumplings de cerdo o verdura", 12000, "/uploads/productos/gyoza.jpg"),
            ("Té Verde", "Bebida refrescante tradicional", 5000, "/uploads/productos/te_verde.jpg"),
        ]
    }
    
    producto_ids = {}
    for tienda_nombre, productos in productos_data.items():
        if tienda_nombre in tienda_ids:
            tienda_id = tienda_ids[tienda_nombre]
            for nombre, desc, precio, imagen in productos:
                cursor.execute("SELECT id_producto FROM productos WHERE nombre = %s AND tienda_id = %s", (nombre, tienda_id))
                if not cursor.fetchone():
                    cursor.execute("""
                        INSERT INTO productos (nombre, descripcion, precio, tienda_id, disponible, imagen_url)
                        VALUES (%s, %s, %s, %s, %s, %s)
                        RETURNING id_producto
                    """, (nombre, desc, precio, tienda_id, True, imagen))
                    
                    producto_id = cursor.fetchone()[0]
                    producto_ids[nombre] = producto_id
                    print(f"  ✓ {nombre} - ${precio}")
                else:
                    cursor.execute("SELECT id_producto FROM productos WHERE nombre = %s AND tienda_id = %s", (nombre, tienda_id))
                    producto_ids[nombre] = cursor.fetchone()[0]
    
    conn.commit()
    
    # ==================== PEDIDOS ====================
    print("\n📦 Creando pedidos...")
    
    comprador_emails = list(usuario_ids.keys())
    tiendas_list = list(tienda_ids.items())
    
    # Ensure we have compradores
    if not comprador_emails:
        print("  ✗ No hay usuarios para crear pedidos")
    else:
        for i in range(12):  # 12 orders
            comprador_id = usuario_ids[random.choice(comprador_emails)]
            tienda_nombre, tienda_id = random.choice(tiendas_list)
            estado = random.choice(["COMPLETADO", "COMPLETADO", "COMPLETADO", "EN_PREPARACION", "PENDIENTE"])
            fecha = datetime.now() - timedelta(days=random.randint(1, 30))
            
            cursor.execute("""
                INSERT INTO pedidos (comprador_id, tienda_id, estado, fecha_creacion, total)
                VALUES (%s, %s, %s, %s, %s)
                RETURNING id
            """, (comprador_id, tienda_id, estado, fecha, 0))
            
            pedido_id = cursor.fetchone()[0]
            
            # Add details
            productos_tienda = [p for p in productos_data.get(tienda_nombre, [])]
            if productos_tienda:
                for _ in range(random.randint(1, 3)):
                    producto_nombre, _, precio, _ = random.choice(productos_tienda)
                    if producto_nombre in producto_ids:
                        cantidad = random.randint(1, 3)
                        total_producto = precio * cantidad
                        
                        cursor.execute("""
                            INSERT INTO detalles_pedido (pedido_id, producto_id, cantidad, precio_unitario)
                            VALUES (%s, %s, %s, %s)
                        """, (pedido_id, producto_ids[producto_nombre], cantidad, precio))
            
            # Update total
            cursor.execute("""
                UPDATE pedidos 
                SET total = (SELECT SUM(cantidad * precio_unitario) FROM detalles_pedido WHERE pedido_id = %s)
                WHERE id = %s
            """, (pedido_id, pedido_id))
            
            print(f"  ✓ Pedido #{pedido_id} en {tienda_nombre}")
        
        conn.commit()
    
    # ==================== CUSTOMER PROFILES ====================
    print("\n📊 Creando perfiles de cliente...")
    
    for correo_comprador, usuario_id in usuario_ids.items():
        cursor.execute("SELECT id FROM customer_profiles WHERE usuario_id = %s", (usuario_id,))
        if not cursor.fetchone():
            cursor.execute("""
                SELECT COUNT(*), COALESCE(SUM(total), 0) FROM pedidos WHERE comprador_id = %s
            """, (usuario_id,))
            total_orders, total_spent = cursor.fetchone()
            
            cursor.execute("""
                INSERT INTO customer_profiles 
                (usuario_id, total_purchases, total_spent, segment, created_at)
                VALUES (%s, %s, %s, %s, %s)
            """, (usuario_id, total_orders, float(total_spent or 0), "REGULAR", datetime.now()))
            
            print(f"  ✓ Perfil para {correo_comprador}")
    
    conn.commit()
    
    # ==================== LOYALTY POINTS ====================
    print("\n⭐ Creando puntos de lealtad...")
    
    for correo_comprador, usuario_id in usuario_ids.items():
        cursor.execute("SELECT id FROM loyalty_points WHERE usuario_id = %s", (usuario_id,))
        if not cursor.fetchone():
            puntos = random.randint(100, 1000)
            cursor.execute("""
                INSERT INTO loyalty_points 
                (usuario_id, puntos_totales, puntos_disponibles, nivel_tier, created_at)
                VALUES (%s, %s, %s, %s, %s)
            """, (usuario_id, puntos, puntos, "BRONCE", datetime.now()))
            
            print(f"  ✓ {puntos} puntos para {correo_comprador}")
    
    conn.commit()
    
    # ==================== DESCUENTOS Y CUPONES ====================
    print("\n🎁 Creando cupones y descuentos...")
    
    cupones_data = [
        ("STUDENT20", "Descuento para estudiantes", 0.20, "PORCENTAJE", 50000, 5),
        ("BURGER15", "Descuento en Burger Palace", 0.15, "PORCENTAJE", 0, 3),
        ("PIZZA10K", "Descuento pizza $10.000", 10000, "MONTO", 20000, 2),
        ("BIENVENIDA", "Cupón de bienvenida", 5000, "MONTO", 0, 1),
    ]
    
    for codigo, desc, valor, tipo, minimo, usos_maximos in cupones_data:
        cursor.execute("SELECT id FROM coupons WHERE codigo = %s", (codigo,))
        if not cursor.fetchone():
            cursor.execute("""
                INSERT INTO coupons 
                (codigo, descripcion, valor_descuento, tipo_descuento, minimo_compra, cantidad_maxima, activo, fecha_inicio, created_at)
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
            """, (codigo, desc, valor, tipo, minimo, usos_maximos, True, datetime.now(), datetime.now()))
            
            print(f"  ✓ {codigo} - {desc}")
    
    conn.commit()
    
    print("\n" + "=" * 70)
    print("✓ BASE DE DATOS POBLADA EXITOSAMENTE")
    print("=" * 70)
    print(f"\n📊 Resumen:")
    print(f"  - Usuarios: {len(usuario_ids)}")
    print(f"  - Tiendas: {len(tienda_ids)}")
    print(f"  - Productos: {len(producto_ids)}")
    print(f"  - Pedidos: 12")
    print(f"  - Cupones: 4")
    
except Exception as e:
    conn.rollback()
    print(f"\n✗ Error: {e}")
    import traceback
    traceback.print_exc()

finally:
    cursor.close()
    conn.close()
