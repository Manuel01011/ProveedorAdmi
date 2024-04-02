package com.example.proyectoprogra4.presentation.proveedor;

import com.example.proyectoprogra4.data.Proveedor.ProveedorRepository;
import com.example.proyectoprogra4.data.Proveedor.ProveedorService;
import com.example.proyectoprogra4.logic.Proveedor;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
    public String loginValidation(@ModelAttribute("proveedor") Proveedor proveedor, Model model, HttpSession session) throws Exception {
        Proveedor existingProveedor = proveedorService.userById(proveedor.getCedula());
        if (existingProveedor == null || !existingProveedor.getContrasena().equals(proveedor.getContrasena())) {
            model.addAttribute("error", "Credenciales incorrectas, por favor intente de nuevo.");
            return "login";
        }
        session.setAttribute("loggedInProveedor", existingProveedor);
        return "redirect:/perfilProveedor";
    }

    @GetMapping("/perfilProveedor")
    public String perfilDelProveedor(Model model, HttpSession session){
        Proveedor loggedInProveedor = (Proveedor) session.getAttribute("loggedInProveedor");
        model.addAttribute("proveedor", loggedInProveedor);
        return "perfilProveedor";
    }

    @GetMapping("/editarPerfil")
    public String mostrarFormularioEditar(Model model, HttpSession session){
        Proveedor loggedInProveedor = (Proveedor) session.getAttribute("loggedInProveedor");
        model.addAttribute("proveedor", loggedInProveedor);
        return "editarPerfil";
    }

    @PostMapping("/editarPerfil")
    public String editProveedorSubmit(@ModelAttribute("proveedor") Proveedor editedProveedor, Model model) throws Exception {
        Proveedor existingProveedor = proveedorService.userById(editedProveedor.getCedula());
        existingProveedor.setNombre(editedProveedor.getNombre());
        existingProveedor.setContrasena(editedProveedor.getContrasena());
        existingProveedor.setCorreo(editedProveedor.getCorreo());
        proveedorService.actualizarProveedor(existingProveedor);
        return "redirect:perfilProveedor";
    }

    @GetMapping("/perfilProveedor/{cedula}")
    public String eliminarProveedor(@PathVariable String cedula){
        proveedorService.eliminarProveedor(cedula);
        return "redirect:login";
    }
}
