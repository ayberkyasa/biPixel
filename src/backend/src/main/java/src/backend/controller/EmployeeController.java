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
        HashMap<String, Object> response = new HashMap<>();
        String insertMovieQuery = "INSERT INTO movie (title, overall_rating, duration, production_year, language_option, subtitle_option,price) " +
                "VALUES " +
                "(" +
                "'" + requestBody.get("title") + "'" + "," +
                null + "," +
                requestBody.get("duration") + "," +
                "'" + requestBody.get("production_year") + "'" + ","  +
                "'" + requestBody.get("language_option") + "'" + "," +
                "'" + requestBody.get("subtitle_option") + "'" + "," +
                requestBody.get("price") +
                ");";

        String selectQuery = "SELECT * from movie " +
                "WHERE title = '" + requestBody.get("title") + "' AND " + "production_year = '" + requestBody.get("production_year") + "';";
        List<HashMap<String, Object>> movieList = connector.executeQuery(selectQuery);
        if(!movieList.isEmpty()){
            response.put("message", "Movie has already existed.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        connector.executeUpdate(insertMovieQuery);
        String insertGenreQuery = "INSERT INTO movie_genre (movie_id, genre_id) VALUES ((SELECT MAX(movie_id) FROM movie), (SELECT genre_id FROM genre WHERE genre_name = ";
        for(String genreName: (ArrayList<String>)requestBody.get("genres")) {
            String insertGenreQuery2 = insertGenreQuery + "'" + genreName + "'));";
            connector.executeUpdate(insertGenreQuery2);
        }

        String insertDirectQuery = "INSERT INTO direct (movie_id, director_id) VALUES ((SELECT MAX(movie_id) FROM movie), (SELECT director_id FROM director WHERE director_full_name = ";
        for(String directorName: (ArrayList<String>)requestBody.get("directors_full_name")) {
            if(connector.executeQuery("SELECT * FROM director WHERE director_full_name = '" + directorName + "';").isEmpty()) {
                connector.executeUpdate("INSERT INTO director (director_full_name) VALUES ('" + directorName + "');");
            }
            String insertDirectQuery2 = insertDirectQuery + "'" + directorName + "'));";
            connector.executeUpdate(insertDirectQuery2);
        }
        String insertActorQuery = "INSERT INTO act (movie_id, actor_id) VALUES ((SELECT MAX(movie_id) FROM movie), (SELECT actor_id FROM actor WHERE actor_full_name = ";
        for(String actorName: (ArrayList<String>)requestBody.get("actors_full_name")) {
            if(connector.executeQuery("SELECT * FROM actor WHERE actor_full_name = '" + actorName + "';").isEmpty()) {
                connector.executeUpdate("INSERT INTO actor (actor_full_name) VALUES ('" + actorName + "');");
            }
            String insertActorQuery2 = insertActorQuery + "'" + actorName + "'));";
            connector.executeUpdate(insertActorQuery2);
        }
        response.put("message", "Movie was successfully added");
        return new ResponseEntity<>(response, HttpStatus.OK);


    }

    @DeleteMapping("/remove-movie")
    public ResponseEntity<HashMap<String, Object>> removeMovie(@RequestBody HashMap<String, Object> requestBody) {
        HashMap<String, Object> response = new HashMap<>();

        String removeMovieQuery = "DELETE FROM movie WHERE movie_id = " + requestBody.get("movieId") + ";";
        connector.executeUpdate(removeMovieQuery);

        response.put("message", "Movie was successfully deleted");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete-customer")
    public ResponseEntity<HashMap<String, Object>> deleteCustomer(@RequestBody HashMap<String, Object> requestBody) {
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

    @GetMapping("/get-all-requests")
    public ResponseEntity<List<HashMap<String, Object>>> getAllRequests() {
        String query = "SELECT * FROM requested_movie NATURAL JOIN request NATURAL JOIN user";
        List<HashMap<String, Object>> requests = connector.executeQuery(query);
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @DeleteMapping("/reject-request")
    public ResponseEntity<HashMap<String, Object>> rejectRequest(@RequestBody HashMap<String, Object> requestBody) {
        String query = "DELETE FROM requested_movie WHERE req_movie_id = " + requestBody.get("req_movie_id");
        connector.executeUpdate(query);
        HashMap<String, Object> result = new HashMap<>();
        result.put("result", "The request was rejected.");
        return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);
    }
}
