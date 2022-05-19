package proyecto.tareas.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import proyecto.tareas.Excepciones.MiExcepcion;
import proyecto.tareas.entidades.Empresa;
import proyecto.tareas.entidades.Tarea;
import proyecto.tareas.entidades.Usuario;
import proyecto.tareas.servicios.EmpresaServicio;
import proyecto.tareas.servicios.UsuarioServicio;
import java.util.List;

@Controller
@RequestMapping("/empresa")
public class EmpresaControlador {

    @Autowired
    EmpresaServicio empresaServicio;

    @Autowired
    UsuarioServicio usuarioServicio;

    //------------ Inyecta en el Html los usuarios de la empresa correspondiente al id de usuario recibido ---------------------

        @PreAuthorize("hasRole('LIDER')")
        @GetMapping("/administrar/{id}")
        public String administrarEmpresa(ModelMap modelo, @PathVariable Long id){
            Usuario usuario = usuarioServicio.obtenerUsuarioPorId(id);

            modelo.put("usuarios",usuarioServicio.obtenerEmpleadosEmpresa(id));
        return "administrarEmpresa.html";
        }

    // ----------------- Modifica datos de la empresa correspondiente ------------------

        @PreAuthorize("hasRole('LIDER')")
        @PostMapping("/modificarEmpresa")
        public String modificarEmpresa(ModelMap modelo, @RequestParam Long id,@RequestParam Long idSesion, @RequestParam String nombreEmpr, @RequestParam String rubroEmpr,
                                       @RequestParam String actividadEmpr, @RequestParam MultipartFile archivoUsuario){
            try {
                empresaServicio.modificarEmpresa(id,nombreEmpr,rubroEmpr,actividadEmpr,archivoUsuario);
            } catch (MiExcepcion ex){
                modelo.put("error",ex.getMessage());
            }
            modelo.put("usuarios", usuarioServicio.obtenerEmpleadosEmpresa(idSesion));
            return "administrarEmpresa.html";
        }
}
