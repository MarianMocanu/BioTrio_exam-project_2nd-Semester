package dk.kea.stud.biotrio.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TheaterController {

  @Autowired
  private TheaterRepository theaterRepo;
  @Autowired
  private TechnologyRepository technologyRepo;

  //Lists all Theaters, "add", "edit", "delete" button available
  @GetMapping("/manage/theaters")
  public String theaters(Model m) {
    List<Theater> theaterList = theaterRepo.findAllTheaters();
    m.addAttribute("theaterlist", theaterList);

    return "theaters/theaters-view";
  }

  //Presents form to add Theater
  @GetMapping("/manage/theaters/add")
  public String create(Model m) {
    m.addAttribute("technologies", technologyRepo.getAllTechnologies());
    m.addAttribute("theaterform", new Theater());

    return "theaters/theaters-add";
  }

  //Saves Theater and lists all the Theaters
  @PostMapping("/manage/theaters/add")
  public String saveTheater(@ModelAttribute Theater t,
                            @RequestParam(value="selectedTechnologies", required = false)
                                List<Integer> selectedTechnologies) {
    t.setSupportedTechnologies(technologyRepo.convertFromIdList(selectedTechnologies));
    theaterRepo.insert(t);
    // TODO discuss this: why "redirect:" and not just theaters:
    // because after posting we need @GetMapping to be able to read what's on /theaters
    return "redirect:/manage/theaters";
  }

  //Presents form to edit Theater
  @GetMapping("/manage/theaters/edit/{id}")
  public String update(@PathVariable(name = "id") int id, Model m) {
    Theater currentTheater = theaterRepo.findTheater(id);
    m.addAttribute("technologies", technologyRepo.getAllTechnologies());
    m.addAttribute("theater", currentTheater);
    m.addAttribute("selectedTechnologies",
        technologyRepo.convertToIdList(currentTheater.getSupportedTechnologies()));

    return "theaters/theaters-edit";
  }

  //Saves edited Theater and lists all the Theaters
  @PostMapping("/manage/theaters/edit")
  public String update(@ModelAttribute Theater t,
                       @RequestParam(value="selectedTechnologies", required = false)
                           List<Integer> selectedTechnologies) {
    t.setSupportedTechnologies(technologyRepo.convertFromIdList(selectedTechnologies));
    theaterRepo.update(t);
    return "redirect:/manage/theaters/";
  }

  //From "delete" button goes to "Are you sure to delete(...)?" or "Theater can't be deleted"
  @GetMapping("/manage/theaters/delete/{id}")
  public String deleteTheater(@PathVariable(name = "id") int id, Model m) {
    Theater t = theaterRepo.findTheater(id);
    boolean canDelete = theaterRepo.canDelete(t);
    m.addAttribute("canDelete", canDelete);
    m.addAttribute("theater", t);
    return "theaters/theaters-delete";
  }

  //Deletes Theater and lists all the Theaters
  @PostMapping("/manage/theaters/delete")
  public String deleteTheater(int id) {
    theaterRepo.deleteTheater(id);

    return "redirect:/manage/theaters";
  }
}

