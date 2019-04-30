package dk.kea.stud.biotrio.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class TheaterController {

    @Autowired
    private TheaterRepository theaterRepo;

    @GetMapping("/theaters")
    public String theaters(Model m){
        List<Theater> theaterList = theaterRepo.findAllTheaters();
        m.addAttribute("theaterlist", theaterList);
        // TODO theaters.html in resources
        return "theaters";
    }
    @GetMapping ("/theaters/create")
    public String create (Model m){
        m.addAttribute("theatreform", new Theater());

        return "createTheater";
    }
    @PostMapping("/theaters/savetheater")
    public String saveTheater (@ModelAttribute Theater t){
        theaterRepo.insert(t);
        return "redirect:/theaters";
    }

    //TODO something
    //@GetMapping ("theaters/update")
    //public String

}

