package com.example.proyectoprogra4.presentation.proveedor;
import com.example.proyectoprogra4.data.Proveedor.ProveedorService;
import com.example.proyectoprogra4.logic.Cliente;
import com.example.proyectoprogra4.logic.Factura;
import com.example.proyectoprogra4.logic.Producto;
import com.example.proyectoprogra4.logic.Proveedor;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;


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
    public String guardarProveedor(@ModelAttribute("proveedor") Proveedor proveedorRR, Model model) {
        try {
            proveedorService.guardarProveedor(proveedorRR);
            return "redirect:login";
        } catch (Exception e) {
            model.addAttribute("error1", e.getMessage());
            return "registrarProveedor";
        }
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

    @GetMapping("/administracion")
    public String admin(Model model){
        model.addAttribute("proveedores",proveedorService.listaProveedores());
        model.addAttribute("proveedoresDesabilitados",proveedorService.listaNohabilitados());
        model.addAttribute("admin", "Modulo de Administración");
        return "administracion";
    }

    @GetMapping("/adminSolicitudes")
    public String adminSolicitudes(Model model){
        model.addAttribute("proveedoresPendientes",proveedorService.getProveedoresNoAceptados());
        return "adminSolicitudes";
    }

    @PostMapping("/aceptar")
    public String aceptar(@RequestParam("proveedorId") String proveedorId, Model model){
        proveedorService.aceptar(proveedorId);
        model.addAttribute("proveedores",proveedorService.listaProveedores());
        return "redirect:adminSolicitudes";
    }

    @PostMapping("/rechazar")
    public String rechazar(@RequestParam("proveedorId")String proveedorId, Model model){
        proveedorService.rechazar(proveedorId);
        model.addAttribute("proveedores",proveedorService.listaProveedores());
        return "redirect:adminSolicitudes";
    }

    @PostMapping("/habilitar")
    public String habilitar(@RequestParam("proveedorId") String proveedorId, Model model){
        proveedorService.hailtar(proveedorId);
        return "redirect:administracion";
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

    @PostMapping("/generar_pdf")
    public ResponseEntity<byte[]> generarPDF(@RequestParam("facturaCodigo") String facturaCodigo) {
        // Buscar la factura en función de su código
        Factura factura = proveedorService.buscarFactura(facturaCodigo);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(outputStream));
            com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdfDocument);

            // Agrega el contenido de la factura al PDF
            document.add(new com.itextpdf.layout.element.Paragraph("Código: " + factura.getCodigo()));
            document.add(new com.itextpdf.layout.element.Paragraph("Fecha: " + factura.getFecha()));
            document.add(new com.itextpdf.layout.element.Paragraph("Precio: " + factura.getPrecio()));
            document.add(new com.itextpdf.layout.element.Paragraph("Cliente: " + factura.getClienteByCliente().getNombre()));
            document.add(new com.itextpdf.layout.element.Paragraph("Producto: " + factura.getProductoByProducto().getNombre()));
            document.add(new com.itextpdf.layout.element.Paragraph("Proveedor: " + factura.getProveedorByProveedor().getNombre()));
            document.add(new com.itextpdf.layout.element.Paragraph("Cedula de Proveedor: " + factura.getProveedorByProveedor().getCedula()));

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Construir el ResponseEntity con el arreglo de bytes y los encabezados adecuados
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "factura.pdf");

        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }

    @PostMapping("/generar_xml")
    public ResponseEntity<byte[]> generarXML(@RequestParam("facturaCodigo") String facturaCodigo) {
        // Buscar la factura en función de su código
        Factura factura = proveedorService.buscarFactura(facturaCodigo);

        // Crear el contenido XML de la factura
        String xmlContent = "<factura>\n" +
                "    <codigo>" + factura.getCodigo() + "</codigo>\n" +
                "    <fecha>" + factura.getFecha() + "</fecha>\n" +
                "    <precio>" + factura.getPrecio() + "</precio>\n" +
                "    <cliente>" + factura.getClienteByCliente().getNombre() + "</cliente>\n" +
                "    <producto>" + factura.getProductoByProducto().getNombre() + "</producto>\n" +
                "</factura>";

        // Convertir el contenido XML en un arreglo de bytes
        byte[] xmlBytes = xmlContent.getBytes(StandardCharsets.UTF_8);

        // Construir el ResponseEntity con el arreglo de bytes y los encabezados adecuados
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setContentDispositionFormData("filename", "factura.xml");

        return new ResponseEntity<>(xmlBytes, headers, HttpStatus.OK);
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
        Producto producto = proveedorService.buscarProducto(productoCodigo);

        // Establecer el proveedor, cliente y producto en la factura
        factura.setProveedorByProveedor(proveedor);
        factura.setClienteByCliente(cliente);
        factura.setProductoByProducto(producto);

        // Guardar la factura
        proveedorService.saveFacturas(factura);

        return "redirect:perfilProveedor";
    }

    @PostMapping("/Desabilitar")
    public String desbilitar(@RequestParam("proveedorId") String proveedorId, Model model){
        proveedorService.desabilitar(proveedorId);
        model.addAttribute("proveedores",proveedorService.listaProveedores());
        return "redirect:administracion";
    }



}
