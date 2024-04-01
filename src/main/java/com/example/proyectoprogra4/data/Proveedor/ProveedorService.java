package com.example.proyectoprogra4.data.Proveedor;

import com.example.proyectoprogra4.logic.Proveedor;

import java.util.List;
import java.util.Optional;

public interface ProveedorService  {
    public List<Proveedor> listaProveedores();
    public Proveedor guardarProveedor(Proveedor proveedor);
    public Proveedor userById(String cedula);
}
