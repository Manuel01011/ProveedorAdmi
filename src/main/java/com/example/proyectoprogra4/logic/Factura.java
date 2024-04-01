package com.example.proyectoprogra4.logic;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Factura {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "codigo")
    private String codigo;
    @Basic
    @Column(name = "fecha")
    private String fecha;
    @Basic
    @Column(name = "precio")
    private String precio;
    @ManyToOne
    @JoinColumn(name = "cliente", referencedColumnName = "identificacion", nullable = false)
    private Cliente clienteByCliente;
    @ManyToOne
    @JoinColumn(name = "producto", referencedColumnName = "codigo", nullable = false)
    private Producto productoByProducto;
    @ManyToOne
    @JoinColumn(name = "proveedor", referencedColumnName = "cedula", nullable = false)
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

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Factura factura = (Factura) o;
        return Objects.equals(codigo, factura.codigo) && Objects.equals(fecha, factura.fecha) && Objects.equals(precio, factura.precio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, fecha, precio);
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
