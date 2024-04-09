package com.example.proyectoprogra4.presentation.proveedor;
import com.example.proyectoprogra4.data.Proveedor.ProveedorService;
import com.example.proyectoprogra4.logic.Cliente;
import com.example.proyectoprogra4.logic.Factura;
import com.example.proyectoprogra4.logic.Producto;
import com.example.proyectoprogra4.logic.Proveedor;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

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
    public String guardarProveedor(@ModelAttribute("proveedor") Proveedor proveedorRR){
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
    public String loginValidation(@ModelAttribute("proveedor") Proveedor proveedor, Model model, HttpSession session,
                                  @RequestParam("cedula") String cedula,
                                  @RequestParam("contrasena") String contra) throws Exception {
        if(cedula.equals("admin") && contra.equals("admin")){
            model.addAttribute("admin", "");
            return "redirect:/administracion";
        }
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

        model.addAttribute("clientes",proveedorService.listaClientes(loggedInProveedor.getCedula()));
        model.addAttribute("productos",proveedorService.listaProductos(loggedInProveedor.getCedula()));
        model.addAttribute("facturas",proveedorService.listaFactura(loggedInProveedor.getCedula()));
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

   @GetMapping("/registrarCliente")
   public String formCliente(Model model){
       Cliente cliente = new Cliente();
       model.addAttribute("cliente",cliente);
       return "registrarCliente";
   }
    @PostMapping("/registrarCliente")
    public String saveCliente(@ModelAttribute("cliente")Cliente cliente, HttpSession session, Model model){
        Proveedor proveedor = (Proveedor) session.getAttribute("loggedInProveedor");
        cliente.setProveedor(proveedor.getCedula());
        proveedorService.saveCliente(cliente);
        return "redirect:perfilProveedor";
    }

    @GetMapping("/perfilProveedor/{cedula}")
    public String eliminarProveedor(@PathVariable String cedula){
        proveedorService.eliminarProveedor(cedula);
        return "redirect:login";
    }

    @GetMapping("/registrarProductos")
    public String formProductos(Model model){
        Producto producto = new Producto();
        model.addAttribute("producto",producto);
        return "registrarProductos";
    }
    @PostMapping("/registrarProductos")
    public String saveProducto(@ModelAttribute("producto")Producto producto, HttpSession session, Model model){
        Proveedor proveedor = (Proveedor) session.getAttribute("loggedInProveedor");
        producto.setProveedorByProveedor(proveedor);
        proveedorService.saveProducto(producto);
        return "redirect:perfilProveedor";
    }

    @GetMapping("/registrarFactura")
    public String formFacturas(Model model,HttpSession session){
        Factura factura = new Factura();
        Proveedor loggedInProveedor = (Proveedor) session.getAttribute("loggedInProveedor");
        model.addAttribute("proveedor", loggedInProveedor);
        model.addAttribute("clientes",proveedorService.listaClientes(loggedInProveedor.getCedula()));
        model.addAttribute("productos",proveedorService.listaProductos(loggedInProveedor.getCedula()));
        model.addAttribute("facturas",factura);
        return "registrarFactura";
    }

    @PostMapping("/registrarFactura")
    public String saveFactura(@ModelAttribute("factura") Factura factura,
                              @RequestParam("opciones") String clienteIdentificacion,
                              @RequestParam("opciones2") String productoCodigo,
                              HttpSession session) {
        Proveedor proveedor = (Proveedor) session.getAttribute("loggedInProveedor");

        // Recuperar el cliente y producto basado en las identificaciones y códigos recibidos
        Cliente cliente = proveedorService.buscarCliente(clienteIdentificacion);
        Producto producto =proveedorService.buscarProducto(productoCodigo);

        // Establecer el proveedor, cliente y producto en la factura
        factura.setProveedorByProveedor(proveedor);
        factura.setClienteByCliente(cliente);
        factura.setProductoByProducto(producto);

        // Guardar la factura
        proveedorService.saveFacturas(factura);

        return "redirect:perfilProveedor";
    }


}
