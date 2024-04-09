package com.example.proyectoprogra4.data.Proveedor;

import com.example.proyectoprogra4.logic.Cliente;
import com.example.proyectoprogra4.logic.Factura;
import com.example.proyectoprogra4.logic.Producto;
import com.example.proyectoprogra4.logic.Proveedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProveedorImplementation implements ProveedorService {
    @Autowired
    private ProveedorRepository proveedorRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private productoRepository productoRepository;
    @Autowired
    private facturaRepository facturaRepository;

    @Override
    public List<Proveedor> listaProveedores() { return proveedorRepository.findAll(); }

    @Override
    public List<Cliente> listaClientes(String id) {
        List<Cliente> lisClientes = clienteRepository.findAll();
        return lisClientes.stream()
                .filter(cliente -> cliente.getProveedor().equals(id))
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> listaProductos(String id) {
        List<Producto> lisProductos = productoRepository.findAll();
        return lisProductos.stream()
                .filter(producto -> producto.getProveedorByProveedor().getCedula().equals(id))
                .collect(Collectors.toList());
    }

    @Override
    public List<Factura> listaFactura(String id) {
        List<Factura> lisFactura = facturaRepository.findAll();
        return lisFactura.stream()
                .filter(producto -> producto.getProveedorByProveedor().getCedula().equals(id))
                .collect(Collectors.toList());
    }

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

    @Override
    public void saveCliente(Cliente cliente) {
        clienteRepository.save(cliente);
    }

    @Override
    public void saveProducto(Producto producto) {
        productoRepository.save(producto);
    }

    @Override
    public void saveFacturas(Factura factura) {
        facturaRepository.save(factura);
    }

    @Override
    public Cliente buscarCliente(String id) {
        Optional<Cliente> optionalCliente = clienteRepository.findById(id);
        Cliente cliente = optionalCliente.orElseThrow(() -> new RuntimeException("Not Found"));
        return cliente;
    }

    @Override
    public Producto buscarProducto(String id) {
        Optional<Producto> optionalProducto = productoRepository.findById(id);
        Producto producto = optionalProducto.orElseThrow(() -> new RuntimeException("Not Found"));
        return producto;
    }

}
