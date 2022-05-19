package proyecto.tareas.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import proyecto.tareas.Excepciones.MiExcepcion;
import proyecto.tareas.entidades.Foto;
import proyecto.tareas.repositorios.FotoRepositorio;

import java.util.Optional;

@Service
public class FotoServicio {

    @Autowired
    FotoRepositorio fotoRepositorio;

    @Transactional
    public Foto guardarFoto (MultipartFile archivo) throws MiExcepcion {
        if (!archivo.isEmpty()){
            try {
                Foto foto = new Foto();
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());

                return fotoRepositorio.save(foto);
            } catch (Exception ex){
               throw new MiExcepcion("ERROR: Al cargar la foto del usuario");
            }
        }  return null;
    }

    @Transactional
    public Foto actualizarFoto (Long idFoto, MultipartFile archivo) throws MiExcepcion {
        if (!archivo.isEmpty()){
            try {
                Foto foto = new Foto();

                if (idFoto != null){
                    Optional<Foto> respuesta = fotoRepositorio.findById(idFoto);
                    if (respuesta.isPresent()){
                        foto = respuesta.get();
                    }
                }

                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());

                return fotoRepositorio.save(foto);
            } catch (Exception ex){
                throw new MiExcepcion("ERROR al intentar actualizar la foto.");
            }
        }  else if (fotoRepositorio.findById(idFoto).isPresent()){
            return fotoRepositorio.getById(idFoto);
        } else return null;
    }
}
