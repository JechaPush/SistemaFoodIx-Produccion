-- Script para insertar datos iniciales en la base de datos db_foodix
-- Ejecutar este script en MySQL Workbench

USE db_foodix;

-- ============================================
-- 1. DEPARTAMENTOS (Algunos principales del Perú)
-- ============================================
INSERT INTO departamento (nombre, estado) VALUES
('Lima', TRUE),
('Arequipa', TRUE),
('Cusco', TRUE),
('La Libertad', TRUE),
('Piura', TRUE);

-- ============================================
-- 2. PROVINCIAS (Algunas provincias)
-- ============================================
-- Lima
INSERT INTO provincia (nombre, codigo_departamento, estado) VALUES
('Lima', 1, TRUE),
('Huaura', 1, TRUE),
('Cañete', 1, TRUE);

-- Arequipa
INSERT INTO provincia (nombre, codigo_departamento, estado) VALUES
('Arequipa', 2, TRUE),
('Camaná', 2, TRUE);

-- Cusco
INSERT INTO provincia (nombre, codigo_departamento, estado) VALUES
('Cusco', 3, TRUE),
('Urubamba', 3, TRUE);

-- La Libertad
INSERT INTO provincia (nombre, codigo_departamento, estado) VALUES
('Trujillo', 4, TRUE),
('Ascope', 4, TRUE);

-- Piura
INSERT INTO provincia (nombre, codigo_departamento, estado) VALUES
('Piura', 5, TRUE),
('Sullana', 5, TRUE);

-- ============================================
-- 3. DISTRITOS (Algunos distritos por provincia)
-- ============================================
-- Provincia Lima (codigo 1)
INSERT INTO distrito (nombre, codigo_provincia, estado) VALUES
('Lima', 1, TRUE),
('Miraflores', 1, TRUE),
('San Isidro', 1, TRUE),
('Surco', 1, TRUE),
('La Molina', 1, TRUE),
('San Borja', 1, TRUE),
('Barranco', 1, TRUE),
('San Miguel', 1, TRUE),
('Pueblo Libre', 1, TRUE),
('Jesús María', 1, TRUE);

-- Provincia Arequipa (codigo 4)
INSERT INTO distrito (nombre, codigo_provincia, estado) VALUES
('Arequipa', 4, TRUE),
('Cayma', 4, TRUE),
('Cerro Colorado', 4, TRUE),
('Yanahuara', 4, TRUE);

-- Provincia Cusco (codigo 6)
INSERT INTO distrito (nombre, codigo_provincia, estado) VALUES
('Cusco', 6, TRUE),
('Wanchaq', 6, TRUE),
('Santiago', 6, TRUE);

-- Provincia Trujillo (codigo 8)
INSERT INTO distrito (nombre, codigo_provincia, estado) VALUES
('Trujillo', 8, TRUE),
('La Esperanza', 8, TRUE),
('Víctor Larco Herrera', 8, TRUE);

-- Provincia Piura (codigo 10)
INSERT INTO distrito (nombre, codigo_provincia, estado) VALUES
('Piura', 10, TRUE),
('Castilla', 10, TRUE);

-- ============================================
-- 4. CATEGORÍAS DE RESTAURANTES
-- ============================================
INSERT INTO categoria (nombre, descripcion, icono, estado) VALUES
('Comida Criolla', 'Platos tradicionales peruanos', 'fa-utensils', TRUE),
('Chifa', 'Comida fusión chino-peruana', 'fa-dragon', TRUE),
('Pizzería', 'Pizzas y comida italiana', 'fa-pizza-slice', TRUE),
('Hamburguesas', 'Hamburguesas y fast food', 'fa-hamburger', TRUE),
('Pollos y Parrillas', 'Pollo a la brasa y parrillas', 'fa-drumstick-bite', TRUE),
('Mariscos', 'Cevicherías y mariscos', 'fa-fish', TRUE),
('Postres', 'Heladerías y postres', 'fa-ice-cream', TRUE),
('Cafetería', 'Cafés y bebidas', 'fa-coffee', TRUE),
('Comida Vegetariana', 'Opciones vegetarianas y veganas', 'fa-leaf', TRUE),
('Sushi', 'Comida japonesa', 'fa-fish', TRUE);

-- ============================================
-- 5. TIPOS DE VEHÍCULO
-- ============================================
INSERT INTO tipo_vehiculo (nombre, estado) VALUES
('Bicicleta', TRUE),
('Motocicleta', TRUE),
('Scooter Eléctrico', TRUE),
('Automóvil', TRUE);

-- ============================================
-- 6. ESTADOS DE APROBACIÓN
-- ============================================
INSERT INTO estado_aprobacion (nombre, descripcion, estado) VALUES
('Pendiente', 'Solicitud en revisión', TRUE),
('Aprobado', 'Solicitud aprobada', TRUE),
('Rechazado', 'Solicitud rechazada', TRUE);

-- ============================================
-- VERIFICACIÓN
-- ============================================
SELECT 'Departamentos insertados:' AS msg, COUNT(*) AS cantidad FROM departamento;
SELECT 'Provincias insertadas:' AS msg, COUNT(*) AS cantidad FROM provincia;
SELECT 'Distritos insertados:' AS msg, COUNT(*) AS cantidad FROM distrito;
SELECT 'Categorías insertadas:' AS msg, COUNT(*) AS cantidad FROM categoria;
SELECT 'Tipos de vehículo insertados:' AS msg, COUNT(*) AS cantidad FROM tipo_vehiculo;
SELECT 'Estados de aprobación insertados:' AS msg, COUNT(*) AS cantidad FROM estado_aprobacion;

-- ============================================
-- CONSULTAS DE PRUEBA
-- ============================================
-- Ver departamentos con sus provincias
SELECT d.nombre AS Departamento, p.nombre AS Provincia
FROM departamento d
LEFT JOIN provincia p ON p.codigo_departamento = d.codigo
ORDER BY d.nombre, p.nombre;

-- Ver provincias con sus distritos
SELECT p.nombre AS Provincia, di.nombre AS Distrito
FROM provincia p
LEFT JOIN distrito di ON di.codigo_provincia = p.codigo
ORDER BY p.nombre, di.nombre;
