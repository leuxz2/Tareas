
package proyecto.tareas.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import proyecto.tareas.Enums.Rol;


@Entity
public class Usuario implements Serializable {
    
    @Id     
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nombre, apellido, clave, puesto;
    private Integer dni;

    @Column(unique = true)
    private String email;

    private boolean alta; // Cuando se elimina un usuario lo que en realidad hacemos es darlo de baja, pero su informacion se sigue conservando en la BD.

    @OneToOne
    private Foto foto; // un usuario puede tener solo una foto en particular

    @Enumerated(EnumType.STRING)
    private Rol rol; // El rol puede ser LIDER (de empresa) o USUARIO (empleado de empresa existente)

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private Integer cantidadTareas, tareasCumplidas;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "usuario")
    private List<Tarea> tareas; // Un usuario puede tener multiples tareas asignadas

    @ManyToOne
    private Empresa empresa; // Muchos usuarios pueden compartir la misma empresa

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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer dni) {
        this.dni = dni;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public boolean isAlta() {
        return alta;
    }

    public void setAlta(boolean alta) {
        this.alta = alta;
    }

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Integer getCantidadTareas() {
        return cantidadTareas;
    }

    public void setCantidadTareas(Integer cantidadTareas) {
        this.cantidadTareas = cantidadTareas;
    }

    public Integer getTareasCumplidas() {
        return tareasCumplidas;
    }

    public void setTareasCumplidas(Integer tareasCumplidas) {
        this.tareasCumplidas = tareasCumplidas;
    }

    public List<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}
