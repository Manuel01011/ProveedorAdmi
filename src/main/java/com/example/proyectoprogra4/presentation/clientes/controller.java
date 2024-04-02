package com.example.proyectoprogra4.presentation.clientes;
import com.example.proyectoprogra4.modelos.Modelo;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller("clientes")
public class controller {

    @GetMapping("/")
    public String index(Model model){
        //por acá deberia estar a la capa de logica que traiga los datos por ejemplo de las facturas
        Modelo modelo = new Modelo("Sistema Web Factura Electronica");
        model.addAttribute("modelo",modelo);
        return "index";
    }





    @GetMapping("/about")
    public String about(Model model){
        Modelo modelo = new Modelo("Esta es la pagina de informacion de la Hacienda");
        model.addAttribute("modelo",modelo);
        return "about";
    }

    @GetMapping("/footer")
    public String footer(Model model){
        Modelo modelo = new Modelo("Creado por:");
        model.addAttribute("modelo",modelo);
        return "footer";
    }



}


