package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Entidad Restaurante - Representa la tabla Restaurante en la base de datos
 * Almacena información de los restaurantes registrados en la plataforma
 */
@Entity
@Table(name = "Restaurante")
@Data
public class Restaurante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Codigo")
    private Long codigo;
    
    @Column(name = "CodigoUsuario", nullable = false)
    private Long codigoUsuario;
    
    @Column(name = "RUC", nullable = false, unique = true, length = 15)
    private String ruc;
    
    @Column(name = "RazonSocial", nullable = false, length = 150)
    private String razonSocial;
    
    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "Descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "Direccion", length = 200)
    private String direccion;
    
    @Column(name = "Telefono", length = 20)
    private String telefono;
    
    @Column(name = "CorreoElectronico", length = 150)
    private String correoElectronico;
    
    @Column(name = "CodigoDistrito", nullable = false)
    private Long codigoDistrito;
    
    @Column(name = "CodigoEstadoAprobacion", nullable = false)
    private Long codigoEstadoAprobacion = 1L; // 1 = Pendiente, 2 = Aprobado, 3 = Rechazado
    
    @Column(name = "FechaAprobacion")
    private LocalDateTime fechaAprobacion;
    
    @Column(name = "CodigoAprobador")
    private Long codigoAprobador;
    
    @Column(name = "MotivoRechazo", columnDefinition = "TEXT")
    private String motivoRechazo;
    
    @Column(name = "FechaCreacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "Estado", nullable = false)
    private Boolean estado = true;
    
    // Relaciones (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CodigoUsuario", insertable = false, updatable = false)
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CodigoDistrito", insertable = false, updatable = false)
    private Distrito distrito;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CodigoEstadoAprobacion", insertable = false, updatable = false)
    private EstadoAprobacion estadoAprobacion;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (estado == null) {
            estado = true;
        }
        if (codigoEstadoAprobacion == null) {
            codigoEstadoAprobacion = 1L; // Pendiente
        }
    }
}
