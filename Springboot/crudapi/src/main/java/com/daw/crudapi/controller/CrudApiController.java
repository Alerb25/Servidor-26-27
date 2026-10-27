package com.daw.crudapi.controller;

import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.daw.crudapi.model.Cliente;
import com.daw.crudapi.model.Direccion;
import com.daw.crudapi.repository.ClienteRepository;
import com.daw.crudapi.repository.DireccionRepository;
import com.daw.crudapi.repository.PedidoRepository;

import net.datafaker.Faker;

@RestController // Las respuestas se devuelven como JSON
@RequestMapping("/crudapi") // Prefijo común de todas las URL
public class CrudApiController {

    @Autowired
    ClienteRepository repoCli;
    @Autowired
    DireccionRepository repoDir;
    @Autowired
    PedidoRepository repoPed;

    private final Faker faker = new Faker(Locale.forLanguageTag("es-ES"));

    @GetMapping
    public String index() {
        Cliente cliente = new Cliente();
        cliente.setNombre(faker.name().firstName());
        cliente.setApellidos(faker.name().lastName());
        cliente.setMail(faker.internet().emailAddress());
        cliente.setTelf(faker.phoneNumber().cellPhone());

        // Creamos una direccion
        Direccion direccion = new Direccion(null, faker.address().streetAddress(),
                Integer.toString(faker.number().numberBetween(1, 100)), faker.address().city(),
                faker.address().zipCode(), null);

        cliente.addDireccion(direccion);

        // Guardamos el cliente en bd con save
        // Esto hace un insert en bd
        // Devuelve una entity que representa al registro guardado en bd
        Cliente clienteGuardado = repoCli.save(cliente);
        // Guardamos tambien la dirección
        repoDir.save(direccion);

        // Si modificamos los datos del cliente y hacemos save otra vez
        // En este caso hace un update ya que la entity esta mapeada en bd
        // clienteGuardado.setApellidos("Gonsdfszalez Savedra");
        // repoCli.save(clienteGuardado);

        // Carga de entidades por id
        cliente = repoCli.findById(1L).orElse(null);

        return clienteGuardado.toString();
    }

}