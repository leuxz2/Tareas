package proyecto.tareas.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import proyecto.tareas.entidades.Usuario;
import java.util.List;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

    // Retorna todos los empleados de la empresa cuyo ID es indicado y que estén dados de alta en el sistema.-----------
    @Query(value = "SELECT u FROM Usuario u WHERE u.empresa.id = :idEmp AND u.alta = true")
    List<Usuario> obtenerEmpleadosEmpresa(@Param("idEmp") Long idEmp);

    // Retorna el usuario cuyo email coincida con el indicado por el servicio que lo requiere.----------------
    @Query(value = "SELECT u FROM Usuario u WHERE u.email = :email")
    Usuario obtenerUsuarioPorEmail(String email);

}
