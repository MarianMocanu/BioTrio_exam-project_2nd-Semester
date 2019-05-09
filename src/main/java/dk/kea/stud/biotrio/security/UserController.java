package dk.kea.stud.biotrio.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
  @Autowired
  private UserRepository userRepo;

  @GetMapping("/manage/users")
  public String viewUsers(Model model) {
    model.addAttribute("users", userRepo.getAllUsers());
    return "security/users-view";
  }

  @GetMapping("/manage/users/add")
  public String addUser(Model model) {
    model.addAttribute("roles", userRepo.getAllRoles());
    model.addAttribute("userData", new User());
    return "security/users-add";
  }

  @PostMapping("/manage/users/add")
  public String addUserResult(@ModelAttribute User userData,
                              @RequestParam String confPassword,
                              Model model) {
    String message = null;
    boolean success = false;
    if (userRepo.findByUsername(userData.getUsername()) != null) {
      message = "Username taken. Choose something different.";
    } else if (!confPassword.equals(userData.getPassword())) {
      message = "Passwords don't match. Try again.";
    } else {
      userRepo.addUser(userData);
      message = "User successfully added.";
      success = true;
    }

    model.addAttribute("message", message);
    model.addAttribute("success", success);
    return "security/users-add-result";
  }

  @GetMapping("/manage/users/delete/{id}")
  public String deleteUser(@PathVariable("id") int id, Model model) {
    model.addAttribute("user", userRepo.findById(id));
    return "security/users-delete";
  }

  @PostMapping("/manage/users/delete")
  public String doDelete(@ModelAttribute User user) {
    userRepo.deleteUser(user.getId());
    return "redirect:/manage/users";
  }
}
