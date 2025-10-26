# Plan de Implementación: Marketing CRM

## 📋 Resumen Ejecutivo

El proyecto está **100% funcional** con:
- ✅ PostgreSQL local corriendo
- ✅ Todas las 12 tablas base creadas
- ✅ Spring Boot iniciando sin errores
- ✅ Código limpio sin archivos muertos

Ahora podemos proceder a la **Fase 2: Marketing basado en CRM**

---

## 🎯 Objetivos de Marketing CRM

### Segmentación de Clientes
- Agrupar estudiantes por comportamiento de compra
- Identificar clientes VIP, frecuentes, dormidos
- Analizar patrones de consumo por categoría

### Campañas Personalizadas
- Enviar promociones según segmento
- Ofertas automáticas en base a historial
- Email marketing inteligente

### Retención y Loyalty
- Programa de puntos de compra
- Referidos con incentivos
- Descuentos progresivos

---

## 🗄️ Diseño de Base de Datos CRM (14 nuevas tablas)

### CAPA 1: PERFILES Y SEGMENTACIÓN (3 tablas)

#### 1. customer_profiles
Almacena información consolidada del cliente
```sql
CREATE TABLE customer_profiles (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT UNIQUE NOT NULL,
    segment VARCHAR(50), -- 'VIP', 'FREQUENT', 'OCCASIONAL', 'DORMANT', 'NEW'
    total_purchases INT DEFAULT 0,
    total_spent DECIMAL(10,2) DEFAULT 0,
    average_order_value DECIMAL(10,2),
    last_purchase_date TIMESTAMP,
    lifetime_value DECIMAL(15,2),
    purchase_frequency INT, -- días promedio entre compras
    preferred_categories VARCHAR(500), -- JSON o comma-separated
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);
```

#### 2. customer_segments
Define segmentos predefinidos
```sql
CREATE TABLE customer_segments (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL,
    descripcion TEXT,
    criterios_json JSONB, -- Reglas de segmentación
    color_tag VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### 3. behavior_logs
Registro de cada interacción del usuario
```sql
CREATE TABLE behavior_logs (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    tipo_evento VARCHAR(50), -- 'VIEW', 'ADD_TO_CART', 'PURCHASE', 'REVIEW', 'SEARCH'
    entidad_tipo VARCHAR(50), -- 'PRODUCTO', 'TIENDA', 'CATEGORIA'
    entidad_id BIGINT,
    detalles_json JSONB,
    ip_address VARCHAR(45),
    user_agent VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);
