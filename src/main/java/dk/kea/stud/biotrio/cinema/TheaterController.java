package dk.kea.stud.biotrio.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    ArrayList<Boolean> editableTheaters = new ArrayList<>();

    //Checking if the theaters can be edited and adding all answers in the array
    for (Theater theater : theaterList) {
      editableTheaters.add(theaterRepo.canDelete(theater));
    }
    m.addAttribute("theaterlist", theaterList);
    m.addAttribute("editableTheaters", editableTheaters);

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
                            @RequestParam(value = "selectedTechnologies", required = false)
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
                       @RequestParam(value = "selectedTechnologies", required = false)
                           List<Integer> selectedTechnologies) {
    t.setSupportedTechnologies(technologyRepo.convertFromIdList(selectedTechnologies));
    theaterRepo.update(t);
    return "redirect:/manage/theaters/";
  }

  //Deletes Theater and lists all the Theaters
  @PostMapping("/manage/theaters/delete")
  public String deleteTheater(@RequestParam(name = "theaterId") int id, Model model) {
    Theater theater = theaterRepo.findTheater(id);
    boolean canDelete = theaterRepo.canDelete(theater);

    if (canDelete) {
      theaterRepo.deleteTheater(id);
      return "redirect:/manage/theaters";
    } else {
      model.addAttribute("theaterName", theater.getName());
      return "theaters/theaters-delete";
    }
  }
}

