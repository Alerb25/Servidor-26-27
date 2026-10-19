package com.daw.crudapi.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.daw.crudapi.model.Cliente;
import com.daw.crudapi.model.Direccion;
import com.daw.crudapi.repository.ClienteRepository;
import com.daw.crudapi.repository.DireccionRepository;

@RestController // Las respuestas se devuelven como JSON
@RequestMapping("/crudapi") // Prefijo común de todas las URL
public class CrudApiController {

    @Autowired
    ClienteRepository repoCli;
    @Autowired
    DireccionRepository repoDir;

    @GetMapping
    public String index() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Pepe");
        cliente.setApellidos("Garcia");
        cliente.setEmail("otro@gmadil345.com");
        cliente.setTelefono("345232323");

        // Creamos una direccion
        Direccion direccion = new Direccion(null, "soledad", "23", "Jerez", "23422", null);

        cliente.addDireccion(direccion);

        // Guardamos el cliente en bd con save
        // Esto hace un insert en bd
        // Devuelve una entity que representa al registro guardado en bd
        // Cliente clienteGuardado = repoCli.save(cliente);
        // Guardamos tambien la dirección
        // repoDir.save(direccion);

        // Si modificamos los datos del cliente y hacemos save otra vez
        // En este caso hace un update ya que la entity esta mapeada en bd
        // clienteGuardado.setApellidos("Gonsdfszalez Savedra");
        // repoCli.save(clienteGuardado);

        // Carga de entidades por id
        cliente = repoCli.findById(1L).orElse(null);

        return cliente.toString();
    }

}