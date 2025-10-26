#!/usr/bin/env powershell
# Script para probar la integración de correos en el CRM

# ===== CONFIGURACIÓN =====
$BASE_URL = "http://localhost:8092/api/crm"
$TEST_EMAIL = "dvdavid2509vargs@gmail.com"  # Tu correo para pruebas

Write-Host "🚀 Iniciando pruebas del CRM Marketing..." -ForegroundColor Green
Write-Host ""

# ===== TEST 1: Enviar correo de prueba =====
Write-Host "📧 TEST 1: Enviando correo de prueba..." -ForegroundColor Cyan
$response = Invoke-WebRequest -Uri "$BASE_URL/email/send-test?destinatario=$TEST_EMAIL" `
    -Method POST `
    -ContentType "application/json" `
    -UseBasicParsing

Write-Host "Respuesta:" -ForegroundColor Yellow
$response.Content | ConvertFrom-Json | ConvertTo-Json -Depth 2
Write-Host ""

# ===== TEST 2: Crear plantilla de correo =====
Write-Host "📝 TEST 2: Creando plantilla de correo..." -ForegroundColor Cyan

$templatePayload = @{
    nombre = "Promocion_Descuento_50"
    asunto = "¡Descuento especial del 50% solo para ti!"
    contenidoHtml = @"
<html>
  <head>
    <style>
      body { font-family: Arial, sans-serif; background-color: #f4f4f4; }
      .container { max-width: 600px; margin: 20px auto; background: white; padding: 20px; border-radius: 8px; }
      .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 20px; border-radius: 8px; text-align: center; }
      .promo { font-size: 48px; font-weight: bold; color: #667eea; text-align: center; margin: 20px 0; }
      .cta-button { display: inline-block; background-color: #667eea; color: white; padding: 15px 30px; text-decoration: none; border-radius: 5px; font-weight: bold; }
    </style>
  </head>
  <body>
    <div class="container">
      <div class="header">
        <h1>🎉 UniEats Marketplace</h1>
      </div>
      <h2>¡Hola {{nombre}},</h2>
      <p>Tenemos una oferta especial solo para ti:</p>
      <div class="promo">50% DESCUENTO</div>
      <p>Válido hasta: {{fecha_expiracion}}</p>
      <p>Código: <strong>{{codigo}}</strong></p>
      <center>
        <a href="{{enlace_promocion}}" class="cta-button">Reclamar Descuento</a>
      </center>
      <p>Gracias por ser parte de UniEats!</p>
    </div>
  </body>
</html>
"@
    categoria = "PROMOCIONES"
} | ConvertTo-Json

$response = Invoke-WebRequest -Uri "$BASE_URL/templates" `
    -Method POST `
    -ContentType "application/json" `
    -Body $templatePayload `
    -UseBasicParsing

Write-Host "✅ Plantilla creada:" -ForegroundColor Green
$response.Content | ConvertFrom-Json | ConvertTo-Json -Depth 2
Write-Host ""

# ===== TEST 3: Crear campaña de marketing =====
Write-Host "📣 TEST 3: Creando campaña de marketing..." -ForegroundColor Cyan

$campaignPayload = @{
    nombre = "Promocion_Octubre_2025"
    descripcion = "Campaña especial de descuento para octubre"
    tipo = "EMAIL"
    segmento = "PREMIUM"
    estado = "DRAFT"
    presupuesto = 5000
} | ConvertTo-Json

$response = Invoke-WebRequest -Uri "$BASE_URL/campaigns" `
    -Method POST `
    -ContentType "application/json" `
    -Body $campaignPayload `
    -UseBasicParsing

Write-Host "✅ Campaña creada:" -ForegroundColor Green
$campaignResponse = $response.Content | ConvertFrom-Json
$campaignResponse | ConvertTo-Json -Depth 2
$campaignId = $campaignResponse.id
Write-Host ""

# ===== TEST 4: Obtener plantillas disponibles =====
Write-Host "📚 TEST 4: Listando plantillas disponibles..." -ForegroundColor Cyan

$response = Invoke-WebRequest -Uri "$BASE_URL/templates/category/PROMOCIONES" `
    -Method GET `
    -UseBasicParsing

Write-Host "Plantillas encontradas:" -ForegroundColor Yellow
$response.Content | ConvertFrom-Json | ConvertTo-Json -Depth 2
Write-Host ""

# ===== TEST 5: Obtener campañas por estado =====
Write-Host "🎯 TEST 5: Listando campañas por estado..." -ForegroundColor Cyan

$response = Invoke-WebRequest -Uri "$BASE_URL/campaigns/status/DRAFT" `
    -Method GET `
    -UseBasicParsing

Write-Host "Campañas en estado DRAFT:" -ForegroundColor Yellow
$response.Content | ConvertFrom-Json | ConvertTo-Json -Depth 2
Write-Host ""

# ===== TEST 6: Obtener cupones activos =====
Write-Host "🎫 TEST 6: Listando cupones activos..." -ForegroundColor Cyan

$response = Invoke-WebRequest -Uri "$BASE_URL/coupons/active" `
    -Method GET `
    -UseBasicParsing

Write-Host "Cupones activos:" -ForegroundColor Yellow
$response.Content | ConvertFrom-Json | ConvertTo-Json -Depth 2
Write-Host ""

Write-Host "✨ ¡Pruebas completadas!" -ForegroundColor Green

Write-Host ""
Write-Host "📌 PRÓXIMOS PASOS SUGERIDOS:" -ForegroundColor Magenta
Write-Host "1. Verificar que recibiste el correo de prueba en $TEST_EMAIL"
Write-Host "2. Crear más plantillas de correo según necesites"
Write-Host "3. Crear campañas y enviarlas a segmentos específicos"
Write-Host "4. Monitorear estadísticas de apertura y clics"
Write-Host ""

