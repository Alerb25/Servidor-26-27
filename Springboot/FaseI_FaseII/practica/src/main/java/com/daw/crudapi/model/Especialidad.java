package com.daw.crudapi.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Especialidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_especialidad;
   
    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String desc;

   

    @ToString.Exclude
    @OneToMany(mappedBy = "especialidad", fetch = FetchType.LAZY)
    private List<Cita> medicos = new ArrayList<>();

    /**
     * Funcion para añadir correctamente un pedido a este cliente
     * 
     * @param medico
     */
    public void addMedico(Medico medico) {
        
        medico.add(medico);
        
        medico.setEspecialidad(this);
    }

}