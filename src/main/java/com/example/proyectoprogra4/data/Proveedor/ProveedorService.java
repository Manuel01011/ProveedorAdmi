package com.example.proyectoprogra4.data.Proveedor;

import com.example.proyectoprogra4.logic.Cliente;
import com.example.proyectoprogra4.logic.Factura;
import com.example.proyectoprogra4.logic.Producto;
import com.example.proyectoprogra4.logic.Proveedor;

import java.util.List;

public interface ProveedorService  {
    public List<Proveedor> listaProveedores();
    public List<Cliente> listaClientes(String id);
    public List<Producto> listaProductos(String id);
    public List<Factura> listaFactura(String id);
    public void guardarProveedor(Proveedor proveedor) throws Exception;
    public Proveedor userById(String cedula);
    public Proveedor actualizarProveedor(Proveedor proveedor);
    public void eliminarProveedor(String cedula);
    public void saveCliente(Cliente cliente);
    public void saveProducto(Producto producto);
    public void saveFacturas(Factura factura);
    public Cliente buscarCliente(String id);
    public Producto buscarProducto(String id);
    public Factura buscarFactura(String id);
    List<Proveedor> getProveedoresNoAceptados();
    void aceptar(String cedula);
    void hailtar(String cedula);
    void desabilitar(String cedula);
    public boolean proveedorById(String cedula);
    public void rechazar(String cedula);
    List<Proveedor> listaNohabilitados();
}
