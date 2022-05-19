package proyecto.tareas.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import proyecto.tareas.Enums.Rol;
import proyecto.tareas.Excepciones.MiExcepcion;
import proyecto.tareas.entidades.Empresa;
import proyecto.tareas.entidades.Usuario;
import proyecto.tareas.repositorios.EmpresaRepositorio;
import proyecto.tareas.repositorios.FotoRepositorio;
import proyecto.tareas.repositorios.UsuarioRepositorio;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    EmpresaRepositorio empresaRepositorio;

    @Autowired
    FotoServicio fotoServicio;

    // ----------------Obtiene todos los usuarios -------------------------
    public List<Usuario> ObtenerUsuarios (){
        return usuarioRepositorio.findAll();
    }

    //----------------- Crear usuario dentro de una empresa existente ------------------------
    @Transactional
    public void crearUsuario(String nombre, String apellido, Integer dni,MultipartFile archivoUsuario,String email, String clave,String repetirClave,String puesto, Long idEmpr) throws MiExcepcion {

        validarUsuario(nombre,apellido,dni,email,clave,repetirClave,puesto);

        Empresa empresa = empresaRepositorio.getById(idEmpr);
        if (empresa.getCantUsuarios() < 5){
            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setDni(dni);
            usuario.setFoto(fotoServicio.guardarFoto(archivoUsuario));
            usuario.setEmail(email);
            usuario.setPuesto(puesto);
            usuario.setCantidadTareas(0);
            usuario.setTareasCumplidas(0);
            usuario.setAlta(true);
            usuario.setTareas(null);
            usuario.setRol(Rol.USUARIO);
            usuario.setEmpresa(empresa);

            String encriptada = new BCryptPasswordEncoder().encode(clave);
            usuario.setClave(encriptada);


            usuarioRepositorio.save(usuario);
            empresa.setCantUsuarios(empresa.getCantUsuarios()+1);
            empresaRepositorio.save(empresa);
        } else{
            throw new MiExcepcion("ERROR: La empresa ya tiene los 5 usuarios cubiertos, elija la version PREMIUM!");
        }

    }

    //------------------------------------------------- VALIDAR USUARIO ---------------------------------------------------------------
    private void validarUsuario(String nombre, String apellido, Integer dni, String email, String clave, String repetirClave, String puesto) throws MiExcepcion {

        if (nombre.isEmpty() || nombre == null){
            throw new MiExcepcion("El campo 'nombre' no puede estar vacío");
        }

        if (apellido.isEmpty() || apellido == null){
            throw new MiExcepcion("El campo 'apellido' no puede estar vacío");
        }
        if (dni == null || dni.toString().length()<6 || dni.toString().length()>9){
            throw new MiExcepcion("El campo 'dni' debe tener minimo 6 caracteres y menos de 10.");
        }

        if(usuarioRepositorio.obtenerUsuarioPorEmail(email)!=null){
            throw new MiExcepcion("ERROR: El email ya existe en la Base de Datos.");
        }

        if (!clave.equals(repetirClave) || clave.length()<3){
            throw new MiExcepcion("La claves deben ser idénticas y tener al menos 6 caracteres");
        }

        if (puesto.isEmpty() || puesto == null){
            throw new MiExcepcion("El campo 'puesto' no puede estar vacío");
        }
    }

    // --------------------------Crear usuario Lider de empresa--------------------------------------------------

    @Transactional
    public void crearUsuarioLider(String nombre, String apellido, Integer dni, String email, String clave,String repetirClave, MultipartFile archivoUsuario, String nombreEmp,MultipartFile archivoEmpresa, String rubroEmp, String actividadEmp) throws MiExcepcion {

        validarUsuarioLider(nombre, apellido, dni,email,clave,repetirClave, nombreEmp, rubroEmp, actividadEmp);

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setDni(dni);
        usuario.setEmail(email);
        usuario.setPuesto("Lider");
        usuario.setFoto(fotoServicio.guardarFoto(archivoUsuario));
        usuario.setCantidadTareas(0);
        usuario.setTareasCumplidas(0);
        usuario.setAlta(true);
        usuario.setTareas(null);
        usuario.setRol(Rol.LIDER);

        Empresa empresa = new Empresa();
        empresa.setNombre(nombreEmp);
        empresa.setRubro(rubroEmp);
        empresa.setActividad(actividadEmp);
        empresa.setCantUsuarios(0);
        empresa.setFoto(fotoServicio.guardarFoto(archivoEmpresa));
        usuario.setEmpresa(empresaRepositorio.save(empresa));

        String encriptada = new BCryptPasswordEncoder().encode(clave);
        usuario.setClave(encriptada);

        usuarioRepositorio.save(usuario);
    }

