package src.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import src.backend.connector.Connector;

import java.util.HashMap;
import java.util.List;

@RestController
public class RentController {
    private final Connector connector;

    @Autowired
    public RentController(Connector connector) {
        this.connector = connector;
    }

    @GetMapping("/search-movie")
    public List<HashMap<String, Object>> searchBy(@RequestParam("key") String searchKey,
                                                  @RequestParam("type") String searchType,
                                                  @RequestParam("userId") Integer userId) {
        // TODO: search the movies by title, director, production year, actor
        // WARNING: Filtering will be done in the frontend.
        // TODO: If the user specified by "userId" already rented the movie, do not show it on the returned list.
        return null;
    }

    @PostMapping("/rent")
    public HashMap<String, Object> rentMovie(@RequestBody HashMap<String, Object> requestBody) {
        // TODO: From RequestBody, movieId and userId will come.
        // TODO: If userId exists in Employee table, there is no fee. Otherwise, there is fee.
        return null;
    }

    @PostMapping("renew")
    public HashMap<String, Object> renewMovie(@RequestBody HashMap<String, Object> requestBody) {
        // TODO: From RequestBody, movieId and userId will come.
        // TODO: If userId exists in Employee table, there is no fee. Otherwise, there is fee.
        return null;
    }


}
