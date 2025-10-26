-- Script para insertar plantilla de email
BEGIN TRANSACTION;

INSERT INTO email_template (nombre, asunto, contenido_html, categoria, estado, fecha_creacion)
VALUES (
    'Incentivo Estudial - Hambre y Dedicación',
    'El hambre se hace grande como las ganas de estudiar',
    '<html><body style="font-family: Arial, sans-serif;"><div style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 40px; text-align: center; border-radius: 10px;"><h1 style="margin: 0; font-size: 28px;">¡El hambre se hace grande!</h1><h2 style="margin: 10px 0 20px 0; font-size: 22px;">Como las ganas de estudiar</h2><p style="margin: 15px 0;">Bienvenido a <strong>UniEats</strong>, tu marketplace de comida universitario</p><p style="margin: 15px 0;">Encontrarás comida deliciosa a precios diseñados para estudiantes como tú.</p><div style="background: white; color: #667eea; padding: 15px; margin-top: 20px; border-radius: 5px;"><p><strong>Con UniEats:</strong></p><ul style="text-align: left;"><li>Comida rápida y de calidad</li><li>Precios universitarios</li><li>Entregas rápidas</li><li>Variedad de opciones</li></ul></div><p style="margin-top: 20px; font-size: 12px; opacity: 0.9;">¡Descarga la app y empieza a disfrutar!</p></div></body></html>',
    'marketing',
    'ACTIVO',
    NOW()
);

COMMIT;
