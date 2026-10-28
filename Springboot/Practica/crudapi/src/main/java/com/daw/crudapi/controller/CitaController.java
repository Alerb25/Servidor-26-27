package com.daw.crudapi.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.daw.crudapi.model.Cita;
import com.daw.crudapi.model.Paciente;
import com.daw.crudapi.repository.CitaRepository;
import com.daw.crudapi.repository.PacienteRepository;
import com.daw.crudapi.service.LogService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final LogService logService;

    @ModelAttribute("estados")
    public List<String> estados() {
        return Cita.ESTADOS;
    }

    @ModelAttribute("pacientes")
    public List<Paciente> pacientes() {
        return pacienteRepository.findAllByOrderByNombreCompletoAsc();
    }

    // GET /citas?page=0&size=10
    @GetMapping
    public String listar(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, Model model) {
        page = Math.max(page, 0);
        size = (size < 1 || size > 100) ? 10 : size;
        Page<Cita> pagina = citaRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fecha")));
        model.addAttribute("pagina", pagina);
        return "citas/lista";
    }

    // GET /citas/nuevo[?pacienteId=3] (si viene pacienteId, se preselecciona y se vuelve al detalle)
    @GetMapping("/nuevo")
    public String nuevo(@RequestParam(required = false) Long pacienteId, Model model) {
        Cita cita = new Cita();
        cita.setEstadoCita("Pendiente");
        cita.setFecha(LocalDate.now());
        model.addAttribute("cita", cita);
        model.addAttribute("pacienteId", pacienteId);
        model.addAttribute("volverPaciente", pacienteId);
        return "citas/form";
    }

    // POST /citas/crear
    @PostMapping("/crear")
    public String crear(@Valid @ModelAttribute("cita") Cita cita, BindingResult br,
            @RequestParam(required = false) Long pacienteId,
            @RequestParam(required = false) Long volverPaciente,
            Model model, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("pacienteId", pacienteId);
            model.addAttribute("volverPaciente", volverPaciente);
            return "citas/form";
        }
        cita.setIdCita(null);
        cita.setPaciente(buscarPaciente(pacienteId));
        Cita guardada = citaRepository.save(cita);
        logService.registrar("CREAR", "Cita", guardada.getIdCita(), descripcion(guardada));
        ra.addFlashAttribute("mensaje", "Cita creada correctamente");
        return volver(volverPaciente);
    }

    // GET /citas/editar/{id}[?volverPaciente=3]
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, @RequestParam(required = false) Long volverPaciente,
            Model model, RedirectAttributes ra) {
        Optional<Cita> op = citaRepository.findById(id);
        if (op.isEmpty()) {
            ra.addFlashAttribute("error", "La cita no existe");
            return "redirect:/citas";
        }
        Cita cita = op.get();
        model.addAttribute("cita", cita);
        model.addAttribute("pacienteId", cita.getPaciente() != null ? cita.getPaciente().getIdPaciente() : null);
        model.addAttribute("volverPaciente", volverPaciente);
        return "citas/form";
    }

    // POST /citas/modificar
    @PostMapping("/modificar")
    public String modificar(@Valid @ModelAttribute("cita") Cita cita, BindingResult br,
            @RequestParam(required = false) Long pacienteId,
            @RequestParam(required = false) Long volverPaciente,
            Model model, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("pacienteId", pacienteId);
            model.addAttribute("volverPaciente", volverPaciente);
            return "citas/form";
        }
        Optional<Cita> op = citaRepository.findById(cita.getIdCita());
        if (op.isEmpty()) {
            ra.addFlashAttribute("error", "La cita no existe");
            return "redirect:/citas";
        }
        Cita existente = op.get();
        existente.setMotivoCita(cita.getMotivoCita());
        existente.setEstadoCita(cita.getEstadoCita());
        existente.setObservaciones(cita.getObservaciones());
        existente.setDuracionMinutos(cita.getDuracionMinutos());
        existente.setCostoEstimado(cita.getCostoEstimado());
        existente.setFecha(cita.getFecha());
        existente.setPaciente(buscarPaciente(pacienteId));
        citaRepository.save(existente);
        logService.registrar("MODIFICAR", "Cita", existente.getIdCita(), descripcion(existente));
        ra.addFlashAttribute("mensaje", "Cita modificada correctamente");
        return volver(volverPaciente);
    }

    // GET /citas/eliminar/{id}[?volverPaciente=3]
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, @RequestParam(required = false) Long volverPaciente,
            RedirectAttributes ra) {
        Optional<Cita> op = citaRepository.findById(id);
        if (op.isEmpty()) {
            ra.addFlashAttribute("error", "La cita no existe");
            return volver(volverPaciente);
        }
        String desc = descripcion(op.get());
        citaRepository.deleteById(id);
        logService.registrar("ELIMINAR", "Cita", id, desc);
        ra.addFlashAttribute("mensaje", "Cita eliminada");
        return volver(volverPaciente);
    }

    // ------------------- ASOCIAR / DESASOCIAR -------------------

    @PostMapping("/asociar/{id}")
    public String asociar(@PathVariable Long id, @RequestParam Long pacienteId,
            @RequestParam(required = false) Long volverPaciente, RedirectAttributes ra) {
        Optional<Cita> op = citaRepository.findById(id);
        Paciente paciente = buscarPaciente(pacienteId);
        if (op.isEmpty() || paciente == null) {
            ra.addFlashAttribute("error", "No se pudo asociar la cita");
            return volver(volverPaciente);
        }
        Cita cita = op.get();
        cita.setPaciente(paciente);
        citaRepository.save(cita);
        logService.registrar("MODIFICAR", "Cita", id, "Asociada al paciente " + paciente.getNombreCompleto());
        ra.addFlashAttribute("mensaje", "Cita asociada a " + paciente.getNombreCompleto());
        return volver(volverPaciente);
    }

    @PostMapping("/desasociar/{id}")
    public String desasociar(@PathVariable Long id,
            @RequestParam(required = false) Long volverPaciente, RedirectAttributes ra) {
        Optional<Cita> op = citaRepository.findById(id);
        if (op.isEmpty()) {
            ra.addFlashAttribute("error", "La cita no existe");
            return volver(volverPaciente);
        }
        Cita cita = op.get();
        cita.setPaciente(null);
        citaRepository.save(cita);
        logService.registrar("MODIFICAR", "Cita", id, "Desasociada de su paciente");
        ra.addFlashAttribute("mensaje", "Cita desasociada");
        return volver(volverPaciente);
    }

    // ------------------- BORRADOS MASIVOS -------------------

    // Borra las citas con estado "Cancelada" anteriores a una fecha
    @PostMapping("/limpiar/canceladas")
    public String limpiarCanceladas(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            RedirectAttributes ra) {
        long borradas = citaRepository.deleteByEstadoCitaAndFechaBefore("Cancelada", fecha);
        logService.registrar("ELIMINAR", "Cita", null,
                "Borrado masivo: " + borradas + " citas canceladas anteriores a " + fecha);
        ra.addFlashAttribute("mensaje", borradas + " citas canceladas eliminadas");
        return "redirect:/citas";
    }

    // Borra las citas sin paciente que tengan un estado concreto
    @PostMapping("/limpiar/huerfanas")
    public String limpiarHuerfanas(@RequestParam String estado, RedirectAttributes ra) {
        long borradas = citaRepository.deleteByPacienteIsNullAndEstadoCita(estado);
        logService.registrar("ELIMINAR", "Cita", null,
                "Borrado masivo: " + borradas + " citas sin paciente en estado " + estado);
        ra.addFlashAttribute("mensaje", borradas + " citas sin paciente eliminadas");
        return "redirect:/citas";
    }

    // ------------------- AUXILIARES -------------------

    private Paciente buscarPaciente(Long id) {
        return id == null ? null : pacienteRepository.findById(id).orElse(null);
    }

    private String volver(Long volverPaciente) {
        return volverPaciente != null ? "redirect:/pacientes/detalle/" + volverPaciente : "redirect:/citas";
    }

    private String descripcion(Cita c) {
        return c.getMotivoCita() + " (" + c.getFecha() + ", " + c.getEstadoCita() + ")";
    }
}
