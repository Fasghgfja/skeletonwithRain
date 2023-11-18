package at.qe.skeleton.configs;

import javax.sql.DataSource;

import at.qe.skeleton.model.UserRole;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring configuration for web security.
 */
@Configuration
@EnableWebSecurity()
public class WebSecurityConfig {

    private static final String ADMIN = UserRole.ADMIN.name();
    private static final String USER = UserRole.USER.name();

    private static final String LOGIN = "/login.xhtml";
    private static final String ACCESSDENIED = "/error/access_denied.xhtml";

    @Autowired
    DataSource dataSource;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        try {
            http
                    .csrf(csrf -> csrf.disable());
            http.headers().frameOptions().disable(); // needed for H2 console
                    http
                    .authorizeHttpRequests(authorize -> authorize
                            .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/registration/**")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/**.jsf")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/jakarta.faces.resource/**")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/error/**")).permitAll()
                            // Only access with higher autority role
                            .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasAnyAuthority("ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/secured/**")).hasAnyAuthority(ADMIN, USER)
                            .anyRequest().authenticated()
                    )
                    .formLogin(form -> form
                            .loginPage(LOGIN)
                            .permitAll()
                            .defaultSuccessUrl("/dashboard.xhtml")
                            .loginProcessingUrl("/login")
                            .successForwardUrl("/dashboard.xhtml")
                            .failureUrl("/login.xhtml?error=true")
                    )
                    .logout(logout -> logout
                            .logoutSuccessUrl(LOGIN)
                            .deleteCookies("JSESSIONID")
                            .invalidateHttpSession(true)
                            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    )
                    .sessionManagement(session -> session
                            .invalidSessionUrl("/error/invalid_session.xhtml")
                    );

            return http.build();
        } catch (Exception ex) {
            throw new BeanCreationException("Wrong spring security configuration", ex);
        }

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //Configure roles and passwords via datasource
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from userx where username=?")
                .authoritiesByUsernameQuery("select userx_username, roles from userx_user_role where userx_username=?")
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}