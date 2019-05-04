package dk.kea.stud.biotrio.administration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class EmployeeController {

  @Autowired
  private EmployeeRepository employeeRepo;

  @GetMapping("/manage/employees")
    public String employees(Model m){
      List<Employee> employeeList = employeeRepo.findAllEmployees();
      m.addAttribute("employeelist", employeeList);

      return "employees/employees";
  }
  @GetMapping("/manage/employees/add")
  public String create(Model m) {
    m.addAttribute("employeeform", new Employee());

    return "employees/add-employee";
  }
  @PostMapping("/manage/employees/add")
  public String saveEmployee(@ModelAttribute Employee e) {
    employeeRepo.insert(e);
    // why "redirect:" and not just employees
    return "redirect:/manage/employees";
  }



}
