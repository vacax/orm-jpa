package edu.pucmm.eict.ormjpa.entidades;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class Imagen implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mimeType;
    @Lob
    private String imagenBase64;

    @ManyToOne
    private Producto producto;

    public Imagen() {
    }

    public Imagen(String mimeType, String imagenBase64) {
        this.mimeType = mimeType;
        this.imagenBase64 = imagenBase64;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getImagenBase64() {
        return imagenBase64;
    }

    public void setImagenBase64(String imagenBase64) {
        this.imagenBase64 = imagenBase64;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
