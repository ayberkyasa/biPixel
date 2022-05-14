package src.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import src.backend.connector.Connector;

import java.util.ArrayList;
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
                requestBody.get("production_year") + ","  +
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

        String removeMovieQuery = "DELETE FROM movie WHERE movie_id = " + movieId + ";";
        connector.executeUpdate(removeMovieQuery);

        response.put("message", "Movie was successfully deleted");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete-customer")
    public ResponseEntity<HashMap<String, Object>> deleteCustomer(@RequestBody HashMap<String, Object> requestBody) {
        // TODO: From RequestBody, "userId" will come
        HashMap<String, Object> response = new HashMap<>();

        String deleteUserQuery = "DELETE FROM user WHERE user_id = " + requestBody.get("userId") + ";";
        connector.executeUpdate(deleteUserQuery);

        response.put("message", "Customer was successfully deleted");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-all-customers")
    public ResponseEntity<List<HashMap<String, Object>>> getAllCustomers() {
        String customerQuery = "SELECT * FROM user WHERE user_id in (SELECT user_id FROM customer);";
        List<HashMap<String, Object>> customerList = connector.executeQuery(customerQuery);
        return new ResponseEntity<>(customerList, HttpStatus.OK);
    }

    @GetMapping("/get-all-movies")
    public ResponseEntity<List<HashMap<String, Object>>> getAllMovies() {
        List<HashMap<String, Object>> movieList = connector.executeQuery("SELECT * FROM movie;");
        List<HashMap<String, Object>> actors;
        List<HashMap<String, Object>>directors;
        List<HashMap<String, Object>> genres;
        for(HashMap<String, Object> movie : movieList) {
            String query = "SELECT actor_full_name FROM actor WHERE actor_id IN (SELECT actor_id FROM act WHERE movie_id = " + movie.get("movie_id") + ");";
            actors = connector.executeQuery(query);
            ArrayList<String> actors_full_name = new ArrayList<>();
            for(HashMap<String, Object> actor : actors) {
                actors_full_name.add((String) actor.get("actor_full_name"));
            }
            movie.put("actors_full_name", actors_full_name);

            query = "SELECT director_full_name FROM director WHERE director_id IN (SELECT director_id FROM direct WHERE movie_id = " + movie.get("movie_id") + ");";
            directors = connector.executeQuery(query);
            ArrayList<String> directors_full_name = new ArrayList<>();
            for(HashMap<String, Object> director : directors) {
                directors_full_name.add((String) director.get("director_full_name"));
            }
            movie.put("directors_full_name", directors_full_name);

            query = "SELECT genre_name FROM genre WHERE genre_id IN (SELECT genre_id FROM movie_genre WHERE movie_id = " + movie.get("movie_id") + ");";
            genres = connector.executeQuery(query);
            ArrayList<String> genres_name = new ArrayList<>();
            for(HashMap<String, Object> genre : genres) {
                genres_name.add((String) genre.get("genre_name"));
            }
            movie.put("genres", genres_name);
        }
        return new ResponseEntity<>(movieList, HttpStatus.OK);
    }
}
