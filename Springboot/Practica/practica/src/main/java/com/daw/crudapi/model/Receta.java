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
public class Receta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_receta;
   
    @Column(nullable = false)
    private String cod_receta;

    @Column(nullable = false)
    private Date fecha_emision;

   @Column(nullable = false)
    private int duracion_dias;

    @Column(nullable = false)
    private String instrucciones;

    @ToString.Exclude
    @OneToMany(mappedBy = "receta", fetch = FetchType.LAZY)
    private List<Cita> citas = new ArrayList<>();

    /**
     * Funcion para añadir correctamente un pedido a este cliente
     * 
     * @param cita
     */
    public void addCita(Cita cita) {
        
        cita.add(cita);
        
        cita.setReceta(this);
    }

}