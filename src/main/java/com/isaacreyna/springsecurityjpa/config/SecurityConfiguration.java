package com.isaacreyna.springsecurityjpa.config;

import com.isaacreyna.springsecurityjpa.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/admin").hasRole("ADMIN")
            .antMatchers("/user", "/users").hasAnyRole("ADMIN")
            .antMatchers("/register").permitAll()
            .antMatchers("/profile").hasAnyRole("ADMIN", "USER")
            .antMatchers("/", "/demo/*", "/css/**", "/js/**", "/img/**").permitAll()
            .anyRequest().authenticated() // Force everything else to be authenticated, even if they do not exist
            //.and()
            //.exceptionHandling().accessDeniedHandler(accessDeniedHandler()) // See CustomAccessDeniedHandler.java
            .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
            .and()
                .logout()
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/login?logout")
                .permitAll();
            //.and().logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout");
    }

    // TODO: Implement password encoding
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }
}