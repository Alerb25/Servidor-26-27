package com.daw.crudapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity

public class Direccion {

    @Id // hace que sea clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Para que sea automatico
    private long id;

    @Column(nullable = false)
    private String calle;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String codigoPostal;

    //Una direccion tiene  muchos clientes 
    //Usamos JoinColumn para especificar el nombre de la fk, apunta automaticamente al
    // id de  la tabla cliente porque usamos la clase Cliente
    //Ponemos nullable false ya que no puede haber direcciones sin clientes
    @ManyToOne
    @JoinColumn(name = "clinte_id", nullable = false) // nombre de la foreing key
    private Cliente cliente; // en vez de foreing key se pone cliente

}
