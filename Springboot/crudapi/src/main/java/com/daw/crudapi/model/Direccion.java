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

public class Direccion {

    @Id // hace que sea clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Para que sea automatico
    private long id;

    @Column(nullable = false)
    private String calle;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String codigoPostal;

    // Una direccion tiene muchos clientes
    // Usamos JoinColumn para especificar el nombre de la fk, apunta automaticamente
    // al
    // id de la tabla cliente porque usamos la clase Cliente
    // Ponemos nullable false ya que no puede haber direcciones sin clientes
    @ManyToOne
    @JoinColumn(name = "clinte_id", nullable = false) // nombre de la foreing key
    private Cliente cliente; // en vez de foreing key se pone cliente

    @OneToMany(mappedBy = "cliente", fetch = FetchType.EAGER)
    private List<Pedido> pedidos;

     public void addPedido(Pedido pedido){
        //añadimos la direccion a la lista de direcciones del cliente
        pedidos.add(pedido);
        //Le decimos que la direccion pertenece a este cliente(si, da error)
        pedido.setDireccion(this);
    }
}
