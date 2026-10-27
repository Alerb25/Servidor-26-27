package com.daw.crudapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.crudapi.model.Cliente;
import com.daw.crudapi.model.Pedido;

public interface PedidoRepository extends JpaRepository<Cliente, Long> {

    void save(Pedido pedido);
    //metodos
   
} 
