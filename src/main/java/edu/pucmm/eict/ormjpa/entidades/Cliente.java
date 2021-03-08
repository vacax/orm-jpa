package edu.pucmm.eict.ormjpa.entidades;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.Set;

@Entity
public class Cliente implements Serializable {

    @Id
    private
    Long id;
    private String nombre;

    public Set<Factura> getListaFactura() {
        return listaFactura;
    }

    public void setListaFactura(Set<Factura> listaFactura) {
        this.listaFactura = listaFactura;
    }

    @OneToMany(mappedBy = "cliente")
    private Set<Factura> listaFactura;


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
}
