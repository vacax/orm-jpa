package edu.pucmm.eict.ormjpa.entidades;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Entidad para presentar una clase con más de un PK compuesto.
 */
@Entity
public class ClienteFactura implements Serializable {

    @EmbeddedId
    private ClienteFacturaId clienteFacturaId;
    String campo1;
    String campo2;

    public ClienteFacturaId getClienteFacturaId() {
        return clienteFacturaId;
    }

    public void setClienteFacturaId(ClienteFacturaId clienteFacturaId) {
        this.clienteFacturaId = clienteFacturaId;
    }

    public String getCampo1() {
        return campo1;
    }

    public void setCampo1(String campo1) {
        this.campo1 = campo1;
    }

    public String getCampo2() {
        return campo2;
    }

    public void setCampo2(String campo2) {
        this.campo2 = campo2;
    }
}

/**
 * Clase para manejar los PK compuestos.
 */
@Embeddable
class ClienteFacturaId implements Serializable {

    @OneToOne
    private Cliente cliente;
    @OneToOne
    private Factura factura;

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }
}
