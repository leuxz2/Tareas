package proyecto.tareas.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import proyecto.tareas.Excepciones.MiExcepcion;
import proyecto.tareas.entidades.Empresa;
import proyecto.tareas.entidades.Usuario;
import proyecto.tareas.servicios.EmpresaServicio;
import proyecto.tareas.servicios.UsuarioServicio;

@Controller
@RequestMapping("/foto")
public class FotoControlador {

    @Autowired
    UsuarioServicio usuarioServicio;

    @Autowired
    EmpresaServicio empresaServicio;

    //----- Comprueba si el usuario tiene foto, si la tiene la retorna, sino retorna un HttpStatus.NOT_FOUND--------------

    @PreAuthorize("hasAnyRole('LIDER','USUARIO')")
    @GetMapping("/usuario/{id}")
    public ResponseEntity<byte[]> fotoUsuario (@PathVariable Long id){

        try {
            Usuario usuario = usuarioServicio.obtenerUsuarioPorId(id);
            if (usuario.getFoto() == null){
                throw new MiExcepcion("ATENCION: El usuario no tiene foto guardada.");
            }
            byte[] foto = usuario.getFoto().getContenido();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (Exception ex){
            System.out.println(ex.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //----- Comprueba si la empresa tiene foto, si la tiene la retorna, sino retorna un HttpStatus.NOT_FOUND--------------

    @PreAuthorize("hasAnyRole('LIDER','USUARIO')")
    @GetMapping("/empresa/{id}")
    public ResponseEntity<byte[]> fotoEmpresa (@PathVariable Long id){

        try {
            Empresa empresa = empresaServicio.obtenerEmpresaPorId(id);
            if (empresa.getFoto() == null){
                throw new MiExcepcion("ATENCION: La empresa no tiene foto guardada.");
            }
            byte[] foto = empresa.getFoto().getContenido();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (Exception ex){
            System.out.println(ex.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
