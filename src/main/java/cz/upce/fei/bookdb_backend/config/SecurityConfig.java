package cz.upce.fei.bookdb_backend.config;

import cz.upce.fei.bookdb_backend.values.ServerPaths;
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

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final HashUtils hashUtils;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Zde záleží na pořadí konfigurace
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean(), hashUtils);
        customAuthenticationFilter.setFilterProcessesUrl(ServerPaths.LOGIN_PATH);

        http.csrf().disable(); // Disable stateful policy (which uses session and cookies)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers(ServerPaths.LOGIN_PATH + "/**", ServerPaths.TOKEN_REFRESH_PATH + "/**")
                .permitAll();
        // User and role
        http.authorizeRequests().antMatchers(GET, ServerPaths.USERS_PATH).hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST, ServerPaths.USER_PATH + "/save/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST, ServerPaths.ROLE_PATH + "/**").hasAnyAuthority("ROLE_ADMIN");

        // Books
        http.authorizeRequests().antMatchers(POST, "/books").hasAnyAuthority("ROLE_EDITOR", "ROLE_ADMIN"); // Create
        http.authorizeRequests().antMatchers(PUT, "/books/{bookId}").hasAnyAuthority("ROLE_EDITOR", "ROLE_ADMIN"); // Update
        http.authorizeRequests().antMatchers(DELETE, "/books/{bookId}").hasAnyAuthority("ROLE_EDITOR", "ROLE_ADMIN"); // Delete

        // Book reviews
        http.authorizeRequests().antMatchers(POST, "/reviews/{bookId}").hasAnyAuthority("ROLE_USER", "ROLE_EDITOR", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers(PUT, "/reviews/{bookId}/{reviewId}").hasAnyAuthority("ROLE_USER", "ROLE_EDITOR", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/reviews/{bookId}/{reviewId}").hasAnyAuthority("ROLE_USER", "ROLE_EDITOR", "ROLE_ADMIN");

        // Authors
        http.authorizeRequests().antMatchers(POST, "/authors").hasAnyAuthority("ROLE_EDITOR", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers(PUT, "/authors/{authorId}").hasAnyAuthority("ROLE_EDITOR", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/authors/{authorId}").hasAnyAuthority("ROLE_EDITOR", "ROLE_ADMIN");

        // Genres
        http.authorizeRequests().antMatchers(POST, "/genres").hasAnyAuthority("ROLE_EDITOR", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers(PUT, "/genres/{genreId}").hasAnyAuthority("ROLE_EDITOR", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/genres/{genreId}").hasAnyAuthority("ROLE_EDITOR", "ROLE_ADMIN");

        //http.authorizeRequests().anyRequest().authenticated(); // Turn on authentication for every endpoint

        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(hashUtils), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
