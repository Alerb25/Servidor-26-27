package com.daw.crudapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity //marca como entidad
@Table (
    indexes = @Index(name="idx_mail", columnList = "mail")
)
public class Cliente {
    
    @Id //hace que sea clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Para que sea automatico
    private long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false, unique = true) 
    private String mail;

    @Column(nullable = false)
    private  String telf;


}
