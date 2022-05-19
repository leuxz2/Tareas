package proyecto.tareas.entidades;

import javax.persistence.*;

@Entity
public class Foto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre,mime;

    @Lob @Basic(fetch = FetchType.LAZY) // @Lob indica que sera un archivo grande./Fetch tipo LAZY indica que debe ir a buscar el archivo solo cuando se lo necesite.
    private byte[] contenido;

    //----------------------------- GETTERS Y SETTERS ----------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public byte[] getContenido() {
        return contenido;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }
}
