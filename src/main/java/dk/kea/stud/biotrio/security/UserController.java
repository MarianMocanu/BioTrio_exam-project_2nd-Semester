package dk.kea.stud.biotrio.security;

import dk.kea.stud.biotrio.administration.Employee;
import dk.kea.stud.biotrio.administration.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {
  @Autowired
  private UserRepository userRepo;
  @Autowired
  private EmployeeRepository employeeRepo;

  @GetMapping("/manage/users")
  public String viewUsers(Model model) {
    model.addAttribute("users", userRepo.getAllUsers());
    return "security/users-view";
  }

  @GetMapping("/manage/users/add")
  public String addUser(Model model) {
    model.addAttribute("employees", employeeRepo.findAllEmployeesWithoutAccount());
    model.addAttribute("roles", userRepo.getAllRoles());
    model.addAttribute("userData", new User());
    return "security/users-add";
  }

  @PostMapping("/manage/users/add")
  public String addUserResult(@ModelAttribute User userData,
                              @RequestParam String confPassword,
                              @RequestParam(value = "employee_id") int employee,
                              Model model) {
    String message = null;
    boolean success = false;
    if (userRepo.findByUsername(userData.getUsername()) != null) {
      message = "Username taken. Choose something different.";
    } else if (!confPassword.equals(userData.getPassword())) {
      message = "Passwords don't match. Try again.";
    } else {
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

  @PostMapping("/manage/users/delete")
  public String doDelete(@RequestParam(name = "userId") int id) {
    userRepo.deleteUser(id);
    return "redirect:/manage/users";
  }

  @GetMapping("/manage/users/edit/{id}")
  public String editUser(@PathVariable("id") int id, Model model) {
    User userData = userRepo.findById(id);
    List<Employee> employeeData = employeeRepo.findAllEmployeesWithoutAccount();
    System.out.println(userData.getEmployee());
    if (userData.getEmployee() != null) {
      System.out.println("inserting employee id: " + userData.getEmployee().getId());
      employeeData.add(0, userData.getEmployee());
    }
    model.addAttribute("employeeData", employeeData);
    model.addAttribute("userData", userData);
    model.addAttribute("roles", userRepo.getAllRoles());

    return "security/users-edit";
  }

  @PostMapping("/manage/users/edit/{id}")
  public String doEditUser(@PathVariable("id") int id,
                           @ModelAttribute User userData,
                           @RequestParam String confPassword,
                           @RequestParam(value = "employee_id") int employee,
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
    if (employee == 0) {
      userData.setEmployee(null);
    } else {
      userData.setEmployee(employeeRepo.findEmployee(employee));
    }
    userRepo.editUser(userData);
    return "redirect:/manage/users";
  }
}
