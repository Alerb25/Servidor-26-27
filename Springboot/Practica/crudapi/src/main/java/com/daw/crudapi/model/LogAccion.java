package com.daw.crudapi.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Registro de acciones (crear / modificar / eliminar). */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogAccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    private String accion; // CREAR, MODIFICAR, ELIMINAR

    @Column(nullable = false)
    private String entidad; // Paciente, Cita

    private Long idEntidad;

    @Column(length = 500)
    private String detalle;
}
