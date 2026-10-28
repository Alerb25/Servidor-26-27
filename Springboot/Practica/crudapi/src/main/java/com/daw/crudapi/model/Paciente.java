package com.daw.crudapi.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** Entidad PRINCIPAL. */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Paciente {

    public static final List<String> GRUPOS = List.of("A+", "A-", "B+", "B-", "AB+", "AB-", "0+", "0-");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPaciente;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombreCompleto;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser pasada")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @NotBlank(message = "La dirección es obligatoria")
    @Column(nullable = false)
    private String direccion;

    @NotBlank(message = "El grupo sanguíneo es obligatorio")
    @Column(nullable = false)
    private String grupoSanguineo;

    // En mappedBy va el nombre del campo de Cita que es la clave foránea
    @ToString.Exclude
    @OneToMany(mappedBy = "paciente", fetch = FetchType.LAZY)
    private List<Cita> citas = new ArrayList<>();

    public void addCita(Cita cita) {
        citas.add(cita);
        cita.setPaciente(this);
    }

    public void removeCita(Cita cita) {
        citas.remove(cita);
        cita.setPaciente(null);
    }
}
