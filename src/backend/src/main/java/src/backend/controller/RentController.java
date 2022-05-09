package src.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        
        try {
            List<HashMap<String, Object>> returned = connector.executeQuery("SELECT DISTINCT title, director_first_name, director_middle_name, director_last_name, overall_rating, production_year, price" +
                    " FROM (movie NATURAL JOIN act NATURAL JOIN actor NATURAL JOIN direct NATURAL JOIN director NATURAL JOIN movie_genre NATURAL JOIN genre) LEFT JOIN rent_movie ON rent_movie.movie_id = movie.movie_id" +
                    " WHERE movie.movie_id NOT IN (SELECT movie_id FROM rent_movie WHERE user_id = " + userId +")" +
                    " AND (title RLIKE '" + searchKey + "' OR actor_first_name RLIKE '" + searchKey + "' OR actor_middle_name RLIKE '" + searchKey + "' OR actor_last_name RLIKE '" + searchKey + "'" +
                    " OR director_first_name RLIKE '" + searchKey + "' OR director_middle_name RLIKE '" + searchKey + "' OR director_last_name RLIKE '" + searchKey + "')");

            if (returned.size() == 0) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("result", "No movie found.");
                returned.add(result);
            }
            return returned;
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/rent")
    public ResponseEntity<HashMap<String, Object>> rentMovie(@RequestBody HashMap<String, Object> requestBody) {
        // TODO: From RequestBody, movieId and userId will come.
        // TODO: If userId exists in Employee table, there is no fee. Otherwise, there is fee.

        HashMap<String, Object> result = new HashMap<>();
        try {
            connector.executeUpdate("INSERT INTO rent (rent_date, due_date, last_renew_date, renew_times)" +
                    " VALUES (CURRENT_DATE(), DATE_ADD(CURRENT_DATE(), INTERVAL 7 DAY), CURRENT_DATE(), 0)");
            connector.executeUpdate("INSERT INTO rent_movie (movie_id, rent_id, user_id, withdrawn)" +
                    " VALUES (" + requestBody.get("movieId") + ", (SELECT MAX(rent_id) from rent), " + requestBody.get("userId") + ", 0)");

            result.put("result", "The movie is rented.");
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            result.put("result", "Failure due to exception.");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("renew")
    public ResponseEntity<HashMap<String, Object>> renewMovie(@RequestBody HashMap<String, Object> requestBody) {
        // TODO: From RequestBody, movieId and userId will come.
        // TODO: If userId exists in Employee table, there is no fee. Otherwise, there is fee.

        HashMap<String, Object> result = new HashMap<>();
        try {
            connector.executeUpdate("UPDATE rent" +
                    " SET last_renew_date = CURRENT_DATE(), due_date = date_add(CURRENT_DATE(), INTERVAL 7 DAY), renew_times = renew_times + 1" +
                    " WHERE rent_id = " + requestBody.get("rentId"));

            result.put("result", "The movie is renewed.");
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            result.put("result", "Failure due to exception.");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }


}
