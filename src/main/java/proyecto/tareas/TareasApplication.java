package proyecto.tareas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import proyecto.tareas.servicios.UsuarioServicio;

@SpringBootApplication
public class TareasApplication {

	@Autowired
	UsuarioServicio usuarioServicio;

	public static void main(String[] args) {
		SpringApplication.run(TareasApplication.class, args);
	}


	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception { // Encriptamos la clave del usuario
		auth
				.userDetailsService(usuarioServicio)
				.passwordEncoder(new BCryptPasswordEncoder());
	}
}
