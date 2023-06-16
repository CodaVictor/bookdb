package cz.upce.fei.bookdb_backend.controller;

import cz.upce.fei.bookdb_backend.config.JwtTokenUtil;
import cz.upce.fei.bookdb_backend.domain.AppUser;
import cz.upce.fei.bookdb_backend.dto.AppUserAuthenticationRequestDtoV1;
import cz.upce.fei.bookdb_backend.dto.AppUserRequestDtoV1;
import cz.upce.fei.bookdb_backend.service.AppUserService;
import cz.upce.fei.bookdb_backend.service.exception.ConflictEntityException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final AppUserService appUserService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity createAuthenticationRequest(@RequestBody AppUserAuthenticationRequestDtoV1 authenticationRequest) throws Exception {
        Map<String, Object> responseMap = new HashMap<>();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

            if (authentication.isAuthenticated()) {
                log.info("User logged In.");
                UserDetails userDetails = appUserService.loadUserByUsername(authenticationRequest.getUsername());
                String token = jwtTokenUtil.generateJwtToken(userDetails);
                List<String> roles = new ArrayList<>();
                for (GrantedAuthority authority : userDetails.getAuthorities()) {
                    String role = authority.getAuthority();
                    roles.add(role);
                }

                responseMap.put("error", false);
                responseMap.put("message", "User logged In.");
                responseMap.put("access_token", token);
                responseMap.put("roles", roles);
                return ResponseEntity.ok(responseMap);
            } else {
                responseMap.put("error", true);
                responseMap.put("message", "Invalid Credentials.");
                return ResponseEntity.status(401).body(responseMap);
            }
        } catch (DisabledException e) {
            e.printStackTrace();
            responseMap.put("error", true);
            responseMap.put("message", "User is disabled.");
            return ResponseEntity.status(500).body(responseMap);
        } catch (BadCredentialsException e) {
            responseMap.put("error", true);
            responseMap.put("message", "Invalid Credentials.");
            return ResponseEntity.status(401).body(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap.put("error", true);
            responseMap.put("message", "Something went wrong.");
            return ResponseEntity.status(500).body(responseMap);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AppUser> registerUser(@Validated @RequestBody AppUserRequestDtoV1 appUserDto)
            throws ConflictEntityException {
        AppUser appUser = appUserService.registerUser(appUserDto);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("").toUriString());
        return ResponseEntity.created(uri).body(appUser);
    }
}
