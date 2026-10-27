package com.daw.crudapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.crudapi.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    //metodos
    List<Cliente> findByMail(String mail);
} 
