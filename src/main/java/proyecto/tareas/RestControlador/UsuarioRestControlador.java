package proyecto.tareas.RestControlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import proyecto.tareas.entidades.Usuario;
import proyecto.tareas.servicios.UsuarioServicio;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioRestControlador {

    @Autowired
    UsuarioServicio usuarioServicio;

    //----------------- Retorna, en formato Json, una lista de usuarios empleados correspondiente al Id de empresa ingresado. ---------------------------

    @GetMapping("/empleados/{id}")
    public List<Usuario> obtenerEmpleadosEmpresa(@PathVariable Long id){
        try {
            return usuarioServicio.obtenerEmpleadosEmpresa(id);
        } catch (Exception ex){
            return null;
        }
    }

    //----------------- Retorna, en formato Json, una usuario correspondiente al Id ingresado. ---------------------------

    @GetMapping("/{id}")
    public Usuario obtenerUsuarioPorId(@PathVariable Long id){
        try {
            return usuarioServicio.obtenerUsuarioPorId(id);
        } catch (Exception ex){
            return null;
        }
    }

    //----------------- Recibe una peticion HTTP POST y Elimina un usuario segun corresponda. ---------------------------

    @PreAuthorize("hasRole('LIDER')")
    @PostMapping("/eliminar")
    public void eliminarUsuario (ModelMap modelo, @RequestBody  Usuario usuario){
        usuarioServicio.bajaUsuario(usuario.getId());
    }
}
