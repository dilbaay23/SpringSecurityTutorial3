package com.moon.demo.security;

/**
 * Created by Moon on 12/9/2020
 */

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;


import static com.moon.demo.security.ApplicationUserRole.ADMIN;
import static com.moon.demo.security.ApplicationUserRole.ADMINTRAINEE;
import static com.moon.demo.security.ApplicationUserRole.STUDENT;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
// if we use annotations with method for permission, we should add this annotation and set prePostEnable=true
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // we choose this method to secure things
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http

                .csrf().disable()    // a service that is used by non-browser clients, you will likely want to disable CSRF protection. for browser users, it is recommended to be used csrf.
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()    //we add this paths into white list
                .antMatchers("/api/**").hasRole(STUDENT.name())
                .anyRequest()
                .authenticated()

                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/courses", true)


                .and()
                .rememberMe()                    // default to 2 weeks
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                .key("somethingverysecured")
                .userDetailsService(userDetailsServiceBean())

                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .logoutSuccessUrl("/login")
        ;

    }

    @Override
    @Bean
    public UserDetailsService userDetailsServiceBean() throws Exception {      // this is for how you retrieve your users from the database
        UserDetails moonUser = User.builder()
                .username("moon")
                .password(passwordEncoder.encode("password"))
                //              .roles(ApplicationUserRole.STUDENT.name())  //ROLE_STUDENT
                .authorities(STUDENT.getGrantedAuthorities())
                .build();

        UserDetails adminUser = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("password"))
                //               .roles(ApplicationUserRole.ADMIN.name())  //ROLE_ADMIN
                .authorities(ADMIN.getGrantedAuthorities())
                .build();

        UserDetails coUser = User.builder()
                .username("co")
                .password(passwordEncoder.encode("password"))
                //              .roles(ApplicationUserRole.ADMINTRAINEE.name())  //ROLE_ADMINTRAINEE
                .authorities(ADMINTRAINEE.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(moonUser, adminUser, coUser);    //this is a class (InMemoryUserDetailsManager) which implements UserDetailsService
    }
}
