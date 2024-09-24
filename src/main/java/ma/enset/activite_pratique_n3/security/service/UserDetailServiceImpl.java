package ma.enset.activite_pratique_n3.security.service;

import lombok.AllArgsConstructor;
import ma.enset.activite_pratique_n3.security.entities.AppUser;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor



//Retrieve the user from the database, compare the password, check if the user and role exist, etc
public class UserDetailServiceImpl implements UserDetailsService {





    private AccountService accountService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       AppUser appUser= accountService.loadUserByUsername(username);


       if(appUser==null) throw new UsernameNotFoundException(String.format("user %s not found",username));
        //table string : user with their role.
        String[] roles = appUser.getRoles().stream().map(u -> u.getRole()).toArray(String[]::new);

        return User.withUsername(appUser.getUsername())
                                    .password(appUser.getPassword())
                                    .roles(roles).build();

    }
}
