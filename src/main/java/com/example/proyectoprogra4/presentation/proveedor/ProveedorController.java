package com.example.proyectoprogra4.presentation.proveedor;

import com.example.proyectoprogra4.data.Proveedor.ProveedorRepository;
import com.example.proyectoprogra4.data.Proveedor.ProveedorService;
import com.example.proyectoprogra4.logic.Proveedor;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class ProveedorController {
    @Autowired
    private ProveedorService proveedorService;

    @GetMapping("/presentation/clientes/View")
    public String listaProveedores(Model model){
        model.addAttribute("proveedores",proveedorService.listaProveedores());
        return "/presentation/clientes/View";
    }

    @GetMapping("/registrarProveedor")
    public String crearProveedor(Model model){
        Proveedor proveedor = new Proveedor();
        model.addAttribute("proveedor",proveedor);
        return "registrarProveedor";
    }

    @PostMapping("/registrarProveedor")
    public String guardarProveedor(@ModelAttribute("proveedor")Proveedor proveedorRR){
        proveedorService.guardarProveedor(proveedorRR);
        return "redirect:/presentation/clientes/View";
    }

    @GetMapping("/login")
    public String getLoginForm(Model model){
        Proveedor proveedor = new Proveedor();
        model.addAttribute("proveedor",proveedor);
        return "login";
    }

    @PostMapping("/login")
    public String loginValidation(@ModelAttribute("proveedor") Proveedor proveedor, Model model) throws Exception {
        Proveedor existingProveedor = proveedorService.userById(proveedor.getCedula());
        if(existingProveedor == null || !existingProveedor.getContrasena().equals(proveedor.getContrasena())){
            model.addAttribute("error", "Credenciales incorrectas, por favor intente de nuevo.");
            return "login";
        }
        return "redirect:/perfilProveedor";

    /*if (proveedorService.buscar(proveedor.getCedula())) {
            // Si el proveedor es válido, redirigir a la página de perfil
            return "redirect:/perfilProveedor";
        } else {
            // Si el proveedor no es válido, agregar un mensaje de error al modelo y volver a la página de login
            model.addAttribute("error", "Credenciales incorrectas, por favor intente de nuevo.");
            return "login";
        }*/
    }
/*
    @PostMapping("/login")
    public String loginValidation(@ModelAttribute("proveedor")Proveedor proveedor){
        proveedorService.buscar(proveedor.getCedula());
        return "redirect:/perfilProveedor";
    }*/
}
