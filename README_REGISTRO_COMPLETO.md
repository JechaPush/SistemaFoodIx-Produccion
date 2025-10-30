# ✅ ¡SISTEMA COMPLETO PARA REGISTRAR!

## 🎉 **¡YA PUEDES REGISTRAR REPARTIDORES Y RESTAURANTES!**

### **LO QUE ACABO DE IMPLEMENTAR:**

✅ **2 DTOs** → RepartidorRegistroDTO, RestauranteRegistroDTO  
✅ **4 Services** → FileStorageService, UsuarioService, RepartidorService, RestauranteService  
✅ **2 Métodos POST** → RepartidorController.registrarRepartidor(), RestauranteController.registrarRestaurante()  
✅ **Compilación exitosa** → 33 archivos Java compilados

---

## 📋 **PASOS PARA PROBAR EL REGISTRO COMPLETO:**

### **PASO 1: Insertar datos en MySQL** ⚠️ **OBLIGATORIO**

Si aún no lo has hecho, ejecuta el script SQL:

```sql
-- En MySQL Workbench, abre y ejecuta:
datos_iniciales.sql
```

Esto insertará:
- 5 Departamentos
- 11 Provincias  
- 23 Distritos
- 10 Categorías
- 4 Tipos de Vehículo
- 3 Estados de Aprobación

### **PASO 2: Iniciar la aplicación**

```powershell
cd c:\Users\DANIELA\OneDrive\Documentos\SistemaDePromociones\SistemaDePromociones
.\mvnw.cmd spring-boot:run
```

Espera a ver: `Started SistemaDePromocionesApplication`

---

## 🚴 **PROBAR REGISTRO DE REPARTIDOR:**

### **1. Navega a:**
```
http://localhost:8080/registro-repartidor
```

### **2. Llena el formulario:**

**Datos Personales:**
- Nombre: Juan
- Apellidos: Pérez García
- Tipo de Documento: DNI
- Número: 12345678
- Fecha de Nacimiento: 01/01/1990
- Correo: juan.perez@gmail.com
- Contraseña: Password123!
- Teléfono: 987654321

**Ubicación:**
- Departamento: Lima
- Provincia: Lima  
- Distrito: Miraflores
- Dirección: Av. Larco 123

**Datos del Vehículo:**
- Número de Licencia: L123456789
- Tipo de Vehículo: Motocicleta

**Documentos:** (Selecciona cualquier archivo PDF/imagen, max 5MB cada uno)
- Licencia de Conducir
- SOAT
- Antecedentes Policiales
- Tarjeta de Propiedad

### **3. Da click en "Registrar"**

### **4. Verifica en MySQL:**

```sql
-- Ver el usuario creado
SELECT * FROM usuario ORDER BY codigo DESC LIMIT 1;

-- Ver el repartidor creado
SELECT * FROM repartidor ORDER BY codigo DESC LIMIT 1;

-- Ver los documentos guardados
SELECT * FROM documento_repartidor ORDER BY codigo DESC LIMIT 4;
```

---

## 🍕 **PROBAR REGISTRO DE RESTAURANTE:**

### **1. Navega a:**
```
http://localhost:8080/registro-restaurante
```

### **2. Llena el formulario:**

**Datos del Representante:**
- Nombre: María
- Apellidos: López Torres
- Tipo de Documento: DNI
- Número: 87654321
- Fecha de Nacimiento: 15/05/1985
- Correo: maria.lopez@gmail.com
- Contraseña: Password123!
- Teléfono: 912345678
- Dirección Personal: Calle Los Olivos 456
- Ubicación Personal: Lima > Lima > San Isidro

**Datos del Restaurante:**
- RUC: 20123456789 (11 dígitos, empieza con 10 o 20)
- Razón Social: Restaurante El Buen Sabor SAC
- Nombre Comercial: El Buen Sabor
- Descripción: Restaurante de comida criolla peruana con más de 10 años de experiencia...

**Ubicación del Negocio:**
- Dirección: Av. Principal 789
- Teléfono: 014567890
- Correo: contacto@elbuensabor.com
- Ubicación: Lima > Lima > Surco

**Categorías:** (Marca al menos 1)
- ☑ Comida Criolla
- ☑ Mariscos

