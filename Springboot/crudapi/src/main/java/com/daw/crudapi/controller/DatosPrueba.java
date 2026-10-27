package com.daw.crudapi.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daw.crudapi.service.DatosPruebaService;
import com.daw.crudapi.service.DatosPruebaService.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/datos")
@RequiredArgsConstructor
public class DatosPrueba {

    private final DatosPruebaService datosPruebaService;

    // GET http://localhost:8080/datos/clientes?cantidad=10
    @GetMapping("/clientes")
    public ResumenGeneracion generarClientes(@RequestParam(defaultValue = "5") int cantidad) {
        return datosPruebaService.generarClientes(cantidad);
    }

    // GET http://localhost:8080/datos/pedidos?cantidad=20
    @GetMapping("/pedidos")
    public ResumenGeneracion generarPedidos(@RequestParam(defaultValue = "10") int cantidad) {
        return datosPruebaService.generarPedidos(cantidad);
    }
}