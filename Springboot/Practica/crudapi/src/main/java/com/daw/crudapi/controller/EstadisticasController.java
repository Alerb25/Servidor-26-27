package com.daw.crudapi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.crudapi.model.Paciente;
import com.daw.crudapi.repository.CitaRepository;
import com.daw.crudapi.repository.PacienteRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/estadisticas")
@RequiredArgsConstructor
public class EstadisticasController {

    private final PacienteRepository pacienteRepository;
    private final CitaRepository citaRepository;

    /** Fila para dibujar una barra de progreso. */
    @Getter
    @AllArgsConstructor
    public static class Barra {
        private String etiqueta;
        private long valor;
        private long porcentaje;
    }

    @GetMapping
    public String ver(@RequestParam(defaultValue = "2") int minCitas,
            @RequestParam(defaultValue = "A+") String grupo, Model model) {

        long totalPacientes = pacienteRepository.count();
        long totalCitas = citaRepository.count();

        model.addAttribute("totalPacientes", totalPacientes);
        model.addAttribute("totalCitas", totalCitas);
        model.addAttribute("citasSinPaciente", citaRepository.countByPacienteIsNull());
        model.addAttribute("costeMedio", citaRepository.costeMedio());
        model.addAttribute("costeTotal", citaRepository.costeTotal());
        model.addAttribute("duracionMedia", citaRepository.duracionMedia());

        model.addAttribute("porGrupo", barras(pacienteRepository.contarPacientesPorGrupo(), totalPacientes));
        model.addAttribute("porEstado", barras(citaRepository.contarPorEstado(), totalCitas));

        model.addAttribute("masMayores", pacienteRepository.findTop5ByOrderByFechaNacimientoAsc());
        model.addAttribute("conMuchasCitas", pacienteRepository.findConAlMenosCitas(minCitas));
        model.addAttribute("minCitas", minCitas);

        model.addAttribute("grupos", Paciente.GRUPOS);
        model.addAttribute("grupoElegido", grupo);
        model.addAttribute("totalGrupoElegido", pacienteRepository.countByGrupoSanguineo(grupo));
        return "estadisticas";
    }

    private List<Barra> barras(List<Object[]> filas, long total) {
        List<Barra> res = new ArrayList<>();
        for (Object[] f : filas) {
            long valor = ((Number) f[1]).longValue();
            res.add(new Barra(String.valueOf(f[0]), valor, total > 0 ? valor * 100 / total : 0));
        }
        return res;
    }
}
