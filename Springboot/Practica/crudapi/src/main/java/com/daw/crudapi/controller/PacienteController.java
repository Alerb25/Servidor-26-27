package com.daw.crudapi.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.daw.crudapi.model.Paciente;
import com.daw.crudapi.repository.CitaRepository;
import com.daw.crudapi.repository.PacienteRepository;
import com.daw.crudapi.service.LogService;
import com.lowagie.text.Document;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final PacienteRepository pacienteRepository;
    private final CitaRepository citaRepository;
    private final LogService logService;

    @ModelAttribute("grupos")
    public List<String> grupos() {
        return Paciente.GRUPOS;
    }

    // GET /pacientes?page=0&size=5 - Listado paginado
    @GetMapping
    public String listar(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size, Model model) {
        page = Math.max(page, 0);
        size = (size < 1 || size > 100) ? 5 : size;
        Page<Paciente> pagina = pacienteRepository.findAll(PageRequest.of(page, size, Sort.by("nombreCompleto")));
        model.addAttribute("pagina", pagina);
        return "pacientes/lista";
    }

    // GET /pacientes/nuevo
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "pacientes/form";
    }

    // POST /pacientes/crear
    @PostMapping("/crear")
    public String crear(@Valid @ModelAttribute("paciente") Paciente paciente, BindingResult br,
            RedirectAttributes ra) {
        if (br.hasErrors()) {
            return "pacientes/form";
        }
        paciente.setIdPaciente(null);
        Paciente guardado = pacienteRepository.save(paciente);
        logService.registrar("CREAR", "Paciente", guardado.getIdPaciente(), guardado.getNombreCompleto());
        ra.addFlashAttribute("mensaje", "Paciente creado correctamente");
        return "redirect:/pacientes";
    }

    // GET /pacientes/editar/{id}
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Paciente> op = pacienteRepository.findById(id);
        if (op.isEmpty()) {
            ra.addFlashAttribute("error", "El paciente no existe");
            return "redirect:/pacientes";
        }
        model.addAttribute("paciente", op.get());
        return "pacientes/form";
    }

    // POST /pacientes/modificar
    @PostMapping("/modificar")
    public String modificar(@Valid @ModelAttribute("paciente") Paciente paciente, BindingResult br,
            RedirectAttributes ra) {
        if (br.hasErrors()) {
            return "pacientes/form";
        }
        Optional<Paciente> op = pacienteRepository.findById(paciente.getIdPaciente());
        if (op.isEmpty()) {
            ra.addFlashAttribute("error", "El paciente no existe");
            return "redirect:/pacientes";
        }
        Paciente existente = op.get();
        existente.setNombreCompleto(paciente.getNombreCompleto());
        existente.setFechaNacimiento(paciente.getFechaNacimiento());
        existente.setDireccion(paciente.getDireccion());
        existente.setGrupoSanguineo(paciente.getGrupoSanguineo());
        pacienteRepository.save(existente);
        logService.registrar("MODIFICAR", "Paciente", existente.getIdPaciente(), existente.getNombreCompleto());
        ra.addFlashAttribute("mensaje", "Paciente modificado correctamente");
        return "redirect:/pacientes";
    }

    // GET /pacientes/eliminar/{id} (la confirmación se hace con un modal en la vista)
    @GetMapping("/eliminar/{id}")
    @Transactional
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        Optional<Paciente> op = pacienteRepository.findById(id);
        if (op.isEmpty()) {
            ra.addFlashAttribute("error", "El paciente no existe");
            return "redirect:/pacientes";
        }
        String nombre = op.get().getNombreCompleto();
        // Las citas del paciente no se borran: quedan sin paciente asignado
        int desasociadas = citaRepository.desasociarDePaciente(id);
        pacienteRepository.deleteById(id);
        logService.registrar("ELIMINAR", "Paciente", id, nombre + " (citas desasociadas: " + desasociadas + ")");
        ra.addFlashAttribute("mensaje", "Paciente eliminado. Citas desasociadas: " + desasociadas);
        return "redirect:/pacientes";
    }

    // GET /pacientes/detalle/{id} - Vista detallada con sus citas
    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Paciente> op = pacienteRepository.findById(id);
        if (op.isEmpty()) {
            ra.addFlashAttribute("error", "El paciente no existe");
            return "redirect:/pacientes";
        }
        model.addAttribute("paciente", op.get());
        model.addAttribute("citas", citaRepository.findByPaciente_IdPacienteOrderByFechaDesc(id));
        return "pacientes/detalle";
    }

    // ------------------------- EXPORTACIÓN -------------------------

    @GetMapping("/exportar/csv")
    public void exportarCsv(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"pacientes.csv\"");
        PrintWriter w = response.getWriter();
        w.write('\uFEFF'); // BOM para que Excel respete las tildes
        w.write("ID;Nombre completo;Fecha de nacimiento;Dirección;Grupo sanguíneo\n");
        for (Paciente p : pacienteRepository.findAllByOrderByNombreCompletoAsc()) {
            w.write(p.getIdPaciente() + ";" + csv(p.getNombreCompleto()) + ";" + p.getFechaNacimiento().format(FMT)
                    + ";" + csv(p.getDireccion()) + ";" + csv(p.getGrupoSanguineo()) + "\n");
        }
        w.flush();
    }

    @GetMapping("/exportar/pdf")
    public void exportarPdf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"pacientes.pdf\"");

        Document doc = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(doc, response.getOutputStream());
        doc.open();
        doc.add(new Paragraph("Listado de pacientes", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
        doc.add(new Paragraph(" "));

        PdfPTable tabla = new PdfPTable(new float[] { 1, 4, 2, 5, 2 });
        tabla.setWidthPercentage(100);
        for (String h : new String[] { "ID", "Nombre", "Nacimiento", "Dirección", "Grupo" }) {
            PdfPCell cell = new PdfPCell(new Phrase(h, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
            cell.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
            tabla.addCell(cell);
        }
        for (Paciente p : pacienteRepository.findAllByOrderByNombreCompletoAsc()) {
            tabla.addCell(celda(String.valueOf(p.getIdPaciente())));
            tabla.addCell(celda(p.getNombreCompleto()));
            tabla.addCell(celda(p.getFechaNacimiento().format(FMT)));
            tabla.addCell(celda(p.getDireccion()));
            tabla.addCell(celda(p.getGrupoSanguineo()));
        }
        doc.add(tabla);
        doc.close();
    }

    private PdfPCell celda(String texto) {
        return new PdfPCell(new Phrase(texto, FontFactory.getFont(FontFactory.HELVETICA, 9)));
    }

    private String csv(String valor) {
        if (valor == null) {
            return "";
        }
        return "\"" + valor.replace("\"", "\"\"") + "\"";
    }
}
