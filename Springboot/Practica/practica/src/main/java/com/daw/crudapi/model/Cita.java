package com.daw.crudapi.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** Entidad SECUNDARIA. El paciente es opcional para poder asociar/desasociar. */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Cita {

    public static final List<String> ESTADOS = List.of("Pendiente", "Confirmada", "Completada", "Cancelada");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCita;

    @NotBlank(message = "El motivo es obligatorio")
    @Column(nullable = false)
    private String motivoCita;

    @NotBlank(message = "El estado es obligatorio")
    @Column(nullable = false)
    private String estadoCita;

    @Column(length = 1000)
    private String observaciones;

    @NotNull(message = "La duración es obligatoria")
    @Min(value = 1, message = "La duración mínima es 1 minuto")
    @Column(nullable = false)
    private Integer duracionMinutos;

    @NotNull(message = "El coste es obligatorio")
    @PositiveOrZero(message = "El coste no puede ser negativo")
    @Column(nullable = false)
    private Double costoEstimado;

    @NotNull(message = "La fecha es obligatoria")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(nullable = false)
    private LocalDate fecha;

    // Nullable: una cita puede estar sin paciente (desasociada)
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;
}
