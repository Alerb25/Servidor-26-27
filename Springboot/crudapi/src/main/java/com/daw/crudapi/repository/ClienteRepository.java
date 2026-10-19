package com.daw.crudapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.crudapi.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    //metodos
    
} 
