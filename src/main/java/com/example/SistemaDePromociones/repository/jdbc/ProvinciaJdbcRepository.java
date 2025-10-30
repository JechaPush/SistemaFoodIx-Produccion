package com.example.SistemaDePromociones.repository.jdbc;

import com.example.SistemaDePromociones.model.Provincia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para Provincia usando JdbcTemplate (SQL directo)
 */
@Repository
public class ProvinciaJdbcRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private final RowMapper<Provincia> rowMapper = (rs, rowNum) -> {
        Provincia prov = new Provincia();
        prov.setCodigo(rs.getLong("Codigo"));
        prov.setNombre(rs.getString("Nombre"));
        prov.setCodigoDepartamento(rs.getLong("CodigoDepartamento"));
        prov.setEstado(rs.getBoolean("Estado"));
        return prov;
    };
    
    public List<Provincia> findByDepartamento(Long codigoDepartamento) {
        // Usar los nombres EXACTOS de las columnas como están en la base de datos
        String sql = "SELECT Codigo, Nombre, CodigoDepartamento, Estado " +
                    "FROM provincia " +
                    "WHERE CodigoDepartamento = ? AND Estado = 1 " +
                    "ORDER BY Nombre";
        return jdbcTemplate.query(sql, rowMapper, codigoDepartamento);
    }
    
    public Provincia findByCodigo(Long codigo) {
        String sql = "SELECT Codigo, Nombre, CodigoDepartamento, Estado FROM provincia WHERE Codigo = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, codigo);
    }
}
