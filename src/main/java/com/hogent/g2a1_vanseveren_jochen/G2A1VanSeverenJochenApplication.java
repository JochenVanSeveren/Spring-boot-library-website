package com.hogent.g2a1_vanseveren_jochen;

import exception.GenericException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;
import java.util.Properties;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"repository"})
@EntityScan(basePackages = {"domain"})
@ComponentScans({
//        @ComponentScan("service"),
        @ComponentScan("domain"),
        @ComponentScan("repository"),
        @ComponentScan("exception"),
        @ComponentScan("config"),
        @ComponentScan("validation"),
        @ComponentScan("rest")
})
public class G2A1VanSeverenJochenApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(G2A1VanSeverenJochenApplication.class, args);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/books");
    	    registry.addViewController("/403").setViewName("auth/403");

    }

    @Bean
    SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();

        Properties mappings = new Properties();
        mappings.put("java.lang.Exception", "error/error");
        mappings.put("java.lang.RuntimeException", "error/error");
        resolver.setExceptionMappings(mappings);
        resolver.setDefaultErrorView("error/error");
        return resolver;
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.forLanguageTag("nl-BE"));
//        localeResolver.setDefaultLocale(Locale.ENGLISH);
        return localeResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @ExceptionHandler(GenericException.class)
    public ModelAndView handleException(Exception ex) {
        ModelAndView model
                = new ModelAndView("error/error");
        model.addObject("errMsg", ex.getMessage());
        return model;
    }



}