//------------------------------------------------- VALIDAR USUARIO LIDER---------------------------------------------------------------

    public void validarUsuarioLider (String nombre, String apellido, Integer dni, String email,String clave,String repetirClave,String nombreEmp, String rubroEmp,String actividadEmp) throws MiExcepcion {

        if (nombre.isEmpty() || nombre == null){
            throw new MiExcepcion("El campo 'nombre' no puede estar vacío");
        }

        if (apellido.isEmpty() || apellido == null){
            throw new MiExcepcion("El campo 'apellido' no puede estar vacío");
        }
        if (dni == null || dni.toString().length()<6 || dni.toString().length()>9){
            throw new MiExcepcion("El campo 'dni' debe tener minimo 6 caracteres y menos de 10.");
        }

        if(usuarioRepositorio.obtenerUsuarioPorEmail(email)!=null){
            throw new MiExcepcion("ERROR: El email ya existe en la Base de Datos.");
        }

        if (!clave.equals(repetirClave) || clave.length()<3){
            throw new MiExcepcion("La claves deben ser idénticas y tener al menos 6 caracteres");
        }

        if (nombreEmp.isEmpty() || nombreEmp == null){
            throw new MiExcepcion("El campo 'Nombre Empresa' no puede estar vacío");
        }

        if (rubroEmp.isEmpty() || rubroEmp == null){
            throw new MiExcepcion("El campo 'Rubro Empresa' no puede estar vacío");
        }

        if (actividadEmp.isEmpty() || actividadEmp == null){
            throw new MiExcepcion("El campo 'Actividad Empresa' no puede estar vacío");
        }
    }

    //------------------------------------------------- OBTENER USUARIO POR ID ---------------------------------------------------------------

    public Usuario obtenerUsuarioPorId(Long idUsuario) {
        Usuario usuario = usuarioRepositorio.getById(idUsuario);
        if (usuario != null) {
            return usuario;
        } else return null;
    }

    //------------------------------------------------- OBTENER EMPLEADOS EMPRESA  ---------------------------------------------------------------

    public List<Usuario> obtenerEmpleadosEmpresa(Long idLider) {
        Usuario usuario = usuarioRepositorio.getById(idLider);
        return usuarioRepositorio.obtenerEmpleadosEmpresa(usuario.getEmpresa().getId());
    }

    //------------------------------------------------- MODIFICAR USUARIO ---------------------------------------------------------------

    public void modificarUsuario(Long id, String nombre, String apellido, Integer dni, MultipartFile archivoUsuario, String email,String puesto) throws MiExcepcion {

        validarModificacionUsuario(nombre,apellido,dni,email,puesto);

        Usuario usuario = usuarioRepositorio.getById(id);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setDni(dni);
        if (usuarioRepositorio.getById(id).getFoto()==null){
            usuario.setFoto(fotoServicio.guardarFoto(archivoUsuario));
        } else {
            usuario.setFoto(fotoServicio.actualizarFoto(usuarioRepositorio.getById(id).getFoto().getId(),archivoUsuario));
        }
        usuario.setEmail(email);
        usuario.setPuesto(puesto);

        usuarioRepositorio.save(usuario);
    }
    //------------------------------------------------- VALIDAR MODIFICACION DE USUARIO --------------------------------------------

    private void validarModificacionUsuario(String nombre, String apellido, Integer dni, String email, String puesto) throws MiExcepcion {

        if (nombre.isEmpty() || nombre == null){
            throw new MiExcepcion("El campo 'nombre' no puede estar vacío");
        }

        if (apellido.isEmpty() || apellido == null){
            throw new MiExcepcion("El campo 'apellido' no puede estar vacío");
        }
        if (dni == null || dni.toString().length()<6 || dni.toString().length()>9){
            throw new MiExcepcion("El campo 'dni' debe tener minimo 6 caracteres y maximo 9.");
        }

        if (email.isEmpty() || email == null){
            throw new MiExcepcion("El campo 'email' no puede estar vacío");
        }

        /*if (!clave.equals(repetirClave) || clave.length()<3){
            throw new MiExcepcion("La claves deben ser idénticas y tener al menos 6 caracteres");
        }*/

        if (puesto.isEmpty() || puesto == null){
            throw new MiExcepcion("El campo 'puesto' no puede estar vacío");
        }
    }

    //------------------------------------------------- BAJA USUARIO ---------------------------------------------------------------

    public void bajaUsuario(Long id) {
        Usuario usuario = usuarioRepositorio.getById(id);
        usuario.setAlta(false);
        usuarioRepositorio.save(usuario);
        Empresa empresa = empresaRepositorio.getById(usuario.getEmpresa().getId());
        empresa.setCantUsuarios(empresa.getCantUsuarios()-1);
        empresaRepositorio.save(empresa);
    }

    // ------------Metodo de la implementacion UserDetailService encargado del logueo y la sesión del usuario.----------------
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = null;
        try {
            usuario = usuarioRepositorio.obtenerUsuarioPorEmail(email);
        } catch (Exception e) {
            throw new UsernameNotFoundException("No se encontro el usuario.");
        }

        if (usuario!=null) {

            List<GrantedAuthority> permisos = new ArrayList<>();

            // Creo una lista de permisos
            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_"+usuario.getRol());
            permisos.add(p1);

            //Esto me permite guardar el OBJETO USUARIO(Cliente) LOG, para luego ser utilizado
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);

            User user = new User(usuario.getEmail(), usuario.getClave(), permisos);
            return user;


        } else return null;
    }
}
