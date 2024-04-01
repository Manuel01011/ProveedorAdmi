package com.example.proyectoprogra4.logic;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
public class Proveedor {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "cedula", unique = true)
    private String cedula;
    @Basic
    @Column(name = "nombre")
    private String nombre;
    @Basic
    @Column(name = "correo")
    private String correo;
    @Basic
    @Column(name = "contrasena")
    private String contrasena;
    @OneToMany(mappedBy = "proveedorByProveedor")
    private Collection<Factura> facturasByCedula;
    @OneToMany(mappedBy = "proveedorByProveedor")
    private Collection<Producto> productosByCedula;

    public Proveedor() {
    }

    public Proveedor(String cedula, String nombre, String correo, String contrasena) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proveedor proveedor = (Proveedor) o;
        return Objects.equals(cedula, proveedor.cedula) && Objects.equals(nombre, proveedor.nombre) && Objects.equals(correo, proveedor.correo) && Objects.equals(contrasena, proveedor.contrasena);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cedula, nombre, correo, contrasena);
    }

    public Collection<Factura> getFacturasByCedula() {
        return facturasByCedula;
    }

    public void setFacturasByCedula(Collection<Factura> facturasByCedula) {
        this.facturasByCedula = facturasByCedula;
    }

    public Collection<Producto> getProductosByCedula() {
        return productosByCedula;
    }

    public void setProductosByCedula(Collection<Producto> productosByCedula) {
        this.productosByCedula = productosByCedula;
    }
}
