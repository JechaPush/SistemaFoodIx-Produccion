package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.dto.RepartidorRegistroDTO;
import com.example.SistemaDePromociones.model.Departamento;
import com.example.SistemaDePromociones.model.Repartidor;
import com.example.SistemaDePromociones.model.TipoVehiculo;
import com.example.SistemaDePromociones.repository.jdbc.DepartamentoJdbcRepository;
import com.example.SistemaDePromociones.repository.TipoVehiculoRepository;
import com.example.SistemaDePromociones.service.RepartidorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller para el registro de repartidores
 */
@Controller
@RequestMapping("/registro-repartidor")
public class RepartidorController {
    
    @Autowired
    private DepartamentoJdbcRepository departamentoRepository;
    
    @Autowired
    private TipoVehiculoRepository tipoVehiculoRepository;
    
    @Autowired
    private RepartidorService repartidorService;
    
    /**
     * Mostrar formulario de registro de repartidor
     * GET /registro-repartidor
     */
    @GetMapping
    public String mostrarFormulario(Model model) {
        // Cargar departamentos para el select usando JDBC
        List<Departamento> departamentos = departamentoRepository.findAllActivos();
        model.addAttribute("departamentos", departamentos);
        
        // Cargar tipos de vehículo para el select
        List<TipoVehiculo> tiposVehiculo = tipoVehiculoRepository.findByEstadoTrue();
        model.addAttribute("tiposVehiculo", tiposVehiculo);
        
        return "registro-Repartidor";
    }
    
    /**
     * Procesar registro de repartidor
     * POST /registro-repartidor
     */
    @PostMapping
    public String registrarRepartidor(
            @ModelAttribute RepartidorRegistroDTO dto,
            RedirectAttributes redirectAttributes) {
        try {
            Repartidor repartidor = repartidorService.registrarRepartidor(dto);
            redirectAttributes.addFlashAttribute("mensaje", 
                "¡Registro exitoso! Tu solicitud está en revisión. Código: " + repartidor.getCodigo());
            redirectAttributes.addFlashAttribute("tipo", "success");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error al registrar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
            return "redirect:/registro-repartidor";
        }
    }
}
