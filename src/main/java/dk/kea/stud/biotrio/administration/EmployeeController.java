package dk.kea.stud.biotrio.administration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class EmployeeController {

  @Autowired
  private EmployeeRepository employeeRepo;

  /**
   * Displays the employee list view
   */
  @GetMapping("/manage/employees")
  public String employees(Model m) {
    List<Employee> employeeList = employeeRepo.findAllEmployees();
    m.addAttribute("employeelist", employeeList);

    return "employees/employees-view";
  }

  /**
   * Displays the add employee view
   */
  @GetMapping("/manage/employees/add")
  public String create(Model m) {
    m.addAttribute("employeeform", new Employee());

    return "employees/employees-add";
  }

  /**
   * Saves the data received from the add employee view to the database,
   * then redirects the user back to the employee list view
   */
  @PostMapping("/manage/employees/add")
  public String saveEmployee(@ModelAttribute Employee e) {
    employeeRepo.insert(e);

    return "redirect:/manage/employees";
  }

  /**
   * Displays the edit employee view
   */
  @GetMapping("/manage/employees/edit/{id}")
  public String update(@PathVariable(name = "id") int id, Model m) {
    m.addAttribute("employee", employeeRepo.findEmployee(id));

    return "employees/employees-edit";
  }


  /**
   * Updates the employee record in the database with the data received from
   * the edit employee view, then redirects the user back to the employee list view
   */
  @PostMapping("/manage/employees/edit")
  public String update(@ModelAttribute Employee e) {
    employeeRepo.update(e);

    return "redirect:/manage/employees";
  }

  /**
   * Deletes an employee record from the database, then redirects the user back
   * to the employee list view
   */
  @PostMapping("/manage/employees/delete")
  public String deleteEmployee(@RequestParam(name = "employeeId") int id) {
    employeeRepo.deleteEmployee(id);

    return "redirect:/manage/employees";
  }


}
