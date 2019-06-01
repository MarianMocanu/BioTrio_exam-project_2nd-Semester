package dk.kea.stud.biotrio.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a way for Spring Security to authenticate users
 */
@Configuration
public class CustomAuthentication implements AuthenticationProvider {

  @Autowired
  private UserRepository userRepo;

  /**
   * Overrides the default Spring Security authenticate method to provide
   * a way to authenticate users based on our custom users table structure
   *
   * @param auth The credentials received from the login form
   * @return An AuthenticationToken on success
   * @throws AuthenticationException if the input credentials don't match any from the database
   */
  @Override
  public Authentication authenticate(Authentication auth)
      throws AuthenticationException {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    // Extract the username and password from the credentials, as strings
    String username = auth.getName();
    String password = auth.getCredentials().toString();

    // Try to find the input username in the database
    User user = userRepo.findByUsername(username);

    if (user == null) {
      throw new BadCredentialsException("Username Not Found");
    }

    // If the user is found, check the input password against the user's associated
    // password in the database
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new BadCredentialsException("Username Or Password Is invalid");
    }

    // If the passwords match, get the user's role, and finally create
    // and return the AuthenticationToken
    List<SimpleGrantedAuthority> role = new ArrayList<>();
    if (user.getRole() != null) {
      role.add(new SimpleGrantedAuthority(user.getRole()));
    }

    return new UsernamePasswordAuthenticationToken(username, password, role);
  }

  @Override
  public boolean supports(Class<?> arg0) {
    return true;
  }

}