package com.moon.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by Moon on 12/9/2020
 */
@Configuration
public class PasswordConfig {


    @Bean
    public PasswordEncoder passwordEncoder(){    //This PasswordEncoder is an interface of Spring
        return new BCryptPasswordEncoder(10);
    }
}
