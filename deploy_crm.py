#!/usr/bin/env python3
import psycopg2
import requests
import json
import time
import subprocess
import os
import signal
import sys

# Configuración
DB_HOST = "localhost"
DB_PORT = 5432
DB_NAME = "unieats_marketplace"
DB_USER = "postgres"
DB_PASSWORD = "2010"
API_URL = "http://localhost:8092/api/crm"
JAR_PATH = r"C:\Users\jero\Desktop\marketplace\target\marketplace-0.0.1-SNAPSHOT.jar"

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
        INSERT INTO email_template (nombre, asunto, contenido_html, categoria, estado, fecha_creacion)
        VALUES (%s, %s, %s, %s, %s, NOW())
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
            'marketing',
            'ACTIVO'
        ))
        
        conn.commit()
        cursor.close()
        conn.close()
        print("✓ Plantilla insertada en la BD")
        return True
    except Exception as e:
        print(f"✗ Error insertando plantilla: {e}")
        return False

def enviar_email_prueba():
    """Enviar email de prueba"""
    try:
        data = {
            "destinatario": "dvdavid2509vargs@gmail.com",
            "asunto": "Prueba UniEats - El hambre y la dedicación",
            "contenido": "Este es un email de prueba del sistema CRM de UniEats"
        }
        
        response = requests.post(
            f"{API_URL}/email/send-test",
            json=data,
            timeout=10
        )
        
        if response.status_code == 200:
            print("✓ Email de prueba enviado exitosamente")
            return True
        else:
            print(f"✗ Error enviando email: {response.status_code} - {response.text}")
            return False
    except Exception as e:
        print(f"✗ Error enviando email: {e}")
        return False

def iniciar_app():
    """Iniciar la aplicación Spring Boot"""
    print("Iniciando aplicación...")
    try:
        proc = subprocess.Popen([
            "java", "-jar", JAR_PATH
        ], stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
        return proc
    except Exception as e:
        print(f"Error iniciando app: {e}")
        return None

if __name__ == "__main__":
    # Insertar plantilla
    print("=" * 60)
    print("CREANDO PLANTILLA DE EMAIL")
    print("=" * 60)
    if insertar_plantilla():
        print("\n✓ Plantilla creada exitosamente")
    else:
        print("\n✗ Falló la creación de la plantilla")
        sys.exit(1)
    
    # Iniciar app
    print("\n" + "=" * 60)
    print("INICIANDO APLICACIÓN")
    print("=" * 60)
    app = iniciar_app()
    if app:
        time.sleep(10)  # Esperar a que se inicie
        
        # Enviar email de prueba
        print("\n" + "=" * 60)
        print("ENVIANDO EMAIL DE PRUEBA")
        print("=" * 60)
        enviar_email_prueba()
        
        # Mantener app activa
        print("\n✓ Aplicación ejecutándose. Presiona Ctrl+C para salir...")
        try:
            app.wait()
        except KeyboardInterrupt:
            app.terminate()
            print("\n✓ Aplicación detenida")
