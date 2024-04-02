package com.example.proyectoprogra4.data.Proveedor;

import com.example.proyectoprogra4.logic.Proveedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorImplementation implements ProveedorService {
    @Autowired
    private ProveedorRepository proveedorRepository;
    @Override
    public List<Proveedor> listaProveedores() { return proveedorRepository.findAll(); }

    @Override
    public Proveedor guardarProveedor(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Override
    public Proveedor userById(String cedula) {
        Optional<Proveedor> optionalProveedor = proveedorRepository.findById(cedula);
        Proveedor proveedor = optionalProveedor.orElseThrow(() -> new RuntimeException("Not Found"));
        return proveedor;
    }

    @Override
    public Proveedor actualizarProveedor(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Override
    public void eliminarProveedor(String cedula) {
        proveedorRepository.deleteById(cedula);
    }

}
