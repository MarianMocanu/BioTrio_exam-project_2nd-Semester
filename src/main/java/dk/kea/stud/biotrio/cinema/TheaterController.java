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

  @GetMapping("/manage/theaters")
  public String theaters(Model m) {
    List<Theater> theaterList = theaterRepo.findAllTheaters();
    m.addAttribute("theaterlist", theaterList);

    return "theaters/theaters";
  }

  @GetMapping("/manage/theaters/add")
  public String create(Model m) {
    m.addAttribute("theaterform", new Theater());

    return "theaters/add-theater";
  }

  @PostMapping("/manage/theaters/add")
  public String saveTheater(@ModelAttribute Theater t) {
    theaterRepo.insert(t);
    // why "redirect:" and not just theaters:
    //because after posting we need @GetMapping to be able to read what's on /theaters
    return "redirect:/manage/theaters";
  }

  @GetMapping("/manage/theaters/edit/{id}")
  public String update(@PathVariable(name = "id") int id, Model m) {
    m.addAttribute("theater", theaterRepo.findTheater(id));

    return "theaters/edit-theater";
  }

  @PostMapping("/manage/theaters/edit")
  public String update(@ModelAttribute Theater t) {
    theaterRepo.update(t);

    return "redirect:/manage/theaters/";
  }
//TODO "are you sure you want to delete" view
  @GetMapping("/manage/theaters/delete/{id}")
  public String deleteTheater(@PathVariable("id") int id) {
    theaterRepo.deleteTheater(id);

    return "redirect:/manage/theaters";
  }
}