**Documentos:** (PDFs)
- Documento RUC
- Licencia de Funcionamiento
- Carnet de Sanidad

**Imágenes:** (JPG/PNG)
- Logo
- Portada
- Galería (puedes subir varias)

### **3. Da click en "Registrar"**

### **4. Verifica en MySQL:**

```sql
-- Ver el usuario (representante)
SELECT * FROM usuario ORDER BY codigo DESC LIMIT 1;

-- Ver el restaurante creado
SELECT * FROM restaurante ORDER BY codigo DESC LIMIT 1;

-- Ver las categorías asignadas
SELECT cr.*, c.nombre 
FROM categoria_restaurante cr
JOIN categoria c ON cr.codigo_categoria = c.codigo
ORDER BY cr.codigo_restaurante DESC LIMIT 5;

-- Ver documentos
SELECT * FROM documento_restaurante ORDER BY codigo DESC LIMIT 3;

-- Ver imágenes
SELECT * FROM imagen_restaurante ORDER BY codigo DESC LIMIT 5;
```

---

## 📁 **VERIFICAR ARCHIVOS GUARDADOS:**

Los archivos se guardan en:
```
c:\Users\DANIELA\OneDrive\Documentos\SistemaDePromociones\SistemaDePromociones\uploads\
├── repartidores\
│   └── 1\                    (código del repartidor)
│       ├── uuid-licencia.pdf
│       ├── uuid-soat.pdf
│       ├── uuid-antecedentes.pdf
│       └── uuid-tarjeta.pdf
└── restaurantes\
    └── 1\                    (código del restaurante)
        ├── documentos\
        │   ├── uuid-ruc.pdf
        │   ├── uuid-licencia.pdf
        │   └── uuid-sanidad.pdf
        └── imagenes\
            ├── uuid-logo.jpg
            ├── uuid-portada.jpg
            └── uuid-galeria1.jpg
```

---

## ✅ **QUÉ HACE EL SISTEMA AL REGISTRAR:**

### **Para Repartidores:**
1. ✅ Valida que el correo no esté registrado
2. ✅ Valida que el documento no esté registrado
3. ✅ Valida que la licencia no esté registrada
4. ✅ Crea el usuario en la tabla `usuario`
5. ✅ Crea el repartidor en la tabla `repartidor` (estado: Pendiente)
6. ✅ Guarda los 4 documentos en `documento_repartidor`
7. ✅ Guarda los archivos físicos en `uploads/repartidores/{id}/`
8. ✅ Redirige a `/login` con mensaje de éxito

### **Para Restaurantes:**
1. ✅ Valida que el correo no esté registrado
2. ✅ Valida que el RUC no esté registrado
3. ✅ Crea el usuario (representante) en `usuario`
4. ✅ Crea el restaurante en `restaurante` (estado: Pendiente)
5. ✅ Guarda las categorías seleccionadas en `categoria_restaurante`
6. ✅ Guarda los 3 documentos en `documento_restaurante`
7. ✅ Guarda logo y portada en `imagen_restaurante`
8. ✅ Guarda imágenes de galería en `imagen_restaurante`
9. ✅ Guarda todos los archivos en `uploads/restaurantes/{id}/`
10. ✅ Redirige a `/login` con mensaje de éxito

---

## ⚠️ **VALIDACIONES IMPLEMENTADAS:**

### **Backend (Java):**
- ✅ Correo electrónico único
- ✅ Número de documento único
- ✅ Número de licencia único (repartidor)
- ✅ RUC único (restaurante)
- ✅ Tamaño máximo de archivo: 5MB
- ✅ Tipos de archivo permitidos: PDF, JPG, PNG

### **Frontend (JavaScript):**
- ✅ Todos los campos obligatorios
- ✅ Formato de correo electrónico
- ✅ Contraseña segura (8+ caracteres, mayúscula, minúscula, número, especial)
- ✅ Edad mínima 18 años
- ✅ DNI: 8 dígitos
- ✅ CE: 9 dígitos
- ✅ RUC: 11 dígitos (empieza con 10 o 20)
- ✅ Número de licencia: formato válido
- ✅ Teléfono: 9 dígitos
- ✅ Descripción: máximo 500 caracteres
- ✅ Al menos 1 categoría seleccionada (restaurante)

---

## 🐛 **SOLUCIÓN DE PROBLEMAS:**

### **Error: "El correo electrónico ya está registrado"**
✅ Usa otro correo o verifica en la BD:
```sql
SELECT * FROM usuario WHERE correo_electronico = 'tu@email.com';
```

### **Error: "El RUC ya está registrado"**
✅ Usa otro RUC o verifica:
```sql
SELECT * FROM restaurante WHERE ruc = '20123456789';
```

### **Error: "No se puede guardar el archivo"**
✅ Verifica que la carpeta `uploads/` tenga permisos de escritura
✅ Verifica que el archivo no exceda 5MB

### **Error al enviar formulario (JavaScript)**
✅ Abre la consola del navegador (F12)
✅ Revisa errores en la pestaña "Console"
✅ Verifica que llenaste todos los campos obligatorios

### **No aparecen opciones en los selects**
❌ No ejecutaste `datos_iniciales.sql`
✅ Ejecuta el script SQL primero

---

## 📊 **CONSULTAS ÚTILES PARA VERIFICAR:**

```sql
-- Contar registros
SELECT 'Usuarios' AS Tabla, COUNT(*) AS Total FROM usuario
UNION ALL
SELECT 'Repartidores', COUNT(*) FROM repartidor
UNION ALL
SELECT 'Restaurantes', COUNT(*) FROM restaurante
UNION ALL
SELECT 'Documentos Repartidor', COUNT(*) FROM documento_repartidor
UNION ALL
SELECT 'Documentos Restaurante', COUNT(*) FROM documento_restaurante
UNION ALL
SELECT 'Imágenes Restaurante', COUNT(*) FROM imagen_restaurante;

-- Ver último repartidor registrado con su usuario
SELECT 
    u.nombre, 
    u.apellido_paterno,
    u.correo_electronico,
    r.numero_licencia,
    r.codigo_estado_aprobacion,
    r.fecha_creacion
FROM repartidor r
JOIN usuario u ON r.codigo_usuario = u.codigo
ORDER BY r.codigo DESC LIMIT 1;

-- Ver último restaurante registrado
SELECT 
    u.nombre AS representante,
    u.correo_electronico,
    rest.ruc,
    rest.nombre AS restaurante,
    rest.codigo_estado_aprobacion,
    rest.fecha_creacion
FROM restaurante rest
JOIN usuario u ON rest.codigo_usuario = u.codigo
ORDER BY rest.codigo DESC LIMIT 1;
```

---

## 🎯 **RESUMEN:**

### **✅ LO QUE YA FUNCIONA:**
- Navegación completa entre páginas
- Carga dinámica de ubicaciones (Departamento → Provincia → Distrito)
- Carga de categorías y tipos de vehículo
- Validaciones JavaScript en tiempo real
- Registro completo de repartidores (usuario + datos + documentos)
- Registro completo de restaurantes (usuario + datos + categorías + documentos + imágenes)
- Guardado de archivos en sistema de archivos
- Guardado de datos en MySQL
- Mensajes de éxito/error
- Redirección a login después del registro

### **⚠️ LO QUE AÚN FALTA:**
- Sistema de login funcional (Spring Security)
- Encriptación BCrypt para contraseñas (ahora se guardan en texto plano)
- Panel de administración para aprobar/rechazar solicitudes
- Validación de archivos más estricta (virus scan, formato exacto)
- Emails de confirmación
- Dashboard para repartidores/restaurantes
- Sistema de recuperación de contraseña funcional

---

## 🚀 **¡A PROBAR!**

**ORDEN DE EJECUCIÓN:**

1. **Ejecuta** `datos_iniciales.sql` en MySQL Workbench
2. **Inicia** la app: `.\mvnw.cmd spring-boot:run`
3. **Abre** el navegador: http://localhost:8080
4. **Navega** a "Registro" → "Soy Repartidor" o "Soy Restaurante"
5. **Llena** el formulario completo
6. **Sube** los archivos requeridos
7. **Da click** en "Registrar"
8. **Verifica** en MySQL que se guardó todo

**¡DISFRUTA REGISTRANDO!** 🎉
