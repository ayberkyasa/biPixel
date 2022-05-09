package src.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import src.backend.connector.Connector;

import java.util.HashMap;
import java.util.List;

@RestController
public class CustomerController {
    private final Connector connector;

    @Autowired
    public CustomerController(Connector connector) {
        this.connector = connector;
    }

    @PostMapping("/request-movie")
    public ResponseEntity<HashMap<String, Object>> requestMovie(@RequestBody HashMap<String, Object> requestBody) {
        // TODO: From RequestBody, all information about movie will come. Check ER Diagram from requested_movie table.
        HashMap<String, Object> result = new HashMap<>();
        List<HashMap<String, Object>> movieList = connector.executeQuery("SELECT * FROM movie NATURAL JOIN direct NATURAL JOIN director " +
                "WHERE title = '" + requestBody.get("title") + "' AND production_year = " + requestBody.get("production_year") + " AND " +
                "director_full_name = '" + requestBody.get("directors_full_name") + "'");

        if (movieList.size() != 0) {
            result.put("result", "Movie already exists.");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        else {
            List<HashMap<String, Object>> returned = connector.executeQuery("SELECT * FROM requested_movie NATURAL JOIN request" +
                    " WHERE user_id = " + requestBody.get("userId") + " AND title = '" + requestBody.get("title") + "' AND production_year = " + requestBody.get("production_year") + " " +
                    " AND directors_full_name = '" + requestBody.get("directors_full_name") + "'");

            if (returned.size() != 0) {
                result.put("result", "You have already made this request.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }

            connector.executeUpdate("INSERT INTO requested_movie(title, directors_full_name, actors_first_name, production_year, duration, genres)" +
                    " VALUES('" + requestBody.get("title") + "', '" + requestBody.get("directors_full_name") + "', '" + requestBody.get("actors_full_name") + "', " + requestBody.get("production_year") + "" +
                    ", " + requestBody.get("duration") + ", '" + requestBody.get("genres") + "')");
            connector.executeUpdate("INSERT INTO request(user_id, req_movie_id)" +
                    " VALUES(" + requestBody.get("userId") + ", (SELECT MAX(req_movie_id) FROM requested_movie))");

            result.put("result", "Your request is sent.");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }
}
