#!/usr/bin/env python3
import psycopg2
import requests
import time
from datetime import datetime

# Configuración
DB_HOST = "localhost"
DB_PORT = 5432
DB_NAME = "unieats_marketplace"
DB_USER = "postgres"
DB_PASSWORD = "2010"
API_URL = "http://localhost:8092/api/crm"

def insertar_plantilla():
    """Insertar plantilla directamente en la BD"""
    try:
        conn = psycopg2.connect(
            host=DB_HOST,
            port=DB_PORT,
            database=DB_NAME,
            user=DB_USER,
            password=DB_PASSWORD
        )
        cursor = conn.cursor()
        
        sql = """
        INSERT INTO email_templates (nombre, asunto_template, contenido_html, categoria, created_at, updated_at)
        VALUES (%s, %s, %s, %s, NOW(), NOW())
        RETURNING id
        """
        
        html_content = '''<html><body style="font-family: Arial, sans-serif;">
        <div style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 40px; text-align: center; border-radius: 10px;">
            <h1 style="margin: 0; font-size: 28px;">¡El hambre se hace grande!</h1>
            <h2 style="margin: 10px 0 20px 0; font-size: 22px;">Como las ganas de estudiar</h2>
            <p style="margin: 15px 0;">Bienvenido a <strong>UniEats</strong>, tu marketplace de comida universitario</p>
            <p style="margin: 15px 0;">Encontrarás comida deliciosa a precios diseñados para estudiantes como tú.</p>
            <div style="background: white; color: #667eea; padding: 15px; margin-top: 20px; border-radius: 5px;">
                <p><strong>Con UniEats:</strong></p>
                <ul style="text-align: left;">
                    <li>Comida rápida y de calidad</li>
                    <li>Precios universitarios</li>
                    <li>Entregas rápidas</li>
                    <li>Variedad de opciones</li>
                </ul>
            </div>
            <p style="margin-top: 20px; font-size: 12px; opacity: 0.9;">¡Descarga la app y empieza a disfrutar!</p>
        </div></body></html>'''
        
        cursor.execute(sql, (
            'Incentivo Estudial - Hambre y Dedicación',
            'El hambre se hace grande como las ganas de estudiar',
            html_content,
            'marketing'
        ))
        
        template_id = cursor.fetchone()[0]
        conn.commit()
        cursor.close()
        conn.close()
        print(f"✓ Plantilla insertada en la BD - ID: {template_id}")
        return template_id
    except Exception as e:
        print(f"✗ Error insertando plantilla: {e}")
        return None

def enviar_email_prueba():
    """Enviar email de prueba"""
    max_intentos = 10
    intento = 0
    
    while intento < max_intentos:
        try:
            print(f"\n📧 Intento {intento + 1} de {max_intentos} - Enviando email de prueba...")
            
            response = requests.post(
                f"{API_URL}/email/send-test?destinatario=dvdavid2509vargs@gmail.com",
                timeout=10
            )
            
            if response.status_code == 200:
                print("✓ Email enviado exitosamente")
                return True
            else:
                print(f"⚠️  Respuesta {response.status_code}")
                if intento < max_intentos - 1:
                    print(f"Esperando 5 segundos antes de reintentar...")
                    time.sleep(5)
                intento += 1
        except requests.exceptions.ConnectionError:
            print(f"⚠️  No se puede conectar al servidor (intento {intento + 1}/{max_intentos})")
            if intento < max_intentos - 1:
                time.sleep(5)
            intento += 1
        except Exception as e:
            print(f"✗ Error: {e}")
            if intento < max_intentos - 1:
                time.sleep(5)
            intento += 1
    
    return False

if __name__ == "__main__":
    print("=" * 70)
    print("SISTEMA DE PLANTILLAS DE EMAIL - UNIEATS CRM")
    print("=" * 70)
    print(f"Hora: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    
    # Insertar plantilla
    print("\n" + "=" * 70)
    print("PASO 1: INSERTANDO PLANTILLA DE EMAIL")
    print("=" * 70)
    template_id = insertar_plantilla()
    
    if not template_id:
        print("✗ No se pudo insertar la plantilla. Abortando.")
        exit(1)
    
    # Enviar email de prueba
    print("\n" + "=" * 70)
    print("PASO 2: ENVIANDO EMAIL DE PRUEBA")
    print("=" * 70)
    print("Esperando a que la aplicación esté lista...")
    time.sleep(3)
    
    if enviar_email_prueba():
        print("\n" + "=" * 70)
        print("✓ PROCESO COMPLETADO EXITOSAMENTE")
        print("=" * 70)
        print(f"- Plantilla insertada con ID: {template_id}")
        print("- Email de prueba enviado a: dvdavid2509vargs@gmail.com")
        print("- Tema: 'El hambre se hace grande como las ganas de estudiar'")
    else:
        print("\n✗ No se pudo enviar el email después de múltiples intentos")
        print("Pero la plantilla sí fue creada exitosamente")
