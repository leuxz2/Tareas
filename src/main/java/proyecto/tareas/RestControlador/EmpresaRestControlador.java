package proyecto.tareas.RestControlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import proyecto.tareas.entidades.Empresa;
import proyecto.tareas.servicios.EmpresaServicio;
import proyecto.tareas.servicios.UsuarioServicio;
import java.util.List;

@RestController
@RequestMapping("/empresaRest")
public class EmpresaRestControlador { // Retorna respuestas en formato Json referente a la Clase Empresa

    @Autowired
    EmpresaServicio empresaServicio;

    @Autowired
    UsuarioServicio usuarioServicio;

    //----------------- Retorna, en formato Json, la empresa correspondiente al Id de empresa ingresado. ---------------------------


    @GetMapping("/{idEmpr}")
    public Empresa cantidadUsuarios (@PathVariable Long idEmpr){
        try {
            return empresaServicio.obtenerEmpresaPorId(idEmpr);
        } catch (Exception ex ){
            System.out.println(ex.getMessage());
            return null;
        }
    }

}

