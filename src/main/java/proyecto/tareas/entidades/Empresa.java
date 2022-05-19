package proyecto.tareas.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre,rubro,actividad;
    private Integer cantUsuarios =0;

    @OneToOne
    private Foto foto; // Una empresa puede tener solo una foto en particular

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "empresa")
    private List<Usuario> usuario;



    //----------------------------- GETTERS Y SETTERS ----------------------------------


    public Integer getCantUsuarios() {
        return cantUsuarios;
    }

    public void setCantUsuarios(Integer cantUsuarios) {
        this.cantUsuarios = cantUsuarios;
    }

    public String getRubro() {
        return rubro;
    }

    public void setRubro(String rubro) {
        this.rubro = rubro;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }

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

    public List<Usuario> getUsuario() {
        return usuario;
    }

    public void setUsuario(List<Usuario> usuario) {
        this.usuario = usuario;
    }
}
