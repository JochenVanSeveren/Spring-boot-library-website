package config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    DataSource dataSource;

    //    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(new BCryptPasswordEncoder());
//    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth.inMemoryAuthentication()
                .withUser("user").password(encoder.encode("user")).roles("USER").and()
                .withUser("admin").password(encoder.encode("admin")).roles("USER", "ADMIN");
    }

//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf().and()
//                .authorizeHttpRequests(requests ->
//                        requests.requestMatchers("/*").hasRole("USER"))
//                .httpBasic();
//
//        return http.build();
//    }

//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf().and()
//                .authorizeHttpRequests(requests -> requests.requestMatchers("/login**").permitAll()
//                        .requestMatchers("/css/**").permitAll().requestMatchers("/403**").permitAll()
//                        .requestMatchers("/bibliotheek/saveBook")
//                        .access(new WebExpressionAuthorizationManager("hasRole('ROLE_ADMIN')")).requestMatchers("/*")
//                        .access(new WebExpressionAuthorizationManager("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')"))
//                        .requestMatchers("/*/*").permitAll())
//                .formLogin(form -> form.defaultSuccessUrl("/bibliotheek", true).loginPage("/login")
//                        .usernameParameter("username").passwordParameter("password"))
//                .exceptionHandling().accessDeniedPage("/403");
//
//        return http.build();
//    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().and()
                .authorizeHttpRequests(requests ->
                        requests
                                .requestMatchers("/login**").permitAll()
                                .requestMatchers("/css/**").permitAll()
                                .requestMatchers("/403**").permitAll()
                                .requestMatchers("/api/**").permitAll()
                                .requestMatchers("/bookDetails/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/addBook/**").hasAnyRole("ADMIN")
                                .requestMatchers("/*")
                                .access(new WebExpressionAuthorizationManager("hasRole('ROLE_USER')")))
                .formLogin(form ->
                        form.defaultSuccessUrl("/books", true)
                                .loginPage("/login")
                                .usernameParameter("username").passwordParameter("password"))
                .exceptionHandling().accessDeniedPage("/403");

        return http.build();
    }

}