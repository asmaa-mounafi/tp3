package ma.enset.activite_pratique_n3;

import ma.enset.activite_pratique_n3.entities.Patient;
import ma.enset.activite_pratique_n3.repository.PatientRepository;
import ma.enset.activite_pratique_n3.security.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.util.Date;
@SpringBootApplication
public class ActivitePratiqueN3Application {

    @Autowired
    PatientRepository patientRepository;

    public static void main(String[] args) {
        SpringApplication.run(ActivitePratiqueN3Application.class, args);
    }

     //@Override
    /* public void run(String... args) throws Exception {
        /*Patient p1=new Patient();
        p1.setId(null);
        p1.setNom("aya");
        p1.setDateN(new Date());
        p1.setMalade(false);
        p1.setScore(120);

        patientRepository.save(p1);


    }*/
//@Bean
CommandLineRunner start(PatientRepository patientRepository) {
    Patient p1 = new Patient();
    p1.setId(null);
    p1.setNom("aya");
    p1.setDateN(new Date());
    p1.setMalade(false);
    p1.setScore(120);
    return args -> {
        patientRepository.save(p1);
    };
}
//for JDBC AUTHENTIFICATION
//@Bean

CommandLineRunner commandLineRunner (JdbcUserDetailsManager jdbcUserDetailsManager)
    {
        PasswordEncoder passwordEncoder=passwordEncoder();
        return args -> {
            UserDetails u1= jdbcUserDetailsManager.loadUserByUsername("user1");
           // If the user does not exist
             if(u1==null)
         {
            //create user and their role
            jdbcUserDetailsManager.createUser(User.withUsername("user1").password(passwordEncoder.encode("1234")).roles("USER").build());
        }
            UserDetails u2= jdbcUserDetailsManager.loadUserByUsername("user2");

            if(u2==null)
            {
                //create user and their role
                jdbcUserDetailsManager.createUser(User.withUsername("user2").password(passwordEncoder.encode("1234")).roles("USER").build());
            }

            UserDetails u3= jdbcUserDetailsManager.loadUserByUsername("Admin");
            if(u3==null)
            {
                //create user and their role
                jdbcUserDetailsManager.createUser(User.withUsername("Admin").password(passwordEncoder.encode("1234")).roles("USER", "ADMIN").build());
            }
            else
            {
                jdbcUserDetailsManager.createUser(User.withUsername("Admin1").password(passwordEncoder.encode("1234")).roles("USER", "ADMIN").build());
            }

    };

    }
  //  FOR user detailsService Authentication ****************************************************************************************************************************************************************************************

   // @Bean

    CommandLineRunner commandLineRunnerUserDetails (AccountService accountService)
    {
        return args->
        {
            accountService.addNewRole("USER");
            accountService.addNewRole("ADMIN");
            accountService.addNewUser("user1","1234","user1@gmail.com","1234");
            accountService.addNewUser("user2","1234","user2@gmail.com","1234");
            accountService.addNewUser("admin","5678","admin@gmail.com","5678");


            accountService.addRoleToUser("user1","USER");

            accountService.addRoleToUser("user2","USER");

            accountService.addRoleToUser("admin","ADMIN");


        };
    }

    @Bean
    PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
