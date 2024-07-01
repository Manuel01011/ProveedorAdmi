package com.example.proyectoprogra4.logic;

import com.example.proyectoprogra4.data.Proveedor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private com.example.proyectoprogra4.data.Proveedor.productoRepository productoRepository;
    @Autowired
    private com.example.proyectoprogra4.data.Proveedor.facturaRepository facturaRepository;

    private List<Proveedor> proveedoresNoAceptados = new ArrayList<>();
    private List<Proveedor> listaDesabilitados = new ArrayList<>();

    @Override
    public List<Proveedor> listaProveedores() {
        List<Proveedor> proveedores = proveedorRepository.findAll();
        // Filtrar los proveedores que no están en la lista de deshabilitados
        List<Proveedor> proveedoresActivos = proveedores.stream()
                .filter(proveedor -> !listaDesabilitados.contains(proveedor))
                .collect(Collectors.toList());

        return proveedoresActivos;
    }

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
    public void guardarProveedor(Proveedor proveedor) throws Exception {
        List<Proveedor> e = proveedoresNoAceptados.stream().filter(h -> h.getCedula().equals(proveedor.getCedula()))
                .toList();
        if(proveedorById(proveedor.getCedula()) && e.isEmpty()) {
            proveedoresNoAceptados.add(proveedor);
        }
        else {
            throw new Exception("El proveedor con cedula " + proveedor.getCedula() + " ya existe.");
        }
    }

    @Override
    public boolean proveedorById(String cedula) {
        Optional<Proveedor> optionalProveedor = proveedorRepository.findById(cedula);
        return optionalProveedor.isEmpty();
    }

    @Override
    public Proveedor userById(String cedula) {
        Optional<Proveedor> proveedor1 = listaDesabilitados.stream()
                .filter(h->h.getCedula().equals(cedula)).findFirst();


        if(proveedor1.isEmpty()){
            Optional<Proveedor> optionalProveedor = proveedorRepository.findById(cedula);
            Proveedor proveedor = optionalProveedor.orElseThrow(() -> new RuntimeException("Not Found"));
            return proveedor;
        }
        return null;
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

    @Override
    public Factura buscarFactura(String id) {
        Optional<Factura> optionalFactura = facturaRepository.findById(id);
        Factura factura = optionalFactura.orElseThrow(() -> new RuntimeException("Not Found"));
        return factura;
    }

    @Override
    public List<Proveedor> getProveedoresNoAceptados() {
        return proveedoresNoAceptados;
    }

    @Override
    public void aceptar(String cedula) {
        Optional<Proveedor> proveedor = proveedoresNoAceptados.stream().filter(h->h.getCedula().equals(cedula)).findFirst();
        Proveedor proveedor1 = proveedor.orElseThrow(() -> new RuntimeException("Not Found"));
        proveedorRepository.save(proveedor1);
        proveedoresNoAceptados.removeIf(h -> h.getCedula().equals(cedula));
    }

    @Override
    public void hailtar(String cedula) {
        Optional<Proveedor> proveedor1 = listaDesabilitados.stream().filter(h->h.getCedula().equals(cedula)).findFirst();
        proveedoresNoAceptados.remove(proveedor1);
    }

    @Override
    public void desabilitar(String cedula) {
        Optional<Proveedor> proveedor = proveedorRepository.findById(cedula);
        Proveedor proveedor1 = proveedor.orElseThrow(() -> new RuntimeException("Not Found"));
        listaDesabilitados.add(proveedor1);
    }

    @Override
    public void rechazar(String cedula) {
        proveedoresNoAceptados.removeIf(h -> h.getCedula().equals(cedula));
    }

    @Override
    public List<Proveedor> listaNohabilitados() {
        return listaDesabilitados;
    }


}