CREATE INDEX idx_behavior_usuario_fecha ON behavior_logs(usuario_id, created_at DESC);
```

---

### CAPA 2: CAMPAÑAS Y COMUNICACIÓN (4 tablas)

#### 4. marketing_campaigns
Define campañas de marketing
```sql
CREATE TABLE marketing_campaigns (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
    tipo VARCHAR(50), -- 'EMAIL', 'PUSH', 'SMS', 'IN_APP'
    estado VARCHAR(50), -- 'DRAFT', 'SCHEDULED', 'ACTIVE', 'PAUSED', 'COMPLETED'
    segmento_id BIGINT,
    fecha_inicio TIMESTAMP,
    fecha_fin TIMESTAMP,
    presupuesto DECIMAL(10,2),
    presupuesto_gastado DECIMAL(10,2) DEFAULT 0,
    objetivo_conversión DECIMAL(5,2), -- porcentaje
    tasa_conversión_actual DECIMAL(5,2) DEFAULT 0,
    created_by BIGINT, -- usuario que crea la campaña
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (segmento_id) REFERENCES customer_segments(id),
    FOREIGN KEY (created_by) REFERENCES usuarios(id)
);
```

#### 5. email_templates
Plantillas de email reutilizables
```sql
CREATE TABLE email_templates (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL,
    descripcion TEXT,
    asunto_template VARCHAR(200),
    contenido_html TEXT NOT NULL,
    variables_disponibles JSONB, -- {usuario, producto, descuento, etc}
    categoria VARCHAR(50), -- 'WELCOME', 'RECOVERY', 'PROMOTION', 'CART_ABANDONMENT'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### 6. campaign_sends
Registro de cada email enviado
```sql
CREATE TABLE campaign_sends (
    id BIGSERIAL PRIMARY KEY,
    campaign_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    email_template_id BIGINT,
    asunto VARCHAR(200),
    contenido_enviado TEXT,
    estado VARCHAR(50), -- 'PENDING', 'SENT', 'BOUNCED', 'OPENED', 'CLICKED'
    email_address VARCHAR(255),
    fecha_envio TIMESTAMP,
    fecha_apertura TIMESTAMP,
    fecha_clic TIMESTAMP,
    clic_link_ids JSONB, -- qué links fueron clickeados
    reason_failed VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (campaign_id) REFERENCES marketing_campaigns(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (email_template_id) REFERENCES email_templates(id)
);
CREATE INDEX idx_campaign_sends_estado ON campaign_sends(estado);
CREATE INDEX idx_campaign_sends_usuario ON campaign_sends(usuario_id);
```

#### 7. campaign_links
Tracking de links dentro de campañas
```sql
CREATE TABLE campaign_links (
    id BIGSERIAL PRIMARY KEY,
    campaign_id BIGINT NOT NULL,
    enlace_original VARCHAR(500) NOT NULL,
    enlace_trackeable VARCHAR(500) UNIQUE,
    descripcion VARCHAR(200),
    clics_total INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (campaign_id) REFERENCES marketing_campaigns(id) ON DELETE CASCADE
);
```

---

### CAPA 3: OFERTAS Y PROMOCIONES (3 tablas)

#### 8. coupons
Códigos de descuento
```sql
CREATE TABLE coupons (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    descripcion TEXT,
    tipo_descuento VARCHAR(20), -- 'PERCENTAGE', 'FIXED_AMOUNT', 'FREE_SHIPPING'
    valor_descuento DECIMAL(10,2) NOT NULL,
    cantidad_maxima INT,
    cantidad_usada INT DEFAULT 0,
    minimo_compra DECIMAL(10,2) DEFAULT 0,
    fecha_inicio TIMESTAMP,
    fecha_expiracion TIMESTAMP,
    tiendas_aplicables JSONB, -- si es null, aplica a todas
    usuarios_aplicables JSONB, -- si es null, aplica a todos
    activo BOOLEAN DEFAULT true,
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES usuarios(id)
);
```

#### 9. discount_rules
Reglas automáticas de descuento
```sql
CREATE TABLE discount_rules (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    tipo_regla VARCHAR(50), -- 'VOLUME', 'FREQUENCY', 'CATEGORY', 'SEASONAL'
    condiciones_json JSONB, -- reglas en JSON
    descuento_tipo VARCHAR(20), -- 'PERCENTAGE', 'FIXED_AMOUNT'
    descuento_valor DECIMAL(10,2),
    activa BOOLEAN DEFAULT true,
    prioridad INT DEFAULT 100,
    fecha_inicio TIMESTAMP,
    fecha_fin TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### 10. user_coupon_usage
Historial de uso de cupones por usuario
```sql
CREATE TABLE user_coupon_usage (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    coupon_id BIGINT NOT NULL,
    pedido_id BIGINT,
    monto_descuento DECIMAL(10,2),
    usado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (coupon_id) REFERENCES coupons(id),
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id)
);
```

---

### CAPA 4: LOYALTY Y AUTOMACIÓN (3 tablas)

#### 11. loyalty_points
Sistema de puntos
```sql
CREATE TABLE loyalty_points (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT UNIQUE NOT NULL,
    puntos_totales INT DEFAULT 0,
    puntos_disponibles INT DEFAULT 0,
    puntos_canjeados INT DEFAULT 0,
    nivel_tier VARCHAR(50), -- 'BRONZE', 'SILVER', 'GOLD', 'PLATINUM'
    fecha_proximo_nivel TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);
```

#### 12. loyalty_transactions
Historial de movimientos de puntos
```sql
CREATE TABLE loyalty_transactions (
    id BIGSERIAL PRIMARY KEY,
    loyalty_points_id BIGINT NOT NULL,
    tipo_transaccion VARCHAR(50), -- 'EARNED', 'REDEEMED', 'EXPIRED', 'ADJUSTED'
    cantidad INT NOT NULL,
    referencia_tipo VARCHAR(50), -- 'PURCHASE', 'REFERRAL', 'REVIEW', 'COUPON'
    referencia_id BIGINT,
    descripcion VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (loyalty_points_id) REFERENCES loyalty_points(id) ON DELETE CASCADE
);
```

#### 13. marketing_automations
Automaciones de marketing
```sql
CREATE TABLE marketing_automations (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    tipo_trigger VARCHAR(50), -- 'PURCHASE', 'SIGNUP', 'CART_ABANDONMENT', 'INACTIVITY'
    condiciones_json JSONB,
    acciones_json JSONB, -- acciones a ejecutar
    estado VARCHAR(50), -- 'ACTIVE', 'PAUSED', 'COMPLETED'
    activa BOOLEAN DEFAULT true,
    fecha_inicio TIMESTAMP,
    fecha_fin TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

### CAPA 5: ENGAGEMENT (1 tabla)

#### 14. user_engagement_tracking
Tracking general de engagement
```sql
CREATE TABLE user_engagement_tracking (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    ultima_visita TIMESTAMP,
    dias_desde_compra INT,
    email_abiertos_mes INT DEFAULT 0,
    email_clics_mes INT DEFAULT 0,
    carrito_abandonado_veces INT DEFAULT 0,
    reviews_escritos INT DEFAULT 0,
    referidos_exitosos INT DEFAULT 0,
    engagement_score DECIMAL(5,2) DEFAULT 0, -- 0-100
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);
CREATE INDEX idx_engagement_score ON user_engagement_tracking(engagement_score DESC);
```

---

## 📊 Esquema de Relaciones

```
usuarios (existente)
    ↓
    ├→ customer_profiles (NEW) - 1:1
    ├→ behavior_logs (NEW) - 1:N
    ├→ marketing_campaigns (NEW) - 1:N (created_by)
    └→ loyalty_points (NEW) - 1:1
        └→ loyalty_transactions (NEW) - 1:N

marketing_campaigns (NEW) 
    ├→ customer_segments (NEW) - N:1
    ├→ campaign_sends (NEW) - 1:N
    ├→ campaign_links (NEW) - 1:N
    └→ email_templates (NEW) - N:1

coupons (NEW)
    └→ user_coupon_usage (NEW) - 1:N

discount_rules (NEW) - standalone

user_engagement_tracking (NEW)
    └→ usuarios - N:1
```

---

## 🔧 Orden de Implementación Recomendado

### Fase 1: Entidades Base (día 1)
1. Crear todas las 14 entidades JPA
2. Crear todos los repositorios Spring Data JPA
3. Ejecutar DDL (crear tablas)

### Fase 2: Servicios (día 2)
1. Services de customer_profiles
2. Services de behavior tracking
3. Services de campaigns
4. Services de coupons y loyalty

### Fase 3: API REST (día 3)
1. Controllers para admin (CRUD de campañas, templates)
2. Controllers para usuarios (historial, puntos, cupones)
3. Endpoints para tracking

### Fase 4: UI (día 4-5)
1. Panel de admin para crear campañas
2. Dashboard de análisis
3. Página de puntos y cupones para usuarios

---

## 📈 Métricas a Rastrear

### Para Campañas
- Tasa de apertura (opened / sent)
- Tasa de clic (clicked / sent)
- Tasa de conversión (purchases / clicked)
- ROI (revenue - cost / cost)

### Para Usuarios
- Customer Lifetime Value (CLV)
- Engagement Score (0-100)
- Churn Risk (probabilidad de abandono)

### Para CRM
- Segmentación efectividad
- Automatización success rate
- Loyalty program adoption

---

## ✅ Checklist de Implementación

- [ ] Crear migraciones DDL SQL
- [ ] Entidades JPA (14 clases)
- [ ] Repositorios (14 interfaces)
- [ ] Services (8 clases aprox)
- [ ] Controllers API
- [ ] Validaciones de negocio
- [ ] Tests unitarios
- [ ] Tests de integración
- [ ] UI componentes
- [ ] Documentación API

---

## 🎓 Notas Importantes

1. **JSONB en PostgreSQL:** Usar para campos flexibles como configuraciones
2. **Índices:** Críticos en `behavior_logs`, `loyalty_transactions`, `campaign_sends`
3. **Triggers SQL:** Considerar para actualizar `customer_profiles` automáticamente
4. **Particionamiento:** Si crece mucho, particionar `behavior_logs` por fecha
5. **Auditoría:** Considerar tabla de auditoría para cambios de campaña

---

**Siguiente paso:** Confirmar este plan y comenzar a implementar las entidades JPA ✅
