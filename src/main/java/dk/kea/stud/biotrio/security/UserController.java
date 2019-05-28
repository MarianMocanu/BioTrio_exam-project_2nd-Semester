package dk.kea.stud.biotrio.security;

import dk.kea.stud.biotrio.administration.Employee;
import dk.kea.stud.biotrio.administration.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Defines the routes related to {@link User} management
 */
@Controller
public class UserController {
  @Autowired
  private UserRepository userRepo;
  @Autowired
  private EmployeeRepository employeeRepo;

  /**
   * Displays the user list view
   */
  @GetMapping("/manage/users")
  public String viewUsers(Model model) {
    model.addAttribute("users", userRepo.getAllUsers());
    return "security/users-view";
  }

  /**
   * Displays the add user view
   */
  @GetMapping("/manage/users/add")
  public String addUser(Model model) {
    model.addAttribute("employees", employeeRepo.findAllEmployeesWithoutAccount());
    model.addAttribute("roles", userRepo.getAllRoles());
    model.addAttribute("userData", new User());
    return "security/users-add";
  }

  /**
   * Saves the data from the add user view to the database, and displays
   * a view with the message resulting from the operation's success status
   */
  @PostMapping("/manage/users/add")
  public String addUserResult(@ModelAttribute User userData,
                              @RequestParam String confPassword,
                              @RequestParam(value = "employee_id") int employee,
                              Model model) {
    String message = null;
    boolean success = false;
    if (userRepo.findByUsername(userData.getUsername()) != null) {
      // The username must be unique
      message = "Username taken. Choose something different.";
    } else if (!confPassword.equals(userData.getPassword())) {
      // The password must match
      message = "Passwords don't match. Try again.";
    } else {
      // Sets the associated Employee object according to
      // the selection made in the view
      if (employee == 0) {
        userData.setEmployee(null);
      } else {
        userData.setEmployee(employeeRepo.findEmployee(employee));
      }
      userRepo.addUser(userData);
      message = "User successfully added.";
      success = true;
    }

    model.addAttribute("message", message);
    model.addAttribute("success", success);
    return "security/users-add-result";
  }

  /**
   * Deletes a user record from the database, then redirects the user
   * to the user list view
   */
  @PostMapping("/manage/users/delete")
  public String doDelete(@RequestParam(name = "userId") int id) {
    userRepo.deleteUser(id);
    return "redirect:/manage/users";
  }

  /**
   * Displays the edit user view
   */
  @GetMapping("/manage/users/edit/{id}")
  public String editUser(@PathVariable("id") int id, Model model) {
    User userData = userRepo.findById(id);
    List<Employee> employeeData = employeeRepo.findAllEmployeesWithoutAccount();
    if (userData.getEmployee() != null) {
      employeeData.add(0, userData.getEmployee());
    }
    model.addAttribute("employeeData", employeeData);
    model.addAttribute("userData", userData);
    model.addAttribute("roles", userRepo.getAllRoles());

    return "security/users-edit";
  }

  /**
   * If all the data is valid, saves the updated user credentials to the
   * database, and redirects the user to the user list view. Otherwise
   * displays the user edit view again, with an appropriate error message
   */
  @PostMapping("/manage/users/edit/{id}")
  public String doEditUser(@PathVariable("id") int id,
                           @ModelAttribute User userData,
                           @RequestParam String confPassword,
                           @RequestParam(value = "employee_id") int employee,
                           Model model) {

    String message = null;
    User oldUserData = userRepo.findById(id);

    // If the username has changed, check if the new one is not already taken
    if (!userData.getUsername().equals(oldUserData.getUsername())) {
      if (userRepo.findByUsername(userData.getUsername()) != null) {
        message = "Username taken. Try something else.";
        model.addAttribute("userData", oldUserData);
        model.addAttribute("roles", userRepo.getAllRoles());
        model.addAttribute("error", message);
        return "security/users-edit";
      }
    }

    // If the password is changed, check to see if the two input fields match
    if (!userData.getPassword().isEmpty() && !confPassword.isEmpty()) {
      if (!userData.getPassword().equals(confPassword)) {
        message = "New passwords didn't match. Try again.";
        model.addAttribute("userData", oldUserData);
        model.addAttribute("roles", userRepo.getAllRoles());
        model.addAttribute("error", message);
        return "security/users-edit";
      }
    } else {
      // If the password attribute of the User object is null, the update
      // method in the repository will not change the password
      userData.setPassword(null);
    }

    // Set the associated employee data to what the user selected in the edit view
    if (employee == 0) {
      userData.setEmployee(null);
    } else {
      userData.setEmployee(employeeRepo.findEmployee(employee));
    }

    userRepo.editUser(userData);
    return "redirect:/manage/users";
  }
}
