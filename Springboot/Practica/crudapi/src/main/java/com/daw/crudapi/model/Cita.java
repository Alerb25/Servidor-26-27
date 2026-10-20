package com.daw.crudapi.model;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity // marca como entidad
@Getter
@Setter
@ToString
@AllArgsConstructor

public class Cita {

    @Id // hace que sea clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Para que sea automatico
    private long id_cita;

    @Column(nullable = false)
    private String motivo_cita;

    @Column(nullable = false)
    private String estado_cita;

    @Column(nullable = false, unique = true)
    private String observaciones;

    @Column(nullable = false)
    private int duracion_minutos;

    @Column()
    private float costo_estimado;

    @Column(nullable = false)
    private Date fecha;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false) // nombre de la foreing key
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "habitacion_id", nullable = false) // nombre de la foreing key
    private Habitacion habitacion;

}
