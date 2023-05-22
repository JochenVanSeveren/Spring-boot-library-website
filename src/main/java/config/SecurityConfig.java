package config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().and()
                .authorizeHttpRequests(requests ->
                        requests
                                .requestMatchers("/login**").permitAll()
                                .requestMatchers("/css/**").permitAll()
                                .requestMatchers("/403**").permitAll()
                                .requestMatchers("/error/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/api/**").permitAll()
                                .requestMatchers("/bookDetails/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/addBook/**").hasAnyRole("ADMIN")
                                .requestMatchers("/toggleFavorite/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/*")
                                .access(new WebExpressionAuthorizationManager("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")))
                .formLogin(form ->
                        form.defaultSuccessUrl("/books", true)
                                .loginPage("/login")
                                .usernameParameter("username").passwordParameter("password"))
                .exceptionHandling().accessDeniedPage("/403");

        return http.build();
    }


}