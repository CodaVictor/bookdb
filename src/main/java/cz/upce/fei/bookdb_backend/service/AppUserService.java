package cz.upce.fei.bookdb_backend.service;

import cz.upce.fei.bookdb_backend.domain.AppUser;
import cz.upce.fei.bookdb_backend.domain.Role;
import cz.upce.fei.bookdb_backend.repository.AppUserRepository;
import cz.upce.fei.bookdb_backend.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppUserService implements UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<AppUser> findUserByEmail(String email) {
        log.info("Fetching user with email {}.", email);
        return appUserRepository.findByEmail(email);
    }

    public List<AppUser> getAllAppUsers() {
        log.info("Fetching all users.");
        return appUserRepository.findAll();
    }

    public AppUser saveUser(AppUser appUser) {
        log.info("Saving new user [{} {}: {}] to the database.", appUser.getFirstName(), appUser.getLastName(), appUser.getEmail());
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return appUserRepository.save(appUser);
    }

    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database.", role.getName());
        return roleRepository.save(role);
    }

    public void addRoleToUser(String email, String roleName) {
        log.info("Adding role {} to user with email {}.", roleName, email);
        AppUser appUser = appUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found in database."));
        Role role = roleRepository.findByName(roleName);
        appUser.getRoles().add(role);
    }

    public void deleteUser(String email) {
        log.info("Deleting user with email {}.", email);
        appUserRepository.deleteAppUserByEmail(email);
    }

    // Zabezpečení uživatele
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> optUser = appUserRepository.findByEmail(username);
        if(optUser.isEmpty()) {
            log.error("User not found in the database.");
            throw new UsernameNotFoundException("User not found in database.");
        } else {
            log.info("User found in the database.");
        }

        AppUser user = optUser.get();
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
