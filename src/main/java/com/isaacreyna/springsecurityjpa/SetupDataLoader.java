package com.isaacreyna.springsecurityjpa;

import com.isaacreyna.springsecurityjpa.model.Role;
import com.isaacreyna.springsecurityjpa.model.User;
import com.isaacreyna.springsecurityjpa.repository.RoleRepository;
import com.isaacreyna.springsecurityjpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event){
        if (alreadySetup)
            return;

        final Role adminRole = createRoleIfNotFound("ROLE_ADMIN");
        final Role userRole = createRoleIfNotFound("ROLE_USER");

        createUserIfNotFound("idreyna","idr@email.com", "Isaac", "Reyna", "8520", new ArrayList<Role>(Arrays.asList(adminRole)));
        createUserIfNotFound("john","jsmith@email.com", "John", "Smith", "8520", new ArrayList<Role>(Arrays.asList(userRole)));
        alreadySetup = true;
    }


    @Transactional
    private final Role createRoleIfNotFound(String name){
        Role role = roleRepository.findByName(name);

        if (role == null){
            role = new Role();
            role.setName(name);
        }

        role = roleRepository.save(role);
        return role;
    }

    @Transactional
    private final User createUserIfNotFound(
            final String userName,
            final String email,
            final String firstName,
            final String lastName,
            final String password,
            final List<Role> roles){

        Optional<User> usersResult = userRepository.findByUserName(userName); // Should return: Optional.empty

        User user = new User();

        if (usersResult.isPresent()){
            user = usersResult.get();
            return user;
        }

        user.setUserName(userName);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setActive(true);
        user.setRoles(roles);
        user = userRepository.save(user);

        return user;
    }
}
