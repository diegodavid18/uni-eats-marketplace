#!/usr/bin/env python3
import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
import sys

# Gmail SMTP configuration
GMAIL_USER = "dvdavid2509vargs@gmail.com"
GMAIL_PASSWORD = "pcqx nzex uhut mdvw"
GMAIL_SMTP = "smtp.gmail.com"
GMAIL_PORT = 587

# Email content
RECIPIENT = "dvdavid2509vargs@gmail.com"
SUBJECT = "🍔 Prueba UniEats - El hambre y la dedicación"
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

try:
    print("=" * 70)
    print("ENVÍO DIRECTO DE EMAIL - UNIEATS CRM")
    print("=" * 70)
    print(f"De: {GMAIL_USER}")
    print(f"Para: {RECIPIENT}")
    print(f"Asunto: {SUBJECT}")
    print("=" * 70)
    
    # Create the email message
    msg = MIMEMultipart('alternative')
    msg['Subject'] = SUBJECT
    msg['From'] = GMAIL_USER
    msg['To'] = RECIPIENT
    
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
    server.sendmail(GMAIL_USER, RECIPIENT, msg.as_string())
    print("✓ Email enviado exitosamente")
    
    server.quit()
    
    print("\n" + "=" * 70)
    print("✓ PROCESO COMPLETADO EXITOSAMENTE")
    print("=" * 70)
    print(f"✓ Email enviado a: {RECIPIENT}")
    print(f"✓ Tema: '{SUBJECT}'")
    print("✓ Plantilla: 'El hambre se hace grande como las ganas de estudiar'")
    sys.exit(0)
    
except Exception as e:
    print(f"\n✗ Error al enviar email: {e}")
    sys.exit(1)
