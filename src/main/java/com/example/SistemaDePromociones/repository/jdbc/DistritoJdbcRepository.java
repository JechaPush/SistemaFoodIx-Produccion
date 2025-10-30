package com.example.SistemaDePromociones.repository.jdbc;

import com.example.SistemaDePromociones.model.Distrito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para Distrito usando JdbcTemplate (SQL directo)
 */
@Repository
public class DistritoJdbcRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private final RowMapper<Distrito> rowMapper = (rs, rowNum) -> {
        Distrito dist = new Distrito();
        dist.setCodigo(rs.getLong("Codigo"));
        dist.setNombre(rs.getString("Nombre"));
        dist.setCodigoProvincia(rs.getLong("CodigoProvincia"));
        dist.setEstado(rs.getBoolean("Estado"));
        return dist;
    };
    
    public List<Distrito> findByProvincia(Long codigoProvincia) {
        // Usar los nombres EXACTOS de las columnas
        String sql = "SELECT Codigo, Nombre, CodigoProvincia, Estado " +
                    "FROM distrito " +
                    "WHERE CodigoProvincia = ? AND Estado = 1 " +
                    "ORDER BY Nombre";
        return jdbcTemplate.query(sql, rowMapper, codigoProvincia);
    }
    
    public Distrito findByCodigo(Long codigo) {
        String sql = "SELECT Codigo, Nombre, CodigoProvincia, Estado FROM distrito WHERE Codigo = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, codigo);
    }
}
