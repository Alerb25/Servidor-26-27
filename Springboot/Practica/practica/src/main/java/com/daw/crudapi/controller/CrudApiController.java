package com.daw.crudapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/crudapi") 

public class CrudApiController{

    @GetMapping("path")
    public String index() {
        return new String("Página Principal");
    }
    

}