package com.daw.crudapi.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor

public class Paciente {

    @Id // hace que sea clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Para que sea automatico
    private long id;

    @Column(nullable = false)
    private String nombre_completo;

    @Column(nullable = false)
    private Date fecha_nacimiento;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String grupo_sanguineo;

    // En mappedBy va el nombre del campo de la otra entidad que sirve para mapear
    // esta
    @OneToMany(mappedBy = "paciente", fetch = FetchType.EAGER)
    private List<Paciente> pacientes;

    public void addPaciente(Paciente paciente) {
        // añadimos la Paciente a la lista de Pacientees del cliente
        pacientes.add(paciente);
        // Le decimos que la Paciente pertenece a este cliente(si, da error)
        paciente.setCliente(this);
    }

}
