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

  @PostMapping("/manage/users/delete")
  public String doDelete(@RequestParam(name = "userId") int id) {
    userRepo.deleteUser(id);
    return "redirect:/manage/users";
  }

  @GetMapping("/manage/users/edit/{id}")
  public String editUser(@PathVariable("id") int id, Model model) {
    model.addAttribute("userData", userRepo.findById(id));
    model.addAttribute("roles", userRepo.getAllRoles());

    return "security/users-edit";
  }

  // TODO this shit doesn't work as it should; needs to be rewritten.
  @PostMapping("/manage/users/edit/{id}")
  public String doEditUser(@PathVariable("id") int id,
                           @ModelAttribute User userData,
                           @RequestParam String confPassword,
                           Model model) {

    String message = null;
    User oldUserData = userRepo.findById(id);
    if (!userData.getUsername().equals(oldUserData.getUsername())) {
      if (userRepo.findByUsername(userData.getUsername()) != null) {
        message = "Username taken. Try something else.";
        model.addAttribute("userData", oldUserData);
        model.addAttribute("roles", userRepo.getAllRoles());
        model.addAttribute("error", message);
        return "security/users-edit";
      }
    }

    if (!userData.getPassword().isEmpty() && !confPassword.isEmpty()) {
      if (!userData.getPassword().equals(confPassword)) {
        message = "New passwords didn't match. Try again.";
        model.addAttribute("userData", oldUserData);
        model.addAttribute("roles", userRepo.getAllRoles());
        model.addAttribute("error", message);
        return "security/users-edit";
      }
    } else {
      userData.setPassword(null);
    }

    userRepo.editUser(userData);
    return "redirect:/manage/users";
  }
}
