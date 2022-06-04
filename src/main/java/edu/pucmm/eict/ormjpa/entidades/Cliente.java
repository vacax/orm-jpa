package edu.pucmm.eict.ormjpa.entidades;


import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
public class Cliente implements Serializable {

    @Id
    private
    Long id;
    private String nombre;
    @OneToMany(mappedBy = "cliente")
    private Set<Factura> listaFactura;
    @OneToMany
    private Set<Direccion> listaDirecciones;


    public Set<Factura> getListaFactura() {
        return listaFactura;
    }

    public void setListaFactura(Set<Factura> listaFactura) {
        this.listaFactura = listaFactura;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return id.equals(cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
