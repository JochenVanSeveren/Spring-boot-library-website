package com.hogent.g2a1_vanseveren_jochen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import service.*;

import java.util.Properties;

@SpringBootApplication
public class G2A1VanSeverenJochenApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(G2A1VanSeverenJochenApplication.class, args);
    }

    @Bean
    BookService bookService() {
        return new BookServiceImpl();
    }

    @Bean
    AuthorService authorService() {
        return new AuthorServiceImpl();
    }

    @Bean
    LocationService locationService() {
        return new LocationServiceImpl();
    }

    @Bean
    public UserService userService() {
        return new UserServiceImpl();
    }

//    @Bean
//    public FavoritesService favoritesService() {
//        return new FavoritesServiceImpl();
//    }

//        @Bean
//        public LocaleResolver localeResolver() {
//            SessionLocaleResolver slr = new SessionLocaleResolver();
//            slr.setDefaultLocale(new Locale("nl", "BE"));
//            return slr;
//        }

//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addRedirectViewController("/", "/boeken");
//    }

//    @Bean
//    SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
//        SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
//
//        Properties mappings = new Properties();
//        mappings.put("java.lang.Exception", "error");
//        resolver.setExceptionMappings(mappings);
//        resolver.setDefaultErrorView("error/error");
//        return resolver;
//    }

}
