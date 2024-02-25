package pl.rengreen.taskmanager.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

        private final DataSource dataSource;
        private final BCryptPasswordEncoder bCryptPasswordEncoder;

        @Autowired
        public SecurityConfiguration(
                        BCryptPasswordEncoder bCryptPasswordEncoder, DataSource dataSource) {
                this.bCryptPasswordEncoder = bCryptPasswordEncoder;
                this.dataSource = dataSource;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
                http
                                .authorizeRequests()
                                .antMatchers("/register", "/", "/login", "/about", "/css/**", "/webjars/**",
                                                "/forgotPassword/**", "/forgot-password/**", "/reset-password/**")
                                .permitAll()
                                .antMatchers("/profile/**", "/tasks/**", "/task/**", "/users", "/user/**",
                                                "/changePassword", "/changePassword/**",
                                                "/h2-console/**", "/notes", "/notes/**",
                                                "/changePassword/changeUserPassword")
                                .hasAnyRole("USER, ADMIN,SUPERADMIN")
                                .antMatchers("/assignment/**")
                                .hasAnyRole("ADMIN,SUPERADMIN")
                                .antMatchers("/superadmin/**").hasRole("SUPERADMIN")
                                .and()
                                .formLogin()
                                .loginPage("/login")
                                .permitAll()
                                .defaultSuccessUrl("/profile", true)
                                .and()
                                .logout()
                                .logoutSuccessUrl("/login");

                http.csrf().ignoringAntMatchers("/h2-console/**", "/notes/**", "/forgot-password", "/reset-password");
                http.headers().frameOptions().sameOrigin();
        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
                auth.jdbcAuthentication()
                                .usersByUsernameQuery(
                                                "select email as principal, password as credentials, true from user where email=?")
                                .authoritiesByUsernameQuery(
                                                "select u.email as principal, r.role as role from user u inner join user_role ur on(u.user_id=ur.user_id) inner join role r on(ur.role_id=r.role_id) where u.email=?")
                                .dataSource(dataSource)
                                .passwordEncoder(bCryptPasswordEncoder)
                                .rolePrefix("ROLE_");
        }
}