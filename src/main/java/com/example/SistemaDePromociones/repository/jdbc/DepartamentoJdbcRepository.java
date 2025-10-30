package com.example.SistemaDePromociones.repository.jdbc;

import com.example.SistemaDePromociones.model.Departamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para Departamento usando JdbcTemplate (SQL directo)
 */
@Repository
public class DepartamentoJdbcRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private final RowMapper<Departamento> rowMapper = (rs, rowNum) -> {
        Departamento dept = new Departamento();
        dept.setCodigo(rs.getLong("Codigo"));
        dept.setNombre(rs.getString("Nombre"));
        dept.setEstado(rs.getBoolean("Estado"));
        return dept;
    };
    
    public List<Departamento> findAllActivos() {
        // Usar los nombres EXACTOS de las columnas
        String sql = "SELECT Codigo, Nombre, Estado FROM departamento WHERE Estado = 1 ORDER BY Nombre";
        return jdbcTemplate.query(sql, rowMapper);
    }
    
    public Departamento findByCodigo(Long codigo) {
        String sql = "SELECT Codigo, Nombre, Estado FROM departamento WHERE Codigo = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, codigo);
    }
}
