package edu.pucmm.eict.ormjpa.entidades;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Comentario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 1000)
    private String texto;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Producto producto;

    public Comentario() {
    }

    public Comentario(String texto, Usuario usuario, Producto producto) {
        this.texto = texto;
        this.usuario = usuario;
        this.producto = producto;
        this.fecha = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
