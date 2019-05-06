package dk.kea.stud.biotrio.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SecurityController {
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

  @GetMapping("/error")
  public String showError() {
    return "security/error";
  }
}
