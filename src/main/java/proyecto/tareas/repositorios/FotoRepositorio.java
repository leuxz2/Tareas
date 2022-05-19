package proyecto.tareas.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyecto.tareas.entidades.Foto;

@Repository
public interface FotoRepositorio extends JpaRepository<Foto, Long> { // Administra las fotos guardadas en la BD.--------

}
