package com.daw.crudapi.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daw.crudapi.model.Cliente;
import com.daw.crudapi.model.Direccion;
import com.daw.crudapi.model.EstadoPedido;
import com.daw.crudapi.model.Pedido;
import com.daw.crudapi.repository.ClienteRepository;
import com.daw.crudapi.repository.DireccionRepository;
import com.daw.crudapi.repository.PedidoRepository;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;

@Service
@RequiredArgsConstructor
public class DatosPruebaService {

    // Resumen de lo que se ha insertado, para devolverlo como JSON
    public record ResumenGeneracion(int clientes, int direcciones, int pedidos) {
    }

    private final ClienteRepository clienteRepository;
    private final DireccionRepository direccionRepository;
    private final PedidoRepository pedidoRepository;

    private final Faker faker = new Faker(Locale.forLanguageTag("es-ES"));

    /**
     * Crea clientes nuevos, cada uno con 1-3 direcciones y 0-5 pedidos
     */
    @Transactional
    public ResumenGeneracion generarClientes(int cantidad) {
        int totalDirecciones = 0;
        int totalPedidos = 0;

        for (int i = 0; i < cantidad; i++) {
            Cliente cliente = crearCliente();

            int numDirecciones = faker.number().numberBetween(1, 4);
            for (int d = 0; d < numDirecciones; d++) {
                crearDireccion(cliente);
            }

            int numPedidos = faker.number().numberBetween(0, 6);
            for (int p = 0; p < numPedidos; p++) {
                crearPedido(cliente);
            }

            totalDirecciones += numDirecciones;
            totalPedidos += numPedidos;
        }
        return new ResumenGeneracion(cantidad, totalDirecciones, totalPedidos);
    }

    /**
     * Añade pedidos nuevos a clientes que ya existen, elegidos al azar
     */
    @Transactional
    public ResumenGeneracion generarPedidos(int cantidad) {
        List<Cliente> clientes = clienteRepository.findAll();
        if (clientes.isEmpty()) {
            return new ResumenGeneracion(0, 0, 0);
        }

        for (int i = 0; i < cantidad; i++) {
            Cliente cliente = clientes.get(faker.number().numberBetween(0, clientes.size()));
            crearPedido(cliente);
        }
        return new ResumenGeneracion(0, 0, cantidad);
    }

    private Cliente crearCliente() {
        Cliente cliente = new Cliente();
        cliente.setNombre(faker.name().firstName());
        cliente.setApellidos(faker.name().lastName() + " " + faker.name().lastName());
        // Fragmento de UUID para que el email no se repita aunque llames varias veces
        String sufijo = UUID.randomUUID().toString().substring(0, 8);
        cliente.setMail("cliente." + sufijo + "@" + faker.internet().domainName());
        cliente.setTelf(faker.phoneNumber().cellPhone());
        return clienteRepository.save(cliente);
    }

    private Direccion crearDireccion(Cliente cliente) {
        Direccion direccion = new Direccion();
        direccion.setCalle(faker.address().streetName());
        direccion.setNumero(faker.address().buildingNumber());
        direccion.setCiudad(faker.address().city());
        direccion.setCodigoPostal(faker.address().zipCode());
        cliente.addDireccion(direccion);
        return direccionRepository.save(direccion);
    }

    private void crearPedido(Cliente cliente) {
        // Si el cliente no tiene direcciones, le creamos una
        if (cliente.getDirecciones().isEmpty()) {
            crearDireccion(cliente);
        }
        List<Direccion> direcciones = cliente.getDirecciones();
        Direccion direccion = direcciones.get(faker.number().numberBetween(0, direcciones.size()));

        Pedido pedido = new Pedido();
        pedido.setFechaPedido(LocalDateTime.now()
                .minusDays(faker.number().numberBetween(0, 365))
                .minusMinutes(faker.number().numberBetween(0, 1440)));
        pedido.setEstado(faker.options().option(EstadoPedido.class));
        pedido.setDireccion(direccion);
        cliente.addPedido(pedido);
        pedidoRepository.save(pedido);
    }
}