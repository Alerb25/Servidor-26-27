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
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_medico;

    @Column(nullable = false)
    private String num_colegiado;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false)
    private String telefono;

    

    @ToString.Exclude
    @OneToMany(mappedBy = "medico", fetch = FetchType.LAZY)
    private List<Cita> citas = new ArrayList<>();

    /**
     * Funcion para añadir correctamente un pedido a este cliente
     * 
     * @param cita
     */
    public void addCita(Cita cita) {
        
        cita.add(cita);
        
        cita.setMedico(this);
    }

}