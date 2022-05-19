package proyecto.tareas.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import proyecto.tareas.entidades.Tarea;
import java.util.List;

@Repository
public interface TareaRepositorio extends JpaRepository<Tarea, Long> {// Gestiona todas las tareas creadas en el sistema.-----------

    // Retorna todas las tareas del usuario correspondiente al Id recibido---------
    @Query(value = "SELECT t FROM Tarea t WHERE t.usuario.id = :idUsuario")
    public List<Tarea> verTareasPorIdUsuario(@Param("idUsuario") Long idUsuario);

    // Retorna todas las tareas CUMPLIDAS del usuario correspondiente al Id recibido---------
    @Query(value = "SELECT t FROM Tarea t WHERE t.usuario.id = :idUsuario AND t.cumplida = TRUE")
    public List<Tarea> verTareasCumplidasPorIdUsuario(@Param("idUsuario") Long idUsuario);

    // Retorna todas las tareas INCUMPLIDAS del usuario correspondiente al Id recibido---------
    @Query(value = "SELECT t FROM Tarea t WHERE t.usuario.id = :idUsuario AND t.cumplida = FALSE")
    public List<Tarea> verTareasNoCumplidasPorIdUsuario(@Param("idUsuario") Long idUsuario);
}
