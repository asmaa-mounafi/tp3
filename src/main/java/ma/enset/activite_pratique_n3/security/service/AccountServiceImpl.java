package ma.enset.activite_pratique_n3.security.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import ma.enset.activite_pratique_n3.security.entities.AppRole;
import ma.enset.activite_pratique_n3.security.entities.AppUser;
import ma.enset.activite_pratique_n3.security.repo.AppRoleRepository;
import ma.enset.activite_pratique_n3.security.repo.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public AppUser addNewUser(String username, String password, String email, String confirmPassword) {
        AppUser appUser=appUserRepository.findByUsername(username);
        if(appUser != null) throw  new RuntimeException("this user already exist ");
        if(!password.equals(confirmPassword)) throw  new RuntimeException("Passwords not match ");


        appUser=AppUser.builder()
                      .userId(UUID.randomUUID().toString())
                      .username(username)
                      .password(passwordEncoder.encode(password))
                      .email(email).build();
        AppUser saveappuser =appUserRepository.save(appUser);
        return  saveappuser;



    }

    @Override
    public AppRole addNewRole(String role) {
       AppRole appRole =appRoleRepository.findById(role).orElse(null);
       if(appRole != null)throw  new RuntimeException("this role already exist ");
       appRole=appRole.builder().role(role).build();

        return appRoleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String role) {
        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findById(role).get();
        //Add the role to the user (update user)
        appUser.getRoles().add(appRole);

    }


    @Override
    public void removeRoleFromUser(String username, String role) {
        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findById(role).get();
        //remove the role to this user
        appUser.getRoles().remove(appRole);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }


}
