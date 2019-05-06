package dk.kea.stud.biotrio.administration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

    return "redirect:/manage/employees";
  }

  @GetMapping("/manage/employees/edit/{id}")
  public String update(@PathVariable(name = "id") int id, Model m) {
    m.addAttribute("employee", employeeRepo.findEmployee(id));

    return "employees/edit-employee";
  }

  @PostMapping("/manage/employees/edit")
  public String update(@ModelAttribute Employee e) {
    employeeRepo.update(e);

    return "redirect:/manage/employees";
  }

  //TODO are you sure you want to delete?
  @GetMapping("/manage/employees/delete/{id}")
  public String deleteEmployee(@PathVariable(name = "id") int id, Model m) {
    m.addAttribute("employee", employeeRepo.findEmployee(id));

    return "employees/delete-employee";
  }

  @PostMapping("/manage/employees/delete")
  public String deleteEmployee(int id) {
    employeeRepo.deleteEmployee(id);

    return "redirect:/manage/employees";
  }


}
