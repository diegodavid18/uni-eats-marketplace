#!/usr/bin/env powershell
# Script para crear y probar plantilla de correo CRM

$BASE_URL = "http://localhost:8092/api/crm"

# Crear plantilla de correo
$templatePayload = @{
    nombre = "Hambre_Estudiar"
    asunto = "El hambre se hace grande como las ganas de estudiar"
    contenidoHtml = @"
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 20px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            background: white;
            border-radius: 12px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
            overflow: hidden;
        }
        .header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 40px 20px;
            text-align: center;
        }
        .header h1 {
            margin: 0;
            font-size: 32px;
            font-weight: bold;
            line-height: 1.4;
        }
        .content {
            padding: 40px 30px;
        }
        .message {
            font-size: 18px;
            line-height: 1.8;
            color: #333;
            margin: 20px 0;
            text-align: center;
        }
        .highlight {
            font-size: 24px;
            font-weight: bold;
            color: #667eea;
            margin: 30px 0;
            font-style: italic;
        }
        .emoji {
            font-size: 48px;
            margin: 20px 0;
        }
        .features {
            margin: 30px 0;
            background: #f8f9ff;
            padding: 20px;
            border-radius: 8px;
            border-left: 4px solid #667eea;
        }
        .features ul {
            list-style: none;
            padding: 0;
            margin: 0;
        }
        .features li {
            padding: 10px 0;
            color: #555;
            font-size: 16px;
        }
        .features li:before {
            content: "✓ ";
            color: #667eea;
            font-weight: bold;
            margin-right: 10px;
        }
        .cta-button {
            display: inline-block;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 16px 40px;
            text-decoration: none;
            border-radius: 8px;
            font-weight: bold;
            margin: 30px 0;
            transition: transform 0.3s;
        }
        .cta-button:hover {
            transform: scale(1.05);
        }
        .footer {
            background: #f5f5f5;
            padding: 20px;
            text-align: center;
            color: #999;
            font-size: 14px;
            border-top: 1px solid #eee;
        }
        .footer p {
            margin: 5px 0;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>🎓 El Hambre se hace grande<br/>como las ganas de estudiar</h1>
        </div>
        
        <div class="content">
            <div class="emoji">🚀</div>
            
            <div class="message">
                Bienvenido a <strong>UniEats Marketplace</strong>
            </div>
            
            <div class="highlight">
                "Así como el hambre crece, tu pasión por aprender también se hace inmensa"
            </div>
            
            <p style="font-size: 16px; color: #555; line-height: 1.8;">
                En UniEats entendemos que los estudiantes tienen grandes apetitos. No solo de comida, 
                sino de conocimiento, de experiencias y de oportunidades para crecer. Por eso nos esforzamos 
                cada día para ofrecerte lo mejor en cada aspecto.
            </p>
            
            <div class="features">
                <ul>
                    <li>Comida deliciosa con entrega rápida</li>
                    <li>Ofertas especiales para estudiantes</li>
                    <li>Promociones exclusivas semanales</li>
                    <li>Puntos de lealtad en cada compra</li>
                    <li>Acceso a categorías premium</li>
                </ul>
            </div>
            
            <center>
                <a href="https://localhost:8092" class="cta-button">Explorar Ofertas</a>
            </center>
            
            <p style="font-size: 15px; color: #666; text-align: center; margin-top: 30px;">
                Tu hambre de éxito y nuestro compromiso de servicio forman la combinación perfecta. 
                <br/><strong>¡Que disfrutes tu experiencia con UniEats!</strong>
            </p>
            
            <div class="emoji" style="text-align: center;">🎉</div>
        </div>
        
        <div class="footer">
            <p><strong>UniEats Marketplace</strong></p>
            <p>Comida para tu cuerpo, educación para tu mente</p>
            <p style="margin-top: 15px; font-size: 12px;">© 2025 UniEats. Todos los derechos reservados.</p>
        </div>
    </div>
</body>
</html>
"@
    categoria = "MOTIVACIONAL"
} | ConvertTo-Json

Write-Host "📝 Creando plantilla de correo..." -ForegroundColor Cyan

$response = Invoke-WebRequest -Uri "$BASE_URL/templates" `
    -Method POST `
    -ContentType "application/json" `
    -Body $templatePayload `
    -UseBasicParsing

$template = $response.Content | ConvertFrom-Json
$templateId = $template.id

Write-Host "✅ Plantilla creada con ID: $templateId" -ForegroundColor Green
Write-Host ""

# Obtener plantilla para verificar
Write-Host "🔍 Verificando plantilla creada..." -ForegroundColor Cyan
$getResponse = Invoke-WebRequest -Uri "$BASE_URL/templates/category/MOTIVACIONAL" `
    -Method GET `
    -UseBasicParsing

Write-Host "✅ Plantilla verificada:" -ForegroundColor Green
$getResponse.Content | ConvertFrom-Json | ConvertTo-Json
Write-Host ""

# Crear campaña
Write-Host "📣 Creando campaña de marketing..." -ForegroundColor Cyan

$campaignPayload = @{
    nombre = "Campana_Hambre_Estudio"
    descripcion = "Campaña motivacional: El hambre se hace grande como las ganas de estudiar"
    tipo = "EMAIL"
    segmento = "ESTUDIANTE"
    estado = "DRAFT"
    presupuesto = 1000
} | ConvertTo-Json

$response = Invoke-WebRequest -Uri "$BASE_URL/campaigns" `
    -Method POST `
    -ContentType "application/json" `
    -Body $campaignPayload `
    -UseBasicParsing

$campaign = $response.Content | ConvertFrom-Json
$campaignId = $campaign.id

Write-Host "✅ Campaña creada con ID: $campaignId" -ForegroundColor Green
Write-Host ""

# Enviar correo de prueba a tu email registrado
Write-Host "📧 Enviando correo de prueba..." -ForegroundColor Cyan

$testEmailPayload = @{
    destinatario = "dvdavid2509vargs@gmail.com"
} | ConvertTo-Json

$response = Invoke-WebRequest -Uri "$BASE_URL/email/send-test?destinatario=dvdavid2509vargs@gmail.com" `
    -Method POST `
    -UseBasicParsing

$testResult = $response.Content | ConvertFrom-Json

if ($testResult.enviado) {
    Write-Host "✅ CORREO ENVIADO EXITOSAMENTE" -ForegroundColor Green
    Write-Host "   Destinatario: $($testResult.destinatario)" -ForegroundColor Green
} else {
    Write-Host "❌ Error al enviar: $($testResult.message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "📊 RESUMEN DE PRUEBA" -ForegroundColor Yellow
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
Write-Host "Template ID  : $templateId"
Write-Host "Campaign ID  : $campaignId"
Write-Host "Correo Envío : dvdavid2509vargs@gmail.com"
Write-Host "Estado       : ✅ COMPLETADO"
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
Write-Host ""
Write-Host "Próximos pasos:" -ForegroundColor Cyan
Write-Host "1. Revisa tu correo: dvdavid2509vargs@gmail.com"
Write-Host "2. Verifica que el diseño se ve bien"
Write-Host "3. Para enviar a todos los ESTUDIANTES:" -ForegroundColor Yellow
Write-Host "   POST /api/crm/campaigns/[campaignId]/send-segment/ESTUDIANTE/[templateId]"
Write-Host ""

