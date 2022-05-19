package proyecto.tareas.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.tareas.Excepciones.MiExcepcion;
import proyecto.tareas.entidades.Tarea;
import proyecto.tareas.entidades.Usuario;
import proyecto.tareas.repositorios.TareaRepositorio;
import proyecto.tareas.repositorios.UsuarioRepositorio;

import java.util.Date;
import java.util.List;

@Service
public class TareaServicio {

    @Autowired
    TareaRepositorio tareaRepositorio;

    @Autowired
    UsuarioRepositorio usuarioRepositorio;


    public void crearTarea(Usuario usuario, String nombre, String descripcion, Date fechaFinal) throws MiExcepcion {
        Tarea tarea = new Tarea();
        tarea.setUsuario(usuario);
        usuario.setCantidadTareas(usuario.getCantidadTareas()+1);
        usuarioRepositorio.save(usuario);

        tarea.setNombre(nombre);
        tarea.setDescripcion(descripcion);
        tarea.setFechaFinal(fechaFinal);
        tarea.setFechaCreacion(new Date());
        tarea.setNota(null);
        tarea.setCumplida(false);
        tareaRepositorio.save(tarea);
    }

    public List<Tarea> verTareasPorIdUsuario(Long id) throws MiExcepcion {
        List<Tarea> tareas = null;
        try {
            tareas = tareaRepositorio.verTareasPorIdUsuario(id);
        } catch (Exception ex){
            throw new MiExcepcion("ERROR: Al querer buscar tareas por usuario");
        }
        if (!tareas.isEmpty()){
            return tareas;
        } else throw new MiExcepcion("ERROR: No se encontraron tareas para este usuario.");
    }

    public List<Tarea> verTareasCumplidasPorIdUsuario(Long id) throws MiExcepcion {
        List<Tarea> tareas = null;
        try {
            tareas = tareaRepositorio.verTareasCumplidasPorIdUsuario(id);
        } catch (Exception ex){
            throw new MiExcepcion("ERROR: Al querer buscar tareas por usuario");
        }
        if (!tareas.isEmpty()){
            return tareas;
        } else throw new MiExcepcion("ERROR: No se encontraron tareas CUMPLIDAS de este usuario.");
    }

    public List<Tarea> verTareasNoCumplidasPorIdUsuario(Long id) throws MiExcepcion {
        List<Tarea> tareas = null;
        try {
            tareas = tareaRepositorio.verTareasNoCumplidasPorIdUsuario(id);
        } catch (Exception ex){
            throw new MiExcepcion("ERROR: Al querer buscar tareas por usuario");
        }
        if (!tareas.isEmpty()){
            return tareas;
        } else throw new MiExcepcion("ERROR: No se encontraron tareas INCUMPLIDAS de este usuario.");
    }


    public List<Tarea> obtenerTodasTareas() {
        return tareaRepositorio.findAll();
    }

    public Tarea obtenerTareaPorId(Long id) {
        return tareaRepositorio.getById(id);
    }

    public void cumplirTarea(Long id) {
        Tarea tarea = tareaRepositorio.getById(id);
        tarea.setCumplida(true);
        tareaRepositorio.save(tarea);

        Usuario usuario = usuarioRepositorio.getById(tarea.getUsuario().getId()); // traemos al usuario
        usuario.setTareasCumplidas(usuario.getTareasCumplidas()+1); // incrementamos en 1 las tareas cumplidas del usuario
        usuarioRepositorio.save(usuario); // guardamos nuevamente el usuario

    }

    public void crearNota(Long id, String nota) {
        Tarea tarea = tareaRepositorio.getById(id);
        tarea.setNota(nota);
        tareaRepositorio.save(tarea);
    }

    public void eliminarTarea(Long id) {
        Tarea tarea = tareaRepositorio.getById(id);
        Usuario usuario = usuarioRepositorio.getById(tarea.getUsuario().getId());
        if (tarea.isCumplida()){
            usuario.setTareasCumplidas(usuario.getTareasCumplidas()-1);
        }
        usuario.setCantidadTareas(usuario.getCantidadTareas()-1);
        usuarioRepositorio.save(usuario);
        tareaRepositorio.delete(tarea);
    }
}
