package cz.upce.fei.bookdb_backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpMethod.*;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final JwtRequestFilter jwtRequestFilter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests().antMatchers("/auth/**").permitAll()
                //.anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
                    Map<String, Object> responseMap = new HashMap<>();
                    ObjectMapper mapper = new ObjectMapper();
                    response.setStatus(401);
                    responseMap.put("error", true);
                    responseMap.put("message", "Unauthorized.");
                    response.setHeader("content-type", "application/json");
                    String responseMsg = mapper.writeValueAsString(responseMap);
                    response.getWriter().write(responseMsg);
                })
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .cors();

        // User and role
        httpSecurity.authorizeRequests().antMatchers(GET, "/users").hasAnyAuthority("ROLE_ADMIN");
        httpSecurity.authorizeRequests().antMatchers(POST, "/user" + "/info").hasAnyAuthority("ROLE_ADMIN", "ROLE_EDITOR", "ROLE_USER");
        httpSecurity.authorizeRequests().antMatchers(POST, "/user" + "/save/**").hasAnyAuthority("ROLE_ADMIN");
        httpSecurity.authorizeRequests().antMatchers(POST, "/role" + "/**").hasAnyAuthority("ROLE_ADMIN");

        // Books
        httpSecurity.authorizeRequests().antMatchers(POST, "/books").hasAnyAuthority("ROLE_EDITOR", "ROLE_ADMIN"); // Create
        httpSecurity.authorizeRequests().antMatchers(PUT, "/books/{bookId}").hasAnyAuthority("ROLE_EDITOR", "ROLE_ADMIN"); // Update
        httpSecurity.authorizeRequests().antMatchers(DELETE, "/books/{bookId}").hasAnyAuthority("ROLE_EDITOR", "ROLE_ADMIN"); // Delete

        // Book reviews
        httpSecurity.authorizeRequests().antMatchers(POST, "/reviews/{bookId}").hasAnyAuthority("ROLE_USER", "ROLE_EDITOR", "ROLE_ADMIN");
        httpSecurity.authorizeRequests().antMatchers(PUT, "/reviews/{bookId}/{reviewId}").hasAnyAuthority("ROLE_USER", "ROLE_EDITOR", "ROLE_ADMIN");
        httpSecurity.authorizeRequests().antMatchers(DELETE, "/reviews/{bookId}/{reviewId}").hasAnyAuthority("ROLE_USER", "ROLE_EDITOR", "ROLE_ADMIN");

        // Authors
        httpSecurity.authorizeRequests().antMatchers(POST, "/authors").hasAnyAuthority("ROLE_EDITOR", "ROLE_ADMIN");
        httpSecurity.authorizeRequests().antMatchers(PUT, "/authors/{authorId}").hasAnyAuthority("ROLE_EDITOR", "ROLE_ADMIN");
        httpSecurity.authorizeRequests().antMatchers(DELETE, "/authors/{authorId}").hasAnyAuthority("ROLE_EDITOR", "ROLE_ADMIN");

        // Genres
        httpSecurity.authorizeRequests().antMatchers(POST, "/genres").hasAnyAuthority("ROLE_EDITOR", "ROLE_ADMIN");
        httpSecurity.authorizeRequests().antMatchers(PUT, "/genres/{genreId}").hasAnyAuthority("ROLE_EDITOR", "ROLE_ADMIN");
        httpSecurity.authorizeRequests().antMatchers(DELETE, "/genres/{genreId}").hasAnyAuthority("ROLE_EDITOR", "ROLE_ADMIN");

        // Categories
        httpSecurity.authorizeRequests().antMatchers(POST, "/categories").hasAnyAuthority("ROLE_EDITOR", "ROLE_ADMIN");
        httpSecurity.authorizeRequests().antMatchers(PUT, "/categories/{categoryId}").hasAnyAuthority("ROLE_EDITOR", "ROLE_ADMIN");
        httpSecurity.authorizeRequests().antMatchers(DELETE, "/categories/{categoryId}").hasAnyAuthority("ROLE_EDITOR", "ROLE_ADMIN");

        // Publishers
        httpSecurity.authorizeRequests().antMatchers(POST, "/publishers").hasAnyAuthority("ROLE_EDITOR", "ROLE_ADMIN");
        httpSecurity.authorizeRequests().antMatchers(PUT, "/publishers/{publisherId}").hasAnyAuthority("ROLE_EDITOR", "ROLE_ADMIN");
        httpSecurity.authorizeRequests().antMatchers(DELETE, "/publishers/{publisherId}").hasAnyAuthority("ROLE_EDITOR", "ROLE_ADMIN");
    }
}