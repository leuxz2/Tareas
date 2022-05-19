package proyecto.tareas.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import proyecto.tareas.Excepciones.MiExcepcion;
import proyecto.tareas.entidades.Empresa;
import proyecto.tareas.repositorios.EmpresaRepositorio;

import java.util.List;

@Service
public class EmpresaServicio {

    @Autowired
    EmpresaRepositorio empresaRepositorio;

    @Autowired
    FotoServicio fotoServicio;

    public Empresa crearEmpresa(String nombre, String rubro, String actividad) {
        Empresa empresa = new Empresa();

        empresa.setNombre(nombre);
        empresa.setRubro(rubro);
        empresa.setActividad(actividad);

       return empresaRepositorio.save(empresa);

    }


    public Empresa obtenerEmpresaPorId(Long id) {
        return empresaRepositorio.getById(id);
    }

    public Integer cantidadUsuarios(Long idEmpr) {
        return empresaRepositorio.getById(idEmpr).getCantUsuarios();
    }

    public void modificarEmpresa(Long id, String nombreEmpr, String rubroEmpr, String actividadEmpr, MultipartFile archivoUsuario) throws MiExcepcion {

        validarEmpresa(nombreEmpr,rubroEmpr,actividadEmpr);

        Empresa empresa = empresaRepositorio.getById(id);

        empresa.setNombre(nombreEmpr);
        empresa.setRubro(rubroEmpr);
        empresa.setActividad(actividadEmpr);
        if (empresaRepositorio.getById(id).getFoto()==null){
            empresa.setFoto(fotoServicio.guardarFoto(archivoUsuario));
        } else {
            empresa.setFoto(fotoServicio.actualizarFoto(empresaRepositorio.getById(id).getFoto().getId(),archivoUsuario));
        }

        empresaRepositorio.save(empresa);
    }

    private void validarEmpresa(String nombreEmpr, String rubroEmpr, String actividadEmpr) throws MiExcepcion {
        if (nombreEmpr.isEmpty() || nombreEmpr == null){
            throw new MiExcepcion("ERROR: El nombre no puede estar vacío o ser nulo.");
        }

        if (rubroEmpr.isEmpty() || rubroEmpr == null){
            throw new MiExcepcion("ERROR: El rubro no puede estar vacío o ser nulo.");
        }

        if (actividadEmpr.isEmpty() || actividadEmpr == null){
            throw new MiExcepcion("ERROR: La actividad no puede estar vacía o ser nula.");
        }
    }
}
