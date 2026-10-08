package com.daw.crudapi.model;

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
@Table(indexes = @Index(name = "idx_mail", columnList = "mail"))
public class Cliente {

    @Id // hace que sea clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Para que sea automatico
    private long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false, unique = true)
    private String mail;

    @Column(nullable = false)
    private String telf;

    // En mappedBy va el nombre del campo de la otra entidad que sirve para mapear
    // esta
    @OneToMany(mappedBy = "cliente", fetch = FetchType.EAGER)
    private List<Direccion> direcciones;

    public void addDireccion(Direccion direccion) {
        // añadimos la direccion a la lista de direcciones del cliente
        direcciones.add(direccion);
        // Le decimos que la direccion pertenece a este cliente(si, da error)
        direccion.setCliente(this);
    }

    @OneToMany(mappedBy = "cliente", fetch = FetchType.EAGER)
    private List<Pedido> pedidos;

    public void addPedido(Pedido pedido) {
        
        pedidos.add(pedido);
        pedido.setCliente(this);
    }

}
