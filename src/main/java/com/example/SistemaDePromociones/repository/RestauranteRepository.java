package com.example.SistemaDePromociones.repository;

import com.example.SistemaDePromociones.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository para la entidad Restaurante
 */
@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    
    /**
     * Buscar restaurante por código de usuario
     */
    Optional<Restaurante> findByCodigoUsuario(Long codigoUsuario);
    
    /**
     * Buscar restaurante por RUC
     */
    Optional<Restaurante> findByRuc(String ruc);
    
    /**
     * Verificar si existe un RUC
     */
    boolean existsByRuc(String ruc);
}
