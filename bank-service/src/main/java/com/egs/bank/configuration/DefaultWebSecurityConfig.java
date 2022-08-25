package com.egs.bank.configuration;

import com.egs.bank.configuration.resources.ApplicationProperties;
import com.egs.bank.intrface.ITokenGranter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class DefaultWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ITokenGranter tokenGranter;
    @Autowired
    private ApplicationProperties applicationProperties;
    @SneakyThrows
    @Override
    protected void configure(HttpSecurity http) {
        http.csrf().disable()
                .antMatcher("/api/**").addFilterAfter(new JwtFilter(tokenGranter,applicationProperties), BasicAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/public/**",
                "/actuator/**",
                "/error",
                "/v2/api-docs",
                "/configuration/security",
                "/swagger-resources/**",
                "/swagger-ui/**",
                "/webjars/**");
    }

}