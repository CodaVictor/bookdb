package cz.upce.fei.bookdb_backend.service;

import cz.upce.fei.bookdb_backend.config.JwtTokenUtil;
import cz.upce.fei.bookdb_backend.domain.AppUser;
import cz.upce.fei.bookdb_backend.domain.Role;
import cz.upce.fei.bookdb_backend.dto.AppUserRequestDtoV1;
import cz.upce.fei.bookdb_backend.repository.AppUserRepository;
import cz.upce.fei.bookdb_backend.repository.RoleRepository;
import cz.upce.fei.bookdb_backend.service.exception.ConflictEntityException;
import cz.upce.fei.bookdb_backend.service.exception.ResourceNotFoundException;
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
    private final JwtTokenUtil jwtTokenUtil;

    public Optional<AppUser> findUserByEmail(String email) {
        log.info("Fetching user with email {}.", email);
        return appUserRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public AppUser findUserByEmailLookup(String email) throws ResourceNotFoundException {
        log.info("Fetching user with email {}.", email);
        AppUser appUser = appUserRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User doesn't exist."));

        AppUser appUserLookup = new AppUser(appUser.getId(), appUser.getFirstName(), appUser.getLastName(),
                appUser.getEmail(), null, null, null);

        return appUserLookup;
    }

    @Transactional(readOnly = true)
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

    /* #DELETE
    public Map<String, Object> loginUser(String email, String password) {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            if (auth.isAuthenticated()) {
                log.info("Logged In.");
                UserDetails userDetails = loadUserByUsername(email);
                String token = jwtTokenUtil.generateJwtToken(userDetails);
                responseMap.put("error", false);
                responseMap.put("message", "Logged In.");
                responseMap.put("token", token);
                responseMap.put("statusCode" , HttpStatus.OK);
                return responseMap;
            } else {
                responseMap.put("error", true);
                responseMap.put("message", "Invalid Credentials.");
                responseMap.put("statusCode" , HttpStatus.UNAUTHORIZED);
                return responseMap;
            }
        } catch (DisabledException e) {
            e.printStackTrace();
            responseMap.put("error", true);
            responseMap.put("message", "User is disabled.");
            responseMap.put("statusCode" , HttpStatus.INTERNAL_SERVER_ERROR);
            return responseMap;
        } catch (BadCredentialsException e) {
            responseMap.put("error", true);
            responseMap.put("message", "Invalid Credentials.");
            responseMap.put("statusCode" , HttpStatus.UNAUTHORIZED);
            return responseMap;
        } catch (Exception e) {
            e.printStackTrace();
            responseMap.put("error", true);
            responseMap.put("message", "Something went wrong.");
            responseMap.put("statusCode" , HttpStatus.INTERNAL_SERVER_ERROR);
            return responseMap;
        }
    }
    */

    public AppUser registerUser(AppUserRequestDtoV1 appUserDto) throws ConflictEntityException {
        boolean isExistingUser = appUserRepository.findByEmail(appUserDto.getEmail()).isPresent();

        if (isExistingUser) {
            throw new ConflictEntityException("User already exists.");
        }

        AppUser appUser = new AppUser();
        appUser.setFirstName(appUserDto.getFirstName());
        appUser.setLastName(appUserDto.getLastName());
        appUser.setEmail(appUserDto.getEmail());
        appUser.setPassword(passwordEncoder.encode(appUserDto.getPassword()));

        Role role = roleRepository.findByName("ROLE_USER");
        appUser.getRoles().add(role);

        appUserRepository.save(appUser);

        return appUser;
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
