-- =====================================================
-- MIGRACIÓN: Notificaciones Temporales con Expiración
-- =====================================================
-- Este script agrega los campos necesarios para implementar
-- el sistema de notificaciones temporales con expiración automática
--
-- Autor: Sistema Gestor SAT
-- Fecha: 2025-10-16
-- =====================================================

-- 1. Agregar columna fecha_expiracion
ALTER TABLE notificacion
ADD COLUMN IF NOT EXISTS fecha_expiracion TIMESTAMP NOT NULL DEFAULT (CURRENT_TIMESTAMP + INTERVAL '30 days');

-- 2. Agregar columna expirada
ALTER TABLE notificacion
ADD COLUMN IF NOT EXISTS expirada BOOLEAN NOT NULL DEFAULT FALSE;

-- 3. Crear índices para mejorar el rendimiento de las consultas
CREATE INDEX IF NOT EXISTS idx_notificacion_expirada ON notificacion(expirada);
CREATE INDEX IF NOT EXISTS idx_notificacion_fecha_expiracion ON notificacion(fecha_expiracion);
CREATE INDEX IF NOT EXISTS idx_notificacion_usuario_expirada ON notificacion(id_usuario, expirada);
CREATE INDEX IF NOT EXISTS idx_notificacion_usuario_leida_expirada ON notificacion(id_usuario, leida, expirada);

-- 4. Actualizar notificaciones existentes con fecha de expiración (30 días desde su creación)
UPDATE notificacion
SET fecha_expiracion = fecha + INTERVAL '30 days'
WHERE fecha_expiracion IS NULL OR fecha_expiracion = fecha;

-- 5. Marcar como expiradas las notificaciones antiguas (más de 30 días)
UPDATE notificacion
SET expirada = TRUE
WHERE fecha_expiracion < CURRENT_TIMESTAMP AND expirada = FALSE;

-- 6. (OPCIONAL) Si deseas eliminar notificaciones muy antiguas (más de 37 días = 30 + 7 de retención)
-- Descomenta las siguientes líneas si deseas ejecutar esta limpieza inicial
-- DELETE FROM notificacion
-- WHERE expirada = TRUE
-- AND fecha_expiracion < (CURRENT_TIMESTAMP - INTERVAL '7 days');

-- =====================================================
-- Verificación de la migración
-- =====================================================

-- Verificar estructura de la tabla
SELECT column_name, data_type, is_nullable, column_default
FROM information_schema.columns
WHERE table_name = 'notificacion'
ORDER BY ordinal_position;

-- Contar notificaciones por estado
SELECT
    COUNT(*) AS total,
    COUNT(CASE WHEN expirada = FALSE THEN 1 END) AS activas,
    COUNT(CASE WHEN expirada = TRUE THEN 1 END) AS expiradas,
    COUNT(CASE WHEN fecha_expiracion < CURRENT_TIMESTAMP AND expirada = FALSE THEN 1 END) AS por_marcar_expiradas
FROM notificacion;

-- Ver índices creados
SELECT
    indexname,
    indexdef
FROM pg_indexes
WHERE tablename = 'notificacion'
ORDER BY indexname;

-- =====================================================
-- Consultas útiles para monitoreo
-- =====================================================

-- Notificaciones próximas a expirar (dentro de 7 días)
SELECT
    id_notificacion,
    id_usuario,
    tipo,
    fecha,
    fecha_expiracion,
    EXTRACT(DAY FROM (fecha_expiracion - CURRENT_TIMESTAMP)) AS dias_restantes
FROM notificacion
WHERE expirada = FALSE
AND fecha_expiracion BETWEEN CURRENT_TIMESTAMP AND (CURRENT_TIMESTAMP + INTERVAL '7 days')
ORDER BY fecha_expiracion ASC;

-- Estadísticas generales de notificaciones
SELECT
    tipo,
    COUNT(*) AS total,
    COUNT(CASE WHEN expirada = FALSE THEN 1 END) AS activas,
    COUNT(CASE WHEN expirada = TRUE THEN 1 END) AS expiradas,
    COUNT(CASE WHEN leida = TRUE THEN 1 END) AS leidas,
    COUNT(CASE WHEN leida = FALSE AND expirada = FALSE THEN 1 END) AS no_leidas_activas
FROM notificacion
GROUP BY tipo
ORDER BY tipo;

-- =====================================================
-- ÉXITO: La migración se completó correctamente
-- =====================================================
