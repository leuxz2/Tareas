package proyecto.tareas.Excepciones;

public class MiExcepcion extends Exception {

    public MiExcepcion() { // Clase que controla las Excepciones del sistema y las personaliza con un mensaje.
    }

    public MiExcepcion(String message) {
        super(message);
    }
}