#!/usr/bin/env python3
import psycopg2
import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart

# Database connection
DB_HOST = "localhost"
DB_PORT = 5432
DB_NAME = "unieats_marketplace"
DB_USER = "postgres"
DB_PASSWORD = "2010"

# Gmail SMTP configuration
GMAIL_USER = "dvdavid2509vargs@gmail.com"
GMAIL_PASSWORD = "pcqx nzex uhut mdvw"
GMAIL_SMTP = "smtp.gmail.com"
GMAIL_PORT = 587

# Target email
TARGET_EMAIL = "ddavid1509diego@gmail.com"

print("=" * 70)
print("BÚSQUEDA Y ENVÍO DE PLANTILLA A USUARIO")
print("=" * 70)

# Step 1: Search for user in database
print("\n📋 PASO 1: BUSCANDO USUARIO EN LA BD")
print("-" * 70)

try:
    conn = psycopg2.connect(
        host=DB_HOST,
        port=DB_PORT,
        database=DB_NAME,
        user=DB_USER,
        password=DB_PASSWORD
    )
    cursor = conn.cursor()
    
    cursor.execute("SELECT id_usuario, nombre, correo, activo FROM usuarios WHERE correo = %s", (TARGET_EMAIL,))
    user = cursor.fetchone()
    
    if user:
        user_id, user_name, user_email, is_active = user
        print(f"✓ Usuario encontrado:")
        print(f"  - ID: {user_id}")
        print(f"  - Nombre: {user_name}")
        print(f"  - Email: {user_email}")
        print(f"  - Activo: {is_active}")
    else:
        print(f"❌ Usuario NO encontrado: {TARGET_EMAIL}")
        cursor.close()
        conn.close()
        exit(1)
    
    cursor.close()
    conn.close()
    
except Exception as e:
    print(f"✗ Error en la búsqueda: {e}")
    exit(1)

# Step 2: Send email with template
print("\n📧 PASO 2: ENVIANDO PLANTILLA POR EMAIL")
print("-" * 70)

HTML_CONTENT = '''<html><body style="font-family: Arial, sans-serif;">
<div style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 40px; text-align: center; border-radius: 10px;">
    <h1 style="margin: 0; font-size: 28px;">¡El hambre se hace grande!</h1>
    <h2 style="margin: 10px 0 20px 0; font-size: 22px;">Como las ganas de estudiar</h2>
    <p style="margin: 15px 0;">Bienvenido a <strong>UniEats</strong>, tu marketplace de comida universitario</p>
    <p style="margin: 15px 0;">Encontrarás comida deliciosa a precios diseñados para estudiantes como tú.</p>
    <div style="background: white; color: #667eea; padding: 15px; margin-top: 20px; border-radius: 5px;">
        <p><strong>Con UniEats:</strong></p>
        <ul style="text-align: left;">
            <li>✓ Comida rápida y de calidad</li>
            <li>✓ Precios universitarios</li>
            <li>✓ Entregas rápidas</li>
            <li>✓ Variedad de opciones</li>
        </ul>
    </div>
    <p style="margin-top: 20px; font-size: 12px; opacity: 0.9;">¡Descarga la app y empieza a disfrutar!</p>
</div></body></html>'''

SUBJECT = "🍔 Prueba UniEats - El hambre y la dedicación"

try:
    print(f"De: {GMAIL_USER}")
    print(f"Para: {TARGET_EMAIL}")
    print(f"Asunto: {SUBJECT}")
    
    # Create the email message
    msg = MIMEMultipart('alternative')
    msg['Subject'] = SUBJECT
    msg['From'] = GMAIL_USER
    msg['To'] = TARGET_EMAIL
    
    # Add HTML part
    html_part = MIMEText(HTML_CONTENT, 'html', 'utf-8')
    msg.attach(html_part)
    
    # Send the email
    print("\n📧 Conectando a SMTP Gmail...")
    server = smtplib.SMTP(GMAIL_SMTP, GMAIL_PORT)
    server.starttls()
    print("✓ Conexión segura establecida (TLS)")
    
    print("📧 Autenticándose...")
    server.login(GMAIL_USER, GMAIL_PASSWORD)
    print("✓ Autenticación exitosa")
    
    print("📧 Enviando email...")
    server.sendmail(GMAIL_USER, TARGET_EMAIL, msg.as_string())
    print("✓ Email enviado exitosamente")
    
    server.quit()
    
    print("\n" + "=" * 70)
    print("✓ PROCESO COMPLETADO EXITOSAMENTE")
    print("=" * 70)
    print(f"✓ Usuario: {user_name}")
    print(f"✓ Email enviado a: {TARGET_EMAIL}")
    print(f"✓ Tema: '{SUBJECT}'")
    print(f"✓ Plantilla: 'El hambre se hace grande como las ganas de estudiar'")
    
except Exception as e:
    print(f"\n✗ Error al enviar email: {e}")
    exit(1)
