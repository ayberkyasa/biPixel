package src.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import src.backend.connector.Connector;

import java.util.HashMap;
import java.util.List;

@RestController
public class EmployeeController {
    private final Connector connector;

    @Autowired
    public EmployeeController(Connector connector) {
        this.connector = connector;
    }

    @PostMapping("/add-movie")
    public ResponseEntity<HashMap<String, Object>> addMovie(@RequestBody HashMap<String, Object> requestBody) {
        // TODO: From RequestBody, all information about movie will come. Check ER Diagram from movie table.
        HashMap<String, Object> response = new HashMap<>();
        String insertQuery = "INSERT INTO movie (title, overall_rating, duration, production_year, language_option, subtitle_option,price) " +
                "VALUES " +
                "(" +
                "'" + requestBody.get("title") + "'" + "," +
                requestBody.get("overall_rating") + "," +
                requestBody.get("duration") + "," +
                requestBody.get("production_year") + "," +
                "'" + requestBody.get("language_option") + "'" + "," +
                "'" + requestBody.get("subtitle_option") + "'" + "," +
                requestBody.get("price") +
                ");";

        String selectQuery = "SELECT * from movie " +
                "WHERE title = '" + requestBody.get("title") + "' and " + "production_year = " + requestBody.get("production_year") + ";";
        List<HashMap<String, Object>> movieList = connector.executeQuery(selectQuery);
        if(movieList.isEmpty()){
            connector.executeUpdate(insertQuery);
            response.put("message", "Movie was successfully added");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response.put("message", "Movie exists");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping("/remove-movie")
    public ResponseEntity<HashMap<String, Object>> removeMovie(@RequestParam("movieId") Integer movieId) {
        // TODO: Delete the movie specified by "movieId"
        HashMap<String, Object> response = new HashMap<>();

        String removeMovieGenreQuery = "DELETE FROM movie_genre WHERE movie_id = " + movieId + ";";
        connector.executeUpdate(removeMovieGenreQuery);

        String removeActQuery = "DELETE FROM act WHERE movie_id = " + movieId + ";";
        connector.executeUpdate(removeActQuery);

        String removeDirectQuery = "DELETE FROM direct WHERE movie_id = " + movieId + ";";
        connector.executeUpdate(removeDirectQuery);

        String removeMovieQuery = "DELETE FROM movie WHERE movie_id = " + movieId + ";";
        connector.executeUpdate(removeMovieQuery);

        response.put("message", "Movie was successfully deleted");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete-customer")
    public ResponseEntity<HashMap<String, Object>> deleteCustomer(@RequestBody HashMap<String, Object> requestBody) {
        // TODO: From RequestBody, "userId" will come
        HashMap<String, Object> response = new HashMap<>();

        String deleteCustomerQuery = "DELETE FROM customer WHERE user_id = " + requestBody.get("userId") + ";";
        connector.executeUpdate(deleteCustomerQuery);

        String deleteUserQuery = "DELETE FROM user WHERE user_id = " + requestBody.get("userId") + ";";
        connector.executeUpdate(deleteUserQuery);

        response.put("message", "Customer was successfully deleted");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-all-customers")
    public ResponseEntity<List<HashMap<String, Object>>> getAllCustomers() {
        // TODO: return all customers
        String customerQuery = "SELECT * FROM user WHERE user_id in (SELECT user_id FROM customer);";
        List<HashMap<String, Object>> customerList = connector.executeQuery(customerQuery);
        return new ResponseEntity<>(customerList, HttpStatus.OK);
    }
}
