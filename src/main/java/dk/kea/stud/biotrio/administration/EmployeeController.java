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
  //Lists all Employees, "add", "edit", "delete" button available
  @GetMapping("/manage/employees")
  public String employees(Model m) {
    List<Employee> employeeList = employeeRepo.findAllEmployees();
    m.addAttribute("employeelist", employeeList);

    return "employees/employees-view";
  }
  //Presents form to add Employee
  @GetMapping("/manage/employees/add")
  public String create(Model m) {
    m.addAttribute("employeeform", new Employee());

    return "employees/employees-add";
  }
  //Saves Employee and lists all the Employees
  @PostMapping("/manage/employees/add")
  public String saveEmployee(@ModelAttribute Employee e) {
    employeeRepo.insert(e);

    return "redirect:/manage/employees";
  }
  //Presents form to edit Employee
  @GetMapping("/manage/employees/edit/{id}")
  public String update(@PathVariable(name = "id") int id, Model m) {
    m.addAttribute("employee", employeeRepo.findEmployee(id));

    return "employees/employees-edit";
  }
  //Saves edited Employee and lists all the Employees
  @PostMapping("/manage/employees/edit")
  public String update(@ModelAttribute Employee e) {
    employeeRepo.update(e);

    return "redirect:/manage/employees";
  }

  //Deletes Employee and lists all the Employees
  @PostMapping("/manage/employees/delete")
  public String deleteEmployee(@RequestParam(name = "employeeId") int id) {
    employeeRepo.deleteEmployee(id);

    return "redirect:/manage/employees";
  }


}
