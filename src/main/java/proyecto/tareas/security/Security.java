package proyecto.tareas.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Security extends WebSecurityConfigurerAdapter {


    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/*").hasRole("ADMIN") // Restringe vistas solo para ADMIN
                .antMatchers("/css/*", "/js/*", "/img/*", // Permite archivos css, js, img, etc para cualquier usuario.
                        "/**").permitAll()
                .and().
                formLogin()
                .loginPage("/") // Controlador a referenciar para loguear Usuario
                .loginProcessingUrl("/logincheck") // URL de chequeo de los parametros recibidos.
                .usernameParameter("username") // nombre de usuario necesario para el logueo
                .passwordParameter("password") // contraseña de usuario necesaria para el logueo
                .defaultSuccessUrl("/inicio") // URL a la que se dirige cuando el logueo es exitoso.
                .permitAll()
                .and().logout()
                .logoutUrl("/logout") // URL para generar el deslogueo.
                .logoutSuccessUrl("/") // URL a la que se dirige cuando el deslogueo es exitoso.
                .permitAll().
                and().csrf().disable();
    }
}