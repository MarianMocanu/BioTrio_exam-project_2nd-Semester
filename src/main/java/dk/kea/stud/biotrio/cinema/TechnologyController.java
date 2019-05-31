package dk.kea.stud.biotrio.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Defines the routes related to {@link Technology} management
 */
@Controller
public class TechnologyController {
  @Autowired
  private TechnologyRepository technologyRepo;

  /**
   * Displays the technology list view, which also includes a small add form
   */
  @GetMapping("/manage/technologies")
  public String manageTechnologies(Model model) {
    model.addAttribute("technologies", technologyRepo.getAllTechnologies());
    return "theaters/technologies-manage";
  }

  /**
   * Saves the input data from the add form to the database if valid, then
   * then redirects the user back to the technology list view
   */
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

  /**
   * Deletes an technology entry from the database, then
   * redirects the user back to the technology list view
   */
  @PostMapping("/manage/technologies/delete")
  public String confirmDelete(@RequestParam(name = "techId") int id) {
    technologyRepo.deleteTechnology(id);
    return "redirect:/manage/technologies";
  }

}
