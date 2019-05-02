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

  @GetMapping("/theaters")
  public String theaters(Model m) {
    List<Theater> theaterList = theaterRepo.findAllTheaters();
    m.addAttribute("theaterlist", theaterList);
    // TODO theaters.html in resources
    //TODO maybe makes sense to have total no of seats in the view instead of noOfRows and seatsPerRow?
    return "theaters/theaters";
  }

  @GetMapping("/theaters/add")
  public String create(Model m) {
    m.addAttribute("theaterform", new Theater());

    return "theaters/add-theater";
  }

  @PostMapping("/theaters/savetheater")
  public String saveTheater(@ModelAttribute Theater t) {
    theaterRepo.insert(t);
    // why "redirect:" and not just theaters
    return "redirect:/theaters";
  }

  @GetMapping("/theaters/edit")
  public String update(@RequestParam("id") int id, Model m) {
    m.addAttribute("theater", theaterRepo.findTheater(id));

    return "edit-theater";
  }

  @PostMapping("/theaters/update")
  public String update(@ModelAttribute Theater t) {
    theaterRepo.update(t);

    return "redirect:/theaters";
  }

  @GetMapping("/manage/theaters/delete/{id}")
  public String deleteTheater(@PathVariable("id") int id) {
    theaterRepo.deleteTheater(id);

    return "redirect:/manage/theaters";
  }
}

