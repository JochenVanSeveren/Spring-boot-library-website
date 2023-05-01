package com.hogent.g2a1_vanseveren_jochen;

import domain.BoekService;
import domain.BoekServiceImpl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class G2A1VanSeverenJochenApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(G2A1VanSeverenJochenApplication.class, args);
    }

    @Bean
    BoekService boekService() {
        return new BoekServiceImpl();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/hello");
    }

}
