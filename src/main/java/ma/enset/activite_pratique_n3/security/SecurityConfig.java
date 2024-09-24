package ma.enset.activite_pratique_n3.security;

import lombok.AllArgsConstructor;
import ma.enset.activite_pratique_n3.repository.PatientRepository;
import ma.enset.activite_pratique_n3.security.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.sql.DataSource;

import static org.springframework.security.authorization.AuthenticatedAuthorizationManager.rememberMe;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig {

    PatientRepository patientRepository;

    UserDetailServiceImpl userDetailServiceimpl;

    //Stratégie JBDCAthentification
     // @Bean
      public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {

           return new JdbcUserDetailsManager(dataSource);
    }


   //Stratégie InMemory
   // @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder){
        String encodedPassword = passwordEncoder.encode("1234");


        return new InMemoryUserDetailsManager(

               // User.withUsername("user1").password("{noop}1234").roles("USER").build(),
                User.withUsername("user1").password(encodedPassword).roles("USER").build(),
                User.withUsername("user2").password(encodedPassword).roles("USER").build(),
                User.withUsername("admin").password(encodedPassword).roles("USER","ADMIN").build()
        );
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity

                .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/",true) .permitAll())
                //.rememberMe(rememberMe -> rememberMe())
                //If we're not allowed to access Boostrapp
                .authorizeHttpRequests(ar->ar.requestMatchers("/webjars/**","/h2-console/**").permitAll())
                .authorizeHttpRequests(ar->ar.anyRequest().authenticated())
                .exceptionHandling(ar->ar.accessDeniedPage("/notAuthorized"))
                .userDetailsService(userDetailServiceimpl)
                .build();
               //.formLogin(Customizer.withDefaults())
                //.rememberMe(rememberMe -> rememberMe())
                /* .rememberMe(rememberMe -> rememberMe
                        .key("test") // Définissez une clé unique et forte pour le "remember-me"
                        .tokenValiditySeconds(24 * 60 * 60) // Définissez la durée de validité des tokens (en secondes)
                 )*/

        //.rememberMe(rememberMe -> rememberMe.key("uniqueAndSecret"))
               // .logout(logout -> logout.deleteCookies("JSESSIONID"))


              // soit utiliser
              //  .authorizeHttpRequests(ar->ar.requestMatchers("/admin/**").hasRole("ADMIN"))
              //  .authorizeHttpRequests(ar->ar.requestMatchers("/user/**").hasRole("USER"))
                // ou annotation @EnableMethodSecurity(prePostEnabled = true) et @PreAuthorize("hasRole('Role_ADMIN')") en cotrolleur

                 //interdire Tous les applications()Tous les requetes necessitent une authentification (sauf admin ,user et vous pouvez avoir boostrap et h2 console)

                //appel function loadUserByUsername

    }

}
