package com.daw.crudapi.model;

import java.sql.Date;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Pedido {
    @Id // hace que sea clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Para que sea automatico
    private long id;

    @Column(nullable = false)
    private LocalDateTime fechaPedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado = EstadoPedido.CONFIRMADO;

    //relacion pedido-dir
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "direccion_id", nullable = false)
    private Direccion direccion;

    //relacion pedido-cl
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;



}
