package com.daw.crudapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daw.crudapi.repository.ClienteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.daw.crudapi.repository.ClienteRepository;
import com.daw.crudapi.model.Cliente;

@RestController
@RequestMapping("/crudapi") 

public class CrudApiController{
    @Autowired 
    ClienteRepository repoCli;

    @GetMapping("path")
    public String index() {
        
        Cliente cliente = new Cliente( 0, "Pedro", "Jimenez", "pepe@gmail.com", "123456789",  null, null);

        
        Cliente clienteGuardado = repoCli.save(cliente);

        clienteGuardado.setApellidos("Gonzalez Saveedra");
        repoCli.save(cliente);

         return clienteGuardado.toString();



    }
    

}