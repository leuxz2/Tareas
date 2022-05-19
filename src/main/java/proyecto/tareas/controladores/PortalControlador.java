
package proyecto.tareas.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import proyecto.tareas.entidades.Tarea;
import proyecto.tareas.entidades.Usuario;
import proyecto.tareas.servicios.TareaServicio;
import proyecto.tareas.servicios.UsuarioServicio;

import java.util.List;

@Controller
public class PortalControlador {

    @Autowired UsuarioServicio usuarioServicio;

    @Autowired
    TareaServicio tareaServicio;

//------------------------ Vista principal con la que se encuentra el usuario antes de loguearse o registrarse -----------------------------

    @GetMapping("/")
    public String index(){
        return "index.html";
    }

//----------------------- Vista que muestra el menu principal, tanto para lider de empresa como para empleado ------------------------------

    @PreAuthorize("hasAnyRole('LIDER','USUARIO')")
    @GetMapping("/inicio")
public String inicio(ModelMap modelo){
//        List<Usuario> usuarios = usuarioServicio.ObtenerUsuarios();
//        modelo.put("tarea", new Tarea());
//        modelo.put("usuarios",usuarios);
return "inicio.html";
}

//------------ Vista para crear Usuario Lider de empresa ------------------------------------------


    @GetMapping("/crearUsuarioLider")
    public String crearUsuario(ModelMap modelo){
        return "crearUsuarioLider.html";
}

//-------------- Vista para crear Usuario empleado de empresa ya creada ---------------------------

    @PreAuthorize("hasRole('LIDER')")
    @GetMapping("/crearUsuario")
public String crearUsuario () {
        return "crearUsuario.html";
}

//-----------------------------------------------------------------------------------






}
