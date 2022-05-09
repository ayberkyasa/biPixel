package src.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<HashMap<String, Object>> withdrawMovie(@RequestBody HashMap<String, Object> requestBody) {
        // TODO: From RequestBody, movieId and userId will come.
        HashMap<String, Object> result = new HashMap<>();
        try {
            connector.executeUpdate("UPDATE rent_movie" +
                    " SET withdrawn = 1" +
                    " WHERE rent_id = " + requestBody.get("rentId"));

            result.put("result", "The movie is withdrawn.");
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            result.put("result", "Failure due to exception.");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }
}
