package proyecto.tareas.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import proyecto.tareas.entidades.Empresa;
import javax.persistence.EntityManager;
import java.util.List;

@Repository
public interface EmpresaRepositorio extends JpaRepository<Empresa, Long> {

    // ---------------Retorna todas las empresas segun ID en orden Desdencente.-------------------
    @Query(value = "SELECT e FROM Empresa e ORDER BY e.id DESC")
    List<Empresa> obtenerEmpresas();
}
