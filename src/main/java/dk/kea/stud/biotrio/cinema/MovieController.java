package dk.kea.stud.biotrio.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MovieController {

    @Autowired
    private MovieRepository movieRepo;

    @GetMapping("/movies")
    public String findMovies(Model model){
        model.addAttribute( "movies", movieRepo.findAllMovies() );
        return "movies/movies-view";
    }

    //TODO @GetMapping("/manage")

    @GetMapping("/manage/movies")
    public String manageMovies(Model model){
         model.addAttribute( "movies", movieRepo.findAllMovies() );
        return "movies/manage-movies";
     }

    @GetMapping("/manage/movies/add")
    public String addMovie(Model model) {
        model.addAttribute( "movie", new Movie() );
        return "movies/add-movie";
    }

    @PostMapping("/manage/movies/save")
    public String saveMovie(@ModelAttribute Movie movie) {
        movieRepo.addMovie( movie );
        return "redirect:/manage/movies";
    }

    //TODO @GetMapping("/manage/movies/edit/{id}")

    //TODO @GetMapping("manage/movies/delete/{id}")



}
