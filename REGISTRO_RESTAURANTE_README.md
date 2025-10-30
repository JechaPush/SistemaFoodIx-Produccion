# 📋 Documentación - Registro de Restaurantes FoodIx

## ✅ Completado en registro-Restaurante.html

### 🎨 Frontend Completado

1. **Estructura HTML Completa**
   - Formulario multi-sección con indicador de pasos (3 secciones)
   - Campos alineados con tu base de datos
   - Validaciones HTML5 nativas
   - Diseño responsive con Bootstrap 5

2. **JavaScript Implementado**
   - ✅ Validación de contraseñas en tiempo real
   - ✅ Validación de edad mínima (18 años)
   - ✅ Validación de documentos según tipo (DNI, Carnet, Pasaporte)
   - ✅ Validación de solo letras en nombres
   - ✅ Validación de solo números en teléfono
   - ✅ Validación de RUC (11 dígitos)
   - ✅ Carga dinámica de ubicaciones para RESPONSABLE y RESTAURANTE
   - ✅ Validación de archivos (tamaño y tipo)
   - ✅ Validación de categorías (al menos 1)
   - ✅ Prevención de recarga accidental
   - ✅ Auto-guardado en localStorage
   - ✅ Contador de caracteres para descripción
   - ✅ Feedback visual de validación
   - ✅ Indicador de progreso por pasos

---

## 🔧 Tareas Pendientes para el Backend (Java/Spring Boot)

### 1. **Controlador de Registro**

Crear `RestauranteController.java`:

```java
@Controller
@RequestMapping("/auth")
public class RestauranteController {
    
    @Autowired
    private RestauranteService restauranteService;
    
    @Autowired
    private UbicacionService ubicacionService;
    
    @Autowired
    private CategoriaService categoriaService;
    
    // Mostrar formulario de registro
    @GetMapping("/registro-Restaurante")
    public String mostrarFormularioRegistro(Model model) {
        // Cargar departamentos
        List<Departamento> departamentos = ubicacionService.obtenerDepartamentos();
        model.addAttribute("departamentos", departamentos);
        
        // Cargar categorías
        List<Categoria> categorias = categoriaService.obtenerCategoriasActivas();
        model.addAttribute("categorias", categorias);
        
        return "registro-Restaurante";
    }
    
    // Procesar registro
    @PostMapping("/register-restaurante")
    public String registrarRestaurante(
        @ModelAttribute UsuarioDTO usuarioDTO,
        @RequestParam("RUC") String ruc,
        @RequestParam("RazonSocial") String razonSocial,
        @RequestParam("NombreRestaurante") String nombreRestaurante,
        @RequestParam(value = "DescripcionRestaurante", required = false) String descripcion,
        @RequestParam("DireccionRestaurante") String direccionRestaurante,
        @RequestParam(value = "TelefonoRestaurante", required = false) String telefonoRestaurante,
        @RequestParam(value = "CorreoRestaurante", required = false) String correoRestaurante,
        @RequestParam("CodigoDistritoRestaurante") Integer codigoDistritoRestaurante,
        @RequestParam("CodigosCategorias") List<Integer> codigosCategorias,
        @RequestParam(value = "DocumentoRUC", required = false) MultipartFile documentoRUC,
        @RequestParam(value = "DocumentoLicencia", required = false) MultipartFile documentoLicencia,
        @RequestParam(value = "DocumentoSanidad", required = false) MultipartFile documentoSanidad,
        @RequestParam(value = "LogoRestaurante", required = false) MultipartFile logoRestaurante,
        RedirectAttributes redirectAttributes
    ) {
        try {
            // 1. Validar que el correo no exista
            if (restauranteService.existeCorreo(usuarioDTO.getCorreoElectronico())) {
                redirectAttributes.addFlashAttribute("error", "El correo ya está registrado");
                return "redirect:/auth/registro-Restaurante";
            }
            
            // 2. Validar que el RUC no exista
            if (restauranteService.existeRUC(ruc)) {
                redirectAttributes.addFlashAttribute("error", "El RUC ya está registrado");
                return "redirect:/auth/registro-Restaurante";
            }
            
            // 3. Validar que se haya seleccionado al menos una categoría
            if (codigosCategorias == null || codigosCategorias.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Debes seleccionar al menos una categoría");
                return "redirect:/auth/registro-Restaurante";
            }
            
            // 4. Crear usuario con rol Restaurante (Codigo = 3)
            usuarioDTO.setCodigoRol(3);
            Usuario usuario = restauranteService.crearUsuario(usuarioDTO);
            
            // 5. Crear registro de restaurante
            Restaurante restaurante = new Restaurante();
            restaurante.setCodigoUsuario(usuario.getCodigo());
            restaurante.setRuc(ruc);
            restaurante.setRazonSocial(razonSocial);
            restaurante.setNombre(nombreRestaurante);
            restaurante.setDescripcion(descripcion);
            restaurante.setDireccion(direccionRestaurante);
            restaurante.setTelefono(telefonoRestaurante);
            restaurante.setCorreoElectronico(correoRestaurante);
            restaurante.setCodigoDistrito(codigoDistritoRestaurante);
            restaurante.setCodigoEstadoAprobacion(1); // Pendiente
            
            Restaurante restauranteCreado = restauranteService.crearRestaurante(restaurante);
            
            // 6. Asociar categorías al restaurante
            restauranteService.asociarCategorias(restauranteCreado.getCodigo(), codigosCategorias);
            
            // 7. Guardar documentos
            if (documentoRUC != null && !documentoRUC.isEmpty()) {
                restauranteService.guardarDocumento(restauranteCreado.getCodigo(), 
                    "RUC", documentoRUC);
            }
            if (documentoLicencia != null && !documentoLicencia.isEmpty()) {
                restauranteService.guardarDocumento(restauranteCreado.getCodigo(), 
                    "LicenciaFuncionamiento", documentoLicencia);
            }
            if (documentoSanidad != null && !documentoSanidad.isEmpty()) {
                restauranteService.guardarDocumento(restauranteCreado.getCodigo(), 
                    "CarnetSanidad", documentoSanidad);
            }
            
            // 8. Guardar logo
            if (logoRestaurante != null && !logoRestaurante.isEmpty()) {
                restauranteService.guardarImagen(restauranteCreado.getCodigo(), 
                    "Logo", logoRestaurante);
            }
            
            // 9. Enviar notificación de bienvenida
            notificacionService.enviarNotificacionBienvenidaRestaurante(usuario.getCodigo());
            
            redirectAttributes.addFlashAttribute("message", 
                "¡Registro exitoso! Tu solicitud será revisada en 24-48 horas. Te notificaremos por correo.");
            return "redirect:/login";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error al procesar el registro: " + e.getMessage());
            return "redirect:/auth/registro-Restaurante";
        }
    }
}
```

### 2. **Servicio de Restaurantes**

Crear `RestauranteService.java`:

```java
@Service
public class RestauranteService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RestauranteRepository restauranteRepository;
    
    @Autowired
    private DocumentoRestauranteRepository documentoRestauranteRepository;
    
    @Autowired
    private ImagenRestauranteRepository imagenRestauranteRepository;
    
    @Autowired
    private CategoriaRestauranteRepository categoriaRestauranteRepository;
    
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
        usuario.setCodigoRol(3); // Restaurante
        usuario.setCodigoDistrito(dto.getCodigoDistrito());
        usuario.setEstado(true);
        
        return usuarioRepository.save(usuario);
    }
    
    public Restaurante crearRestaurante(Restaurante restaurante) {
        return restauranteRepository.save(restaurante);
    }
    
    public void asociarCategorias(Long codigoRestaurante, List<Integer> codigosCategorias) {
        for (Integer codigoCategoria : codigosCategorias) {
            CategoriaRestaurante catRest = new CategoriaRestaurante();
            catRest.setCodigoRestaurante(codigoRestaurante);
            catRest.setCodigoCategoria(codigoCategoria);
            categoriaRestauranteRepository.save(catRest);
        }
    }
    
    public void guardarDocumento(Long codigoRestaurante, String tipoDocumento, 
                                  MultipartFile archivo) throws IOException {
        // Guardar archivo físicamente
        String rutaArchivo = fileStorageService.guardarArchivo(archivo, 
            "restaurantes/" + codigoRestaurante + "/documentos");
        
        // Guardar registro en BD
        DocumentoRestaurante documento = new DocumentoRestaurante();
        documento.setCodigoRestaurante(codigoRestaurante);
        documento.setTipoDocumento(DocumentoRestaurante.TipoDocumentoRestaurante.valueOf(tipoDocumento));
        documento.setRutaArchivo(rutaArchivo);
        documento.setEstado(true);
        
        documentoRestauranteRepository.save(documento);
    }
    
    public void guardarImagen(Long codigoRestaurante, String tipoImagen, 
                              MultipartFile archivo) throws IOException {
        // Guardar archivo físicamente
        String rutaImagen = fileStorageService.guardarArchivo(archivo, 
            "restaurantes/" + codigoRestaurante + "/imagenes");
        
        // Guardar registro en BD
        ImagenRestaurante imagen = new ImagenRestaurante();
        imagen.setCodigoRestaurante(codigoRestaurante);
        imagen.setTipoImagen(ImagenRestaurante.TipoImagenRestaurante.valueOf(tipoImagen));
        imagen.setRutaImagen(rutaImagen);
        imagen.setEstado(true);
        
        imagenRestauranteRepository.save(imagen);
    }
    
    public boolean existeCorreo(String correo) {
        return usuarioRepository.existsByCorreoElectronico(correo);
    }
    
    public boolean existeRUC(String ruc) {
        return restauranteRepository.existsByRuc(ruc);
    }
}
```

### 3. **Repositorios JPA**

```java
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    Optional<Restaurante> findByCodigoUsuario(Long codigoUsuario);
    List<Restaurante> findByCodigoEstadoAprobacion(Integer codigoEstadoAprobacion);
    boolean existsByRuc(String ruc);
    Optional<Restaurante> findByRuc(String ruc);
}

public interface DocumentoRestauranteRepository extends JpaRepository<DocumentoRestaurante, Long> {
    List<DocumentoRestaurante> findByCodigoRestaurante(Long codigoRestaurante);
}

public interface ImagenRestauranteRepository extends JpaRepository<ImagenRestaurante, Long> {
    List<ImagenRestaurante> findByCodigoRestaurante(Long codigoRestaurante);
    Optional<ImagenRestaurante> findByCodigoRestauranteAndTipoImagen(
        Long codigoRestaurante, 
        ImagenRestaurante.TipoImagenRestaurante tipoImagen
    );
}

public interface CategoriaRestauranteRepository extends JpaRepository<CategoriaRestaurante, CategoriaRestauranteId> {
    List<CategoriaRestaurante> findByCodigoRestaurante(Long codigoRestaurante);
}

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    List<Categoria> findByEstadoTrue();
}
```

### 4. **Servicio de Categorías**

```java
@Service
public class CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    public List<Categoria> obtenerCategoriasActivas() {
        return categoriaRepository.findByEstadoTrue();
    }
}
```

---

## 📊 Campos del Formulario vs Base de Datos

### Tabla Usuario (Responsable del Restaurante)
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
| DireccionPersonal | Direccion | VARCHAR(100) | ❌ |
| CodigoTipoDocumento | CodigoTipoDocumento | TINYINT | ✅ |
| CodigoRol | CodigoRol | TINYINT | ✅ (valor 3) |
| CodigoDistrito | CodigoDistrito | SMALLINT | ✅ |

### Tabla Restaurante
| Campo HTML | Campo BD | Tipo | Requerido |
|------------|----------|------|-----------|
| RUC | RUC | VARCHAR(15) | ✅ |
| RazonSocial | RazonSocial | VARCHAR(150) | ✅ |
| NombreRestaurante | Nombre | VARCHAR(100) | ✅ |
| DescripcionRestaurante | Descripcion | TEXT | ❌ |
| DireccionRestaurante | Direccion | VARCHAR(200) | ✅ |
| TelefonoRestaurante | Telefono | VARCHAR(20) | ❌ |
| CorreoRestaurante | CorreoElectronico | VARCHAR(150) | ❌ |
| CodigoDistritoRestaurante | CodigoDistrito | SMALLINT | ✅ |
| - | CodigoUsuario | BIGINT | ✅ (FK) |
| - | CodigoEstadoAprobacion | TINYINT | ✅ (default 1) |

### Tabla CategoriaRestaurante
| Campo HTML | Campo BD | Tipo | Requerido |
|------------|----------|------|-----------|
| CodigosCategorias[] | CodigoCategoria | INT | ✅ (al menos 1) |

### Tabla DocumentoRestaurante
| Campo HTML | Campo BD | Tipo | Requerido |
|------------|----------|------|-----------|
| DocumentoRUC | TipoDocumento='RUC' | ENUM | ✅ |
| DocumentoLicencia | TipoDocumento='LicenciaFuncionamiento' | ENUM | ❌ |
| DocumentoSanidad | TipoDocumento='CarnetSanidad' | ENUM | ❌ |

### Tabla ImagenRestaurante
| Campo HTML | Campo BD | Tipo | Requerido |
|------------|----------|------|-----------|
| LogoRestaurante | TipoImagen='Logo' | ENUM | ❌ |

---

## 🔐 Validaciones Implementadas

### Frontend (JavaScript)
- ✅ Edad mínima 18 años
- ✅ DNI: 8 dígitos numéricos
- ✅ RUC: 11 dígitos numéricos (inicia con 10 o 20)
- ✅ Teléfono: 9 dígitos
- ✅ Contraseña: mínimo 6 caracteres
- ✅ Confirmación de contraseña
- ✅ Nombres: solo letras
- ✅ Al menos 1 categoría seleccionada
- ✅ Archivos: PDF, JPG, PNG (máx. 5MB documentos, 2MB logo)
- ✅ Descripción: máximo 500 caracteres

### Backend (Pendiente)
- ⚠️ Validar correo único
- ⚠️ Validar RUC único
- ⚠️ Validar RUC válido (estructura SUNAT)
- ⚠️ Encriptar contraseña (BCrypt)
- ⚠️ Validar edad servidor-side
- ⚠️ Sanitizar inputs (XSS)
- ⚠️ Validar tipos de archivo
- ⚠️ Limitar tamaño de archivos

---

## 🚀 Flujo de Registro

1. **Usuario completa formulario** → Frontend
2. **Validaciones en tiempo real** → JavaScript
3. **Submit del formulario** → POST a `/auth/register-restaurante`
4. **Controlador recibe datos** → RestauranteController
5. **Validación backend** → RestauranteService
6. **Crear Usuario** → Tabla Usuario (CodigoRol = 3)
7. **Crear Restaurante** → Tabla Restaurante (CodigoEstadoAprobacion = 1)
8. **Asociar categorías** → Tabla CategoriaRestaurante
9. **Guardar documentos** → Tabla DocumentoRestaurante
10. **Guardar imágenes** → Tabla ImagenRestaurante
11. **Enviar notificación** → NotificacionService
12. **Redirigir a login** → Con mensaje de éxito

---

## 📝 Diferencias con Registro de Repartidor

| Aspecto | Repartidor | Restaurante |
|---------|-----------|-------------|
| Rol | 4 | 3 |
| Ubicación | Una sola | Dos (personal + negocio) |
| Documento único | Licencia conducir | RUC |
| Categorías | No aplica | Sí (múltiples) |
| Vehículo | Sí | No |
| Logo/Imágenes | Foto personal | Logo negocio |
| Horarios | Disponibilidad | No aplica |

---

## 🎯 Entidades JPA Creadas

- ✅ `Restaurante.java`
- ✅ `Categoria.java`
- ✅ `CategoriaRestaurante.java`
- ✅ `CategoriaRestauranteId.java`
- ✅ `ImagenRestaurante.java`
- ✅ `DocumentoRestaurante.java`

---

**Estado**: ✅ Frontend 100% Completo | ⚠️ Backend 50% Completo (Entidades Listas)

**Fecha**: 30 de Octubre, 2025
