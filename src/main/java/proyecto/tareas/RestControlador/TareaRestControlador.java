package proyecto.tareas.RestControlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proyecto.tareas.Excepciones.MiExcepcion;
import proyecto.tareas.entidades.Tarea;
import proyecto.tareas.servicios.TareaServicio;
import java.util.List;

@RestController
@RequestMapping("/tareaRest")
public class TareaRestControlador { // -------- Retorna respuestas en formato Json referente a la Clase Tarea

    @Autowired
    TareaServicio tareaServicio;

    //----------------- Retorna, en formato Json, una lista de todas las tareas existentes. ------------------------------------------------------

    @GetMapping
    public List<Tarea> obtenerTodasTareas(){
        return tareaServicio.obtenerTodasTareas();
    }

    //----------------- Retorna, en formato Json, una lista de tareas correspondiente al Id de usuario ingresado. ---------------------------------

    @GetMapping("/{id}")
    public List<Tarea> obtenerTareasPorId(@PathVariable Long id) throws MiExcepcion { // Obtiene tareas por Id de usuario
        List<Tarea> tareas = null;
        try {
            tareas = tareaServicio.verTareasPorIdUsuario(id);
        } catch (MiExcepcion e) {
            System.out.println("ERROR: "+e.getMessage());
            return tareas;
        }
        return tareas;
    }

    //----------------- Retorna, en formato Json, una lista de tareas CUMPLIDAS correspondiente al Id de usuario ingresado. ---------------------------

    @GetMapping("/cumplidas/{id}")
    public List<Tarea> obtenerTareasCumplidasPorId(@PathVariable Long id) throws MiExcepcion {
        List<Tarea> tareas = null;
        try {
            tareas = tareaServicio.verTareasCumplidasPorIdUsuario(id);
        }catch (Exception ex){
            System.out.println("ERROR: "+ex.getMessage());
        }
        return tareas;
    }

    //----------------- Retorna, en formato Json, una lista de tareas INCUMPLIDAS correspondiente al Id de usuario ingresado. ---------------------------

    @GetMapping("/incumplidas/{id}")
    public List<Tarea> obtenerTareasNoCumplidasPorId(@PathVariable Long id) throws MiExcepcion {
        List<Tarea> tareas = null;
        try {
            tareas = tareaServicio.verTareasNoCumplidasPorIdUsuario(id);
        }catch (Exception ex){
            System.out.println("ERROR: "+ex.getMessage());
        }
        return tareas;
    }

    //----------------- Retorna, en formato Json, una tarea correspondiente al Id de Tarea ingresado. ---------------------------

    @GetMapping("/tarea/{id}")
    public Tarea obtenerTareaPorId(@PathVariable Long id){
        return tareaServicio.obtenerTareaPorId(id);
    }


}
