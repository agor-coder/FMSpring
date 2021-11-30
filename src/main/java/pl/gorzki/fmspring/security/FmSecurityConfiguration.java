package pl.gorzki.fmspring.security;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
@Profile("!test")
public class FmSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable();
        http
                .authorizeRequests()
                .mvcMatchers(HttpMethod.POST, "/users/notifier", "/login").permitAll()
                .mvcMatchers(HttpMethod.GET, "/areas/getAllTest").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);


    }

    @SneakyThrows
    private JasonUserNameAthenticationFilter authenticationFilter() {
        JasonUserNameAthenticationFilter filter = new JasonUserNameAthenticationFilter();
        filter.setAuthenticationManager(super.authenticationManager());
        return filter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("ass1@2.pl")
                .password("{noop}123")
                .roles("ASSIGNER")
                .and()
                .withUser("admin@2.pl")
                .password("{noop}123")
                .roles("ADMIN")
                .and()
                .withUser("notif1@2.pl")
                .password("{noop}123")
                .roles("NOTIFIER")
                .and()
                .withUser("notif2@2.pl")
                .password("{noop}123")
                .roles("NOTIFIER");
    }


}
