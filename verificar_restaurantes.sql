-- Script para verificar y agregar restaurantes de prueba
USE SistemaPromociones;

-- Ver restaurantes existentes
SELECT 
    r.Codigo,
    r.Nombre,
    r.RazonSocial,
    r.RUC,
    r.CorreoElectronico,
    r.CodigoEstadoAprobacion,
    ea.Nombre as EstadoAprobacion,
    r.FechaCreacion
FROM Restaurante r
LEFT JOIN estado_aprobacion ea ON r.CodigoEstadoAprobacion = ea.Codigo
ORDER BY r.FechaCreacion DESC;

-- Si no hay restaurantes, puedes ejecutar esto para crear uno de prueba:
/*
-- Primero crear un usuario para el restaurante (si no existe)
INSERT INTO Usuario (TipoUsuario, NumeroDocumento, Nombres, ApellidoPaterno, ApellidoMaterno, 
                     CorreoElectronico, Contrasena, CodigoDistrito, Estado)
VALUES ('RUC', '20131312955', 'Restaurante', 'Prueba', 'Test', 
        'restaurante@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 
        1, 1);

-- Obtener el ID del usuario creado
SET @usuario_id = LAST_INSERT_ID();

-- Crear el restaurante
INSERT INTO Restaurante (CodigoUsuario, RUC, RazonSocial, Nombre, Direccion, 
                        Telefono, CorreoElectronico, CodigoDistrito, 
                        CodigoEstadoAprobacion, Estado, FechaCreacion)
VALUES (@usuario_id, '20131312955', 'SUNAT - SUPERINTENDENCIA NACIONAL DE ADUANAS Y DE ADMINISTRACION TRIBUTARIA', 
        'SUNAT', 'AV. GARCILASO DE LA VEGA NRO. 1472', 
        '987654321', 'restaurante@test.com', 1, 
        7, 1, NOW());

SELECT 'Restaurante de prueba creado exitosamente' as Resultado;
*/
