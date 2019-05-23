package dk.kea.stud.biotrio.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TechnologyController {
  @Autowired
  private TechnologyRepository technologyRepo;

  @GetMapping("/manage/technologies")
  public String manageTechnologies(Model model) {
    model.addAttribute("technologies", technologyRepo.getAllTechnologies());
    return "theaters/technologies-manage";
  }

  @PostMapping("/manage/technologies/add")
  public String saveTechnology(@RequestParam String techName, Model model) {
    List<Technology> technologies = technologyRepo.getAllTechnologies();
    String message = null;
    boolean success;

    // Check if the input technology name already exists
    boolean found = false;
    if (technologies != null) {
      for (Technology technology : technologies) {
        if (technology.getName().toLowerCase().equals(techName.toLowerCase())) {
          found = true;
          break;
        }
      }
    }

    if (found) {
      message = "Error adding technology. The name already exists.";
      success = false;
    } else {
      technologyRepo.addTechnology(techName);
      message = "Technology successfully added to the list.";
      success = true;
    }

    model.addAttribute("technologies", technologyRepo.getAllTechnologies());
    model.addAttribute("success", success);
    model.addAttribute("message", message);
    return "theaters/technologies-manage";
  }

  @PostMapping("/manage/technologies/delete")
  public String confirmDelete(@RequestParam int techId, Model model) {
    model.addAttribute("tech", technologyRepo.getTechnologyById(techId));
    return "theaters/technologies-delete";
  }

  @PostMapping("/manage/technologies/delete/confirmed")
  public String deleteTechnology(@RequestParam int techId) {
    technologyRepo.deleteTechnology(techId);
    return "redirect:/manage/technologies";
  }
}
