package src.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import src.backend.connector.Connector;

import java.util.HashMap;
import java.util.List;

@RestController
public class WithdrawController {
    private final Connector connector;

    @Autowired
    public WithdrawController(Connector connector) {
        this.connector = connector;
    }

    @GetMapping("/list-all-rented-movies")
    public List<HashMap<String, Object>> listAllRentedMovies(@RequestParam("key") String searchKey,
                                                             @RequestParam("type") String searchType,
                                                             @RequestParam("userId") Integer userId) {
        // TODO: search the movies by title, director, production year, actor
        // TODO: List all rented movies of the user specified by "userId"
        return null;
    }

    @PostMapping("/withdraw")
    public HashMap<String, Object> withdrawMovie(@RequestBody HashMap<String, Object> requestBody) {
        // TODO: From RequestBody, movieId and userId will come.
        return null;
    }
}
