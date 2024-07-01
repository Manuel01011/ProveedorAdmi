package com.example.proyectoprogra4.logic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Factura {
    @Id
    @Column(name = "codigo")
    private String codigo;
    @Basic
    @Column(name = "fecha")
    private String fecha;
    @Basic
    @Column(name = "precio")
    private BigDecimal precio;

    @ManyToOne
    @JoinColumn(name = "cliente", referencedColumnName = "identificacion", nullable = false)
    private Cliente clienteByCliente;

    @ManyToOne
    @JoinColumn(name = "producto", referencedColumnName = "codigo", nullable = false)
    private Producto productoByProducto;

    @ManyToOne
    @JoinColumn(name = "proveedor", referencedColumnName = "cedula", nullable = false)
    @JsonIgnoreProperties("facturasByCedula")
    private Proveedor proveedorByProveedor;
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Factura factura = (Factura) o;

        if (codigo != null ? !codigo.equals(factura.codigo) : factura.codigo != null) return false;
        if (fecha != null ? !fecha.equals(factura.fecha) : factura.fecha != null) return false;
        if (precio != null ? !precio.equals(factura.precio) : factura.precio != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = codigo != null ? codigo.hashCode() : 0;
        result = 31 * result + (fecha != null ? fecha.hashCode() : 0);
        result = 31 * result + (precio != null ? precio.hashCode() : 0);
        return result;
    }

    public Cliente getClienteByCliente() {
        return clienteByCliente;
    }

    public void setClienteByCliente(Cliente clienteByCliente) {
        this.clienteByCliente = clienteByCliente;
    }

    public Producto getProductoByProducto() {
        return productoByProducto;
    }

    public void setProductoByProducto(Producto productoByProducto) {
        this.productoByProducto = productoByProducto;
    }

    public Proveedor getProveedorByProveedor() {
        return proveedorByProveedor;
    }

    public void setProveedorByProveedor(Proveedor proveedorByProveedor) {
        this.proveedorByProveedor = proveedorByProveedor;
    }
}
