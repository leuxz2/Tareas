
package proyecto.tareas.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import proyecto.tareas.Excepciones.MiExcepcion;
import proyecto.tareas.entidades.Usuario;
import proyecto.tareas.servicios.UsuarioServicio;
import java.nio.file.Path;
import java.nio.file.Paths;


@Controller
public class UsuarioControlador {

    @Autowired
    UsuarioServicio usuarioServicio;


    //----------------- Registra un usuario Lider de empresa y retorna a la vista de index para su posterior logueo --------------------------------------------

    @PostMapping("/registrarUsuarioLider")
    public String registrarUsuarioLider (ModelMap modelo, @RequestParam String nombre, @RequestParam String apellido, @RequestParam Integer dni
                                    , @RequestParam String email, @RequestParam String clave, @RequestParam MultipartFile archivoUsuario, @RequestParam String repetirClave
                                    , @RequestParam String nombreEmp,@RequestParam MultipartFile archivoEmpresa, @RequestParam String rubroEmp, @RequestParam String actividadEmp) throws MiExcepcion {
        String error = null;
        String mensaje = "El usuario "+nombre+" y su empresa '"+nombreEmp+"' se registraron correctamente, ahora podes loguearte";

        try {
            usuarioServicio.crearUsuarioLider(nombre,apellido,dni,email,clave,repetirClave, archivoUsuario,nombreEmp,archivoEmpresa,rubroEmp,actividadEmp);
        }catch (MiExcepcion ex){
            modelo.put("nombre",nombre);
            modelo.put("apellido", apellido);
            modelo.put("dni",dni);
            modelo.put("email",email);
            modelo.put("nombreEmp",nombreEmp);
            modelo.put("rubroEmp",rubroEmp);
            modelo.put("actividadEmp",actividadEmp);
            modelo.put("archivoUsuario",archivoUsuario);
            modelo.put("archivoEmpresa",archivoEmpresa);
            modelo.put("error",ex.getMessage());
            return "crearUsuarioLider.html";
        }
        modelo.put("mensaje",mensaje);
        return "index.html";
    }

//---------------------------- Registra un Usuario empleado de empresa y retorna a la vista de Administracion de empresa -----------------------------------

    @PreAuthorize("hasRole('LIDER')")
    @PostMapping("/registrarUsuario")
    public String registrarUsuario (ModelMap modelo, @RequestParam String nombre, @RequestParam String apellido, @RequestParam Integer dni
            ,@RequestParam MultipartFile archivoUsuario, @RequestParam String email, @RequestParam String clave,@RequestParam String puesto, @RequestParam String repetirClave, @RequestParam Long idEmpr,@RequestParam Long idLider) throws MiExcepcion {

        String error = null;

        try {
            usuarioServicio.crearUsuario(nombre,apellido,dni,archivoUsuario,email,clave, repetirClave,puesto,idEmpr);

        }catch (MiExcepcion ex){
            modelo.put("nombre",nombre);
            modelo.put("apellido", apellido);
            modelo.put("dni",dni);
            modelo.put("email",email);
            modelo.put("puesto",puesto);
            modelo.put("archivoUsuario",archivoUsuario);
            modelo.put("idEmpr",idEmpr);
            modelo.put("error", ex.getMessage());
            return "crearUsuario.html";
        }
        modelo.put("mensaje","El usuario "+nombre+" se registro correctamente");
        modelo.put("usuarios",usuarioServicio.obtenerEmpleadosEmpresa(idLider));
        return "administrarEmpresa.html";
    }

    //------------------------------------- Modifica la informacion de cualquier tipo de usuario ---------------------------------------------------------

    @PreAuthorize("hasRole('LIDER')")
    @PostMapping("/modificarUsuario")
    public String modificarUsuario(ModelMap modelo,@RequestParam Long id,@RequestParam String nombre, @RequestParam String apellido, @RequestParam String dni,
                                   @RequestParam MultipartFile archivoUsuario, @RequestParam String email, @RequestParam String puesto) throws MiExcepcion {

        try {
            usuarioServicio.modificarUsuario(id,nombre, apellido, Integer.valueOf(dni), archivoUsuario, email, puesto);
        }catch (MiExcepcion ex){
            modelo.put("error",ex.getMessage());
        }

        modelo.put("usuarios", usuarioServicio.obtenerEmpleadosEmpresa(id));
        return "administrarEmpresa.html";
    }

    //------------------------------- Elimina al usuario empleado cuyo Id es recibido como parametro (No elimina Usuario Lider) ------------------------------

    @PreAuthorize("hasRole('LIDER')")
    @PostMapping("/eliminarUsuario")
    public String eliminarUsuario (ModelMap modelo, @RequestParam Long idEmp){
        usuarioServicio.bajaUsuario(idEmp);
        modelo.put("usuarios", usuarioServicio.obtenerEmpleadosEmpresa(idEmp));
        return "administrarEmpresa.html";
    }
}


