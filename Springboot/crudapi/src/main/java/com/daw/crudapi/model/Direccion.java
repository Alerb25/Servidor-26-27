package com.daw.crudapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String calle;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String codigoPostal;

    // Una direccion tiene muchos clientes
    @ManyToOne
    // usamos JoinColumn para especificar el nombre de la fk, apunta automaticamente
    // al
    // id de la tabla cliente porque usamos la clase Cliente
    // Ponemos nullable false ya que no puede haber direcciones sin clientes
    // asignado
    @ToString.Exclude
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

}