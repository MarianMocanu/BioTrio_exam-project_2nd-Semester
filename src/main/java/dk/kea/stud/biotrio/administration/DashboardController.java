package dk.kea.stud.biotrio.administration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

  @GetMapping("/manage")
  public String showManagementDashboard() {
    return "manage/dashboard";
  }
}
