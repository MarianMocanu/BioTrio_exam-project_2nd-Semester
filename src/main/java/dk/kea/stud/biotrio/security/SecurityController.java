package dk.kea.stud.biotrio.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Defines the Spring Security related routes
 */
@Controller
public class SecurityController {
  /**
   * Displays the custom login view
   */
  @GetMapping(value = "/login")
  public String showLogin(@RequestParam(value = "error", required = false) String error, String logout, Model model) {

    if (logout != null) {
      model.addAttribute("logoutmsg", "You've been logged out Successfully");
    }

    if (error == null || !error.isEmpty()) {
      model.addAttribute("error", error);
    }

    return "security/login";
  }
}