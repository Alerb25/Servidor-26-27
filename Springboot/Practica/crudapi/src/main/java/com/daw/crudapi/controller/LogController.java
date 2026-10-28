package com.daw.crudapi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.crudapi.model.LogAccion;
import com.daw.crudapi.repository.LogAccionRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogAccionRepository logRepository;

    @GetMapping
    public String listar(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size, Model model) {
        page = Math.max(page, 0);
        size = (size < 1 || size > 100) ? 15 : size;
        Page<LogAccion> pagina = logRepository.findAllByOrderByFechaHoraDesc(PageRequest.of(page, size));
        model.addAttribute("pagina", pagina);
        return "logs";
    }
}
