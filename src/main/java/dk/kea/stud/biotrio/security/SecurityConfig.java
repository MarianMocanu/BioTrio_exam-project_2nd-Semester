package dk.kea.stud.biotrio.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * The Spring Security settings for the web application. This is where
 * routes within it can be locked down based on different rules, such as
 * user authorization and user authentication
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private CustomAuthentication customAuthentication;

  /**
   * Configures the rules for the different routes, and other security related settings
   */
  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.csrf().disable();

    http.authorizeRequests().antMatchers("/**").permitAll();

    http.authorizeRequests().and().formLogin().loginProcessingUrl("/login").loginPage("/login")
        .defaultSuccessUrl("/").failureUrl("/login?error=true")
        .usernameParameter("username").passwordParameter("password")
        .and()
        .logout().logoutSuccessUrl("/login?logout");

    //        http.authorizeRequests().antMatchers("/**").authenticated();
    //
    //        http.authorizeRequests().antMatchers("/admin/users/**").hasRole("ADMIN");

    http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/accessdenied");
  }

  /**
   * Tells Spring Security how to authenticate users. In our case we
   * configure it to use the {@link CustomAuthentication} we wrote
   */
  @Autowired
  public void configure(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(customAuthentication);
  }
}