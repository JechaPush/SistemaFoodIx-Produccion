# 📋 Documentación - Registro de Repartidores FoodIx

## ✅ Completado en registro-Repartidor.html

### 🎨 Frontend Completado

1. **Estructura HTML Completa**
   - Formulario multi-sección con indicador de pasos
   - Campos alineados con tu base de datos
   - Validaciones HTML5 nativas
   - Diseño responsive con Bootstrap 5

2. **JavaScript Implementado**
   - ✅ Validación de contraseñas en tiempo real
   - ✅ Validación de edad mínima (18 años)
   - ✅ Validación de documentos según tipo (DNI, Carnet, Pasaporte)
   - ✅ Validación de solo letras en nombres
   - ✅ Validación de solo números en teléfono
   - ✅ Carga dinámica de ubicaciones (Departamento → Provincia → Distrito)
   - ✅ Validación de archivos (tamaño y tipo)
   - ✅ Información dinámica según tipo de vehículo
   - ✅ Prevención de recarga accidental
   - ✅ Auto-guardado en localStorage (opcional)
   - ✅ Feedback visual de validación
   - ✅ Indicador de progreso por pasos

3. **CSS Personalizado**
   - Archivo `style.css` creado con estilos modernos
   - Animaciones y transiciones suaves
   - Diseño responsive
   - Estados de hover y focus mejorados

---

## 🔧 Tareas Pendientes para el Backend (Java/Spring Boot)

### 1. **Controlador de Registro**

Crear `RepartidorController.java`:

```java
@Controller
@RequestMapping("/auth")
public class RepartidorController {
    
    @Autowired
    private RepartidorService repartidorService;
    
    @Autowired
    private UbicacionService ubicacionService;
    
    // Mostrar formulario de registro
    @GetMapping("/registro-Repartidor")
    public String mostrarFormularioRegistro(Model model) {
        // Cargar departamentos
        List<Departamento> departamentos = ubicacionService.obtenerDepartamentos();
        model.addAttribute("departamentos", departamentos);
        return "registro-Repartidor";
    }
    
    // Procesar registro
    @PostMapping("/register-repartidor")
    public String registrarRepartidor(
        @ModelAttribute UsuarioDTO usuarioDTO,
        @RequestParam("CodigoTipoVehiculo") Integer codigoTipoVehiculo,
        @RequestParam("NumeroLicencia") String numeroLicencia,
        @RequestParam(value = "DocumentoLicencia", required = false) MultipartFile documentoLicencia,
        @RequestParam(value = "DocumentoSOAT", required = false) MultipartFile documentoSOAT,
        @RequestParam(value = "DocumentoAntecedentes", required = false) MultipartFile documentoAntecedentes,
        @RequestParam(value = "DocumentoTarjeta", required = false) MultipartFile documentoTarjeta,
        @RequestParam(value = "FotoRepartidor", required = false) MultipartFile fotoRepartidor,
        RedirectAttributes redirectAttributes
    ) {
        try {
            // 1. Validar datos
            if (repartidorService.existeCorreo(usuarioDTO.getCorreoElectronico())) {
                redirectAttributes.addFlashAttribute("error", "El correo ya está registrado");
                return "redirect:/auth/registro-Repartidor";
            }
            
            // 2. Crear usuario con rol Repartidor (Codigo = 4)
            usuarioDTO.setCodigoRol(4);
            Usuario usuario = repartidorService.crearUsuario(usuarioDTO);
            
            // 3. Crear registro de repartidor
            Repartidor repartidor = new Repartidor();
            repartidor.setCodigoUsuario(usuario.getCodigo());
            repartidor.setNumeroLicencia(numeroLicencia);
            repartidor.setCodigoTipoVehiculo(codigoTipoVehiculo);
            repartidor.setDisponible(true);
            repartidor.setCodigoEstadoAprobacion(1); // Pendiente
            
            Repartidor repartidorCreado = repartidorService.crearRepartidor(repartidor);
            
            // 4. Guardar documentos
            if (documentoLicencia != null && !documentoLicencia.isEmpty()) {
                repartidorService.guardarDocumento(repartidorCreado.getCodigo(), 
                    "Licencia", documentoLicencia);
            }
            if (documentoSOAT != null && !documentoSOAT.isEmpty()) {
                repartidorService.guardarDocumento(repartidorCreado.getCodigo(), 
                    "SOAT", documentoSOAT);
            }
            // ... más documentos
            
            // 5. Enviar notificación de bienvenida
            notificacionService.enviarNotificacionBienvenidaRepartidor(usuario.getCodigo());
            
            redirectAttributes.addFlashAttribute("message", 
                "¡Registro exitoso! Tu solicitud será revisada en 24-48 horas. Te notificaremos por correo.");
            return "redirect:/login";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error al procesar el registro: " + e.getMessage());
            return "redirect:/auth/registro-Repartidor";
        }
    }
}
```

### 2. **API REST para Ubicaciones**

Crear `UbicacionRestController.java`:

```java
@RestController
@RequestMapping("/api")
public class UbicacionRestController {
    
    @Autowired
    private UbicacionService ubicacionService;
    
    // Obtener provincias por departamento
    @GetMapping("/provincias/{codigoDepartamento}")
    public ResponseEntity<List<ProvinciaDTO>> obtenerProvincias(
        @PathVariable Integer codigoDepartamento
    ) {
        List<ProvinciaDTO> provincias = ubicacionService.obtenerProvinciasPorDepartamento(codigoDepartamento);
        return ResponseEntity.ok(provincias);
    }
    
    // Obtener distritos por provincia
    @GetMapping("/distritos/{codigoProvincia}")
    public ResponseEntity<List<DistritoDTO>> obtenerDistritos(
        @PathVariable Integer codigoProvincia
    ) {
        List<DistritoDTO> distritos = ubicacionService.obtenerDistritosPorProvincia(codigoProvincia);
        return ResponseEntity.ok(distritos);
    }
}
```

### 3. **Servicio de Repartidores**

Crear `RepartidorService.java`:

```java
@Service
public class RepartidorService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RepartidorRepository repartidorRepository;
    
    @Autowired
    private DocumentoRepartidorRepository documentoRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    public Usuario crearUsuario(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellidoPaterno(dto.getApellidoPaterno());
        usuario.setApellidoMaterno(dto.getApellidoMaterno());
        usuario.setNumeroDocumento(dto.getNumeroDocumento());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setCorreoElectronico(dto.getCorreoElectronico());
        usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        usuario.setTelefono(dto.getTelefono());
        usuario.setDireccion(dto.getDireccion());
        usuario.setCodigoTipoDocumento(dto.getCodigoTipoDocumento());
        usuario.setCodigoRol(4); // Repartidor
        usuario.setCodigoDistrito(dto.getCodigoDistrito());
        usuario.setEstado(true);
        
        return usuarioRepository.save(usuario);
    }
    
    public Repartidor crearRepartidor(Repartidor repartidor) {
        return repartidorRepository.save(repartidor);
    }
    
    public void guardarDocumento(Long codigoRepartidor, String tipoDocumento, 
                                  MultipartFile archivo) throws IOException {
        // Guardar archivo físicamente
        String rutaArchivo = fileStorageService.guardarArchivo(archivo, "repartidores/" + codigoRepartidor);
        
        // Guardar registro en BD
        DocumentoRepartidor documento = new DocumentoRepartidor();
        documento.setCodigoRepartidor(codigoRepartidor);
        documento.setTipoDocumento(tipoDocumento);
        documento.setRutaArchivo(rutaArchivo);
        documento.setEstado(true);
        
        documentoRepository.save(documento);
    }
    
    public boolean existeCorreo(String correo) {
        return usuarioRepository.existsByCorreoElectronico(correo);
    }
}
```

### 4. **Repositorios JPA**

```java
public interface RepartidorRepository extends JpaRepository<Repartidor, Long> {
    Optional<Repartidor> findByCodigoUsuario(Long codigoUsuario);
    List<Repartidor> findByCodigoEstadoAprobacion(Integer codigoEstadoAprobacion);
    List<Repartidor> findByDisponibleTrueAndCodigoEstadoAprobacion(Integer codigoEstado);
}

public interface DocumentoRepartidorRepository extends JpaRepository<DocumentoRepartidor, Long> {
    List<DocumentoRepartidor> findByCodigoRepartidor(Long codigoRepartidor);
}
```

### 5. **Servicio de Almacenamiento de Archivos**

```java
@Service
public class FileStorageService {
    
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;
    
    public String guardarArchivo(MultipartFile file, String carpeta) throws IOException {
        // Validar archivo
        validarArchivo(file);
        
        // Crear carpeta si no existe
        Path uploadPath = Paths.get(uploadDir, carpeta);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Generar nombre único
        String fileName = System.currentTimeMillis() + "_" + 
                         StringUtils.cleanPath(file.getOriginalFilename());
        
        // Guardar archivo
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        return carpeta + "/" + fileName;
    }
    
    private void validarArchivo(MultipartFile file) throws IOException {
        // Validar tamaño
        if (file.getSize() > 5 * 1024 * 1024) { // 5MB
            throw new IOException("El archivo excede el tamaño máximo permitido (5MB)");
        }
        
        // Validar tipo
        String contentType = file.getContentType();
        if (!contentType.equals("application/pdf") && 
            !contentType.startsWith("image/")) {
            throw new IOException("Solo se permiten archivos PDF e imágenes");
        }
    }
}
```

### 6. **DTOs (Data Transfer Objects)**

```java
@Data
public class UsuarioDTO {
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String numeroDocumento;
    private LocalDate fechaNacimiento;
    private String correoElectronico;
    private String contrasena;
    private String telefono;
    private String direccion;
    private Integer codigoTipoDocumento;
    private Integer codigoRol;
    private Integer codigoDistrito;
}

@Data
public class ProvinciaDTO {
    private Integer codigo;
    private String nombre;
}

@Data
public class DistritoDTO {
    private Integer codigo;
    private String nombre;
}
```

### 7. **Configuración de application.properties**

```properties
# Configuración de archivos
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=25MB

# Directorio de subida de archivos
app.upload.dir=uploads

# Configuración de base de datos (ajusta según tu configuración)
spring.datasource.url=jdbc:mysql://localhost:3306/DB_FoodIx?useSSL=false&serverTimezone=UTC
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Thymeleaf
spring.thymeleaf.cache=false
```

---

## 📊 Campos del Formulario vs Base de Datos

### Tabla Usuario
| Campo HTML | Campo BD | Tipo | Requerido |
|------------|----------|------|-----------|
| Nombre | Nombre | VARCHAR(50) | ✅ |
| ApellidoPaterno | ApellidoPaterno | VARCHAR(50) | ✅ |
| ApellidoMaterno | ApellidoMaterno | VARCHAR(50) | ✅ |
| NumeroDocumento | NumeroDocumento | VARCHAR(15) | ✅ |
| FechaNacimiento | FechaNacimiento | DATE | ✅ |
| CorreoElectronico | CorreoElectronico | VARCHAR(50) | ✅ |
| Contrasena | Contrasena | VARCHAR(255) | ✅ |
| Telefono | Telefono | VARCHAR(20) | ✅ |
| Direccion | Direccion | VARCHAR(100) | ❌ |
| CodigoTipoDocumento | CodigoTipoDocumento | TINYINT | ✅ |
| CodigoRol | CodigoRol | TINYINT | ✅ (valor 4) |
| CodigoDistrito | CodigoDistrito | SMALLINT | ✅ |

### Tabla Repartidor
| Campo HTML | Campo BD | Tipo | Requerido |
|------------|----------|------|-----------|
| NumeroLicencia | NumeroLicencia | VARCHAR(15) | ✅ |
| CodigoTipoVehiculo | CodigoTipoVehiculo | TINYINT | ✅ |
| - | CodigoUsuario | BIGINT | ✅ (FK) |
| - | Disponible | BOOLEAN | ✅ (default 1) |
| - | CodigoEstadoAprobacion | TINYINT | ✅ (default 1) |

### Tabla DocumentoRepartidor
| Campo HTML | Campo BD | Tipo | Requerido |
|------------|----------|------|-----------|
| DocumentoLicencia | TipoDocumento='Licencia' | ENUM | ✅ |
| DocumentoSOAT | TipoDocumento='SOAT' | ENUM | ❌ |
| DocumentoAntecedentes | TipoDocumento='AntecedentesPolicial' | ENUM | ❌ |
| DocumentoTarjeta | TipoDocumento='TarjetaPropiedad' | ENUM | ❌ |
| - | RutaArchivo | VARCHAR(255) | ✅ |

---

## 🔐 Validaciones Implementadas

### Frontend (JavaScript)
- ✅ Edad mínima 18 años
- ✅ DNI: 8 dígitos numéricos
- ✅ Carnet de Extranjería: máx. 15 alfanuméricos
- ✅ Pasaporte: máx. 15 alfanuméricos
- ✅ Teléfono: 9 dígitos
- ✅ Contraseña: mínimo 6 caracteres
- ✅ Confirmación de contraseña
- ✅ Nombres: solo letras
- ✅ Archivos: PDF, JPG, PNG (máx. 5MB)
- ✅ Foto perfil: JPG, PNG (máx. 2MB)

### Backend (Pendiente)
- ⚠️ Validar correo único
- ⚠️ Validar número de documento único
- ⚠️ Validar número de licencia único
- ⚠️ Encriptar contraseña (BCrypt)
- ⚠️ Validar edad servidor-side
- ⚠️ Sanitizar inputs (XSS)
- ⚠️ Validar tipos de archivo
- ⚠️ Limitar tamaño de archivos

---

## 🚀 Flujo de Registro

1. **Usuario completa formulario** → Frontend
2. **Validaciones en tiempo real** → JavaScript
3. **Submit del formulario** → POST a `/auth/register-repartidor`
4. **Controlador recibe datos** → RepartidorController
5. **Validación backend** → RepartidorService
6. **Crear Usuario** → Tabla Usuario (CodigoRol = 4)
7. **Crear Repartidor** → Tabla Repartidor (CodigoEstadoAprobacion = 1)
8. **Guardar documentos** → Tabla DocumentoRepartidor
9. **Enviar notificación** → NotificacionService
10. **Redirigir a login** → Con mensaje de éxito

---

## 📧 Notificaciones a Implementar

### 1. Email de Bienvenida
```
Asunto: ¡Bienvenido a FoodIx!
Cuerpo:
- Agradecimiento por registrarse
- Información sobre proceso de revisión (24-48 horas)
- Qué documentos fueron recibidos
- Contacto de soporte
```

### 2. Email de Aprobación
```
Asunto: ¡Tu cuenta ha sido aprobada!
Cuerpo:
- Felicitaciones
- Instrucciones para iniciar sesión
- Link a tutorial de repartidor
- Información de pagos
```

### 3. Email de Rechazo
```
Asunto: Actualización sobre tu solicitud
Cuerpo:
- Motivo del rechazo
- Documentos faltantes o incorrectos
- Opción para volver a aplicar
- Contacto de soporte
```

---

## 🎯 Próximos Pasos Recomendados

### Prioridad Alta
1. ✅ Implementar controlador de registro
2. ✅ Crear API REST para ubicaciones
3. ✅ Implementar servicio de archivos
4. ✅ Configurar seguridad (Spring Security)
5. ✅ Implementar encriptación de contraseñas

### Prioridad Media
6. ⚠️ Panel de administración para aprobar/rechazar repartidores
7. ⚠️ Sistema de notificaciones por email
8. ⚠️ Dashboard para repartidores
9. ⚠️ Sistema de calificaciones

### Prioridad Baja
10. ⚠️ Chat de soporte
11. ⚠️ Sistema de reportes
12. ⚠️ Estadísticas de ganancias

---

## 🧪 Testing Recomendado

### Unit Tests
```java
@Test
public void testCrearRepartidor_ExitosoConDocumentosCompletos() { }

@Test
public void testCrearRepartidor_CorreoDuplicado_DeberiaFallar() { }

@Test
public void testValidarEdad_MenorDe18_DeberiaFallar() { }

@Test
public void testGuardarArchivo_ArchivoGrande_DeberiaFallar() { }
```

### Integration Tests
```java
@Test
public void testRegistroCompleto_DesdeFormularioHastaBaseDeDatos() { }

@Test
public void testCargaDinamica_Departamentos_Provincias_Distritos() { }
```

---

## 📝 Notas Adicionales

- El formulario está completamente alineado con tu esquema de base de datos
- Se incluye auto-guardado en localStorage para prevenir pérdida de datos
- El indicador de pasos mejora la UX para formularios largos
- Las validaciones en tiempo real reducen errores de envío
- El sistema está preparado para escalabilidad futura

---

## 🆘 Soporte

Para dudas o problemas:
1. Revisar los logs de la aplicación
2. Verificar configuración de base de datos
3. Comprobar permisos de carpetas de upload
4. Validar configuración de CORS si usas API separada

---

**Última actualización**: 30 de Octubre, 2025
**Versión**: 1.0
**Autor**: GitHub Copilot
