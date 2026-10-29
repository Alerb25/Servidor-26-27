package com.daw.crudapi.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.crudapi.model.Cita;
import com.daw.crudapi.model.Paciente;
import com.daw.crudapi.repository.CitaRepository;
import com.daw.crudapi.repository.PacienteRepository;

import lombok.RequiredArgsConstructor;

/** Búsqueda avanzada: cada formulario llama a uno de los métodos del repository. */
@Controller
@RequestMapping("/busqueda")
@RequiredArgsConstructor
public class BusquedaController {

    private final PacienteRepository pacienteRepository;
    private final CitaRepository citaRepository;

    @ModelAttribute("grupos")
    public List<String> grupos() {
        return Paciente.GRUPOS;
    }

    @ModelAttribute("estados")
    public List<String> estados() {
        return Cita.ESTADOS;
    }

    @GetMapping
    public String buscar(@RequestParam(required = false) String tipo,
            @RequestParam(required = false) String grupo,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String direccion,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) Double minCosto,
            @RequestParam(required = false) Integer minCitas,
            Model model) {

        if (tipo == null) {
            return "busqueda";
        }

        String nom = nombre == null ? "" : nombre.trim();
        String dir = direccion == null ? "" : direccion.trim();
        String est = estado == null ? "" : estado.trim();

        switch (tipo) {
            case "pacientes-grupo" -> {
                model.addAttribute("titulo", "Pacientes del grupo " + grupo + " cuyo nombre contiene \"" + nom + "\"");
                model.addAttribute("pacientes", pacienteRepository
                        .findByGrupoSanguineoAndNombreCompletoContainingIgnoreCase(grupo, nom));
            }
            case "pacientes-nacimiento" -> {
                LocalDate d = desde != null ? desde : LocalDate.of(1900, 1, 1);
                LocalDate h = hasta != null ? hasta : LocalDate.now();
                model.addAttribute("titulo", "Pacientes nacidos entre " + d + " y " + h + " con dirección que contiene \""
                        + dir + "\"");
                model.addAttribute("pacientes", pacienteRepository
                        .findByFechaNacimientoBetweenAndDireccionContainingIgnoreCase(d, h, dir));
            }
            case "pacientes-citas" -> {
                int min = minCitas != null ? minCitas : 1;
                model.addAttribute("titulo", "Pacientes con al menos " + min + " citas");
                model.addAttribute("pacientes", pacienteRepository.findConAlMenosCitas(min));
            }
            case "citas-filtros" -> {
                LocalDate d = desde != null ? desde : LocalDate.of(1900, 1, 1);
                LocalDate h = hasta != null ? hasta : LocalDate.of(2999, 12, 31);
                double c = minCosto != null ? minCosto : 0.0;
                model.addAttribute("titulo", "Citas filtradas (estado: " + (est.isEmpty() ? "todos" : est)
                        + ", coste mínimo " + c + ")");
                model.addAttribute("citas", citaRepository
                        .findByEstadoCitaContainingIgnoreCaseAndFechaBetweenAndCostoEstimadoGreaterThanEqualOrderByFechaDesc(
                                est, d, h, c));
            }
            case "citas-caras" -> {
                String e = est.isEmpty() ? "Pendiente" : est;
                model.addAttribute("titulo", "Citas \"" + e + "\" más caras que la media");
                model.addAttribute("citas", citaRepository.findMasCarasQueLaMedia(e));
            }
            default -> {
            }
        }
        model.addAttribute("buscado", true);
        return "busqueda";
    }
}
