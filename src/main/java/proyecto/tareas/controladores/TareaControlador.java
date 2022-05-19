package proyecto.tareas.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import proyecto.tareas.Excepciones.MiExcepcion;
import proyecto.tareas.entidades.Tarea;
import proyecto.tareas.entidades.Usuario;
import proyecto.tareas.repositorios.UsuarioRepositorio;
import proyecto.tareas.servicios.TareaServicio;
import proyecto.tareas.servicios.UsuarioServicio;
import java.util.Date;
import java.util.List;

@RequestMapping("/tarea")
@Controller
public class TareaControlador { // Controlador encargado de retornar vistas Html (MVC)

    @Autowired
    TareaServicio tareaServicio;

    @Autowired
    UsuarioServicio usuarioServicio;

    //-------------------------------- Crea una Tarea nueva y retorna a la vista de inicio --------------------------------------

    @PreAuthorize("hasRole('LIDER')")
    @PostMapping("/registrarTarea")
    public String crearTarea(ModelMap modelo, @RequestParam Long id, @RequestParam String nombre,@RequestParam String descripcion,
                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFinal){

        try {
            tareaServicio.crearTarea(usuarioServicio.obtenerUsuarioPorId(id), nombre,descripcion,fechaFinal);
            String mensaje = "La tarea para "+usuarioServicio.obtenerUsuarioPorId(id).getNombre()+" se registró correctamente";
            modelo.put("mensaje", mensaje);
        }catch (MiExcepcion ex){
            String error = ex.getMessage();
            modelo.put("error",error);
        }
        return "inicio.html";
    }

    //---------------------- Da por cumplida una tarea especificada con su ID y retorna a la vista de inicio ----------------------------------

    @PreAuthorize("hasAnyRole('LIDER','USUARIO')")
    @GetMapping("/cumplir/{id}")
    public String cumplirTarea(@PathVariable Long id){
        try {
            tareaServicio.cumplirTarea(id);
        } catch (Exception ex){
            System.out.println("ERROR: no se pudo dar por cumplida la tarea con el id: "+id);
            return "redirect:/inicio";
        }
        return "redirect:/inicio";
    }

    //--------------------------- Elimina una tarea especificada con su ID y retorna a la vista de Inicio -----------------------------------------

    @PreAuthorize("hasRole('LIDER')")
    @GetMapping ("/eliminar/{id}")
    public String eliminarTarea(@PathVariable Long id){
        try {
            tareaServicio.eliminarTarea(id);
        } catch (Exception ex){
            System.out.println("ERROR: no se pudo eliminar la tarea con el id: "+id);
        }
        return "redirect:/inicio";
    }

    //---------------------- Crea una nota nueva para la tarea especificada con su ID y retorna a la vista de Inicio -----------------------------

    @PreAuthorize("hasAnyRole('LIDER','USUARIO')")
    @PostMapping("/crearNota")
    public String crearNota (@RequestParam Long id, @RequestParam String nota){
        try {
            tareaServicio.crearNota(id, nota);
        } catch (Exception ex){
            System.out.println("ERROR: no se pudo guardar la nota.");
        }
        return "redirect:/inicio";
    }

}
