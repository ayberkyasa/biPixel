package src.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import src.backend.connector.Connector;

import java.util.ArrayList;
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
    public ResponseEntity<?> listAllRentedMovies(@RequestParam("key") String searchKey,
                                                             @RequestParam("userId") Integer userId) {
        HashMap<String, Object> result = new HashMap<>();
        List<HashMap<String, Object>> movieList;
        if(!searchKey.equals("")){
            movieList = connector.executeQuery("SELECT DISTINCT movie.movie_id" +
                    " FROM (movie NATURAL JOIN act NATURAL JOIN actor NATURAL JOIN direct NATURAL JOIN director NATURAL JOIN movie_genre NATURAL JOIN genre) LEFT JOIN rent_movie ON rent_movie.movie_id = movie.movie_id" +
                    " WHERE movie.movie_id IN (SELECT movie_id FROM rent_movie WHERE user_id = " + userId +" AND withdrawn = false)" +
                    " AND (title RLIKE '" + searchKey + "' OR actor_full_name RLIKE '" + searchKey + "' OR director_full_name RLIKE '" + searchKey + "')");
        }else{
            movieList = connector.executeQuery("SELECT DISTINCT movie.movie_id" +
                    " FROM (movie NATURAL JOIN act NATURAL JOIN actor NATURAL JOIN direct NATURAL JOIN director NATURAL JOIN movie_genre NATURAL JOIN genre) LEFT JOIN rent_movie ON rent_movie.movie_id = movie.movie_id" +
                    " WHERE movie.movie_id IN (SELECT movie_id FROM rent_movie WHERE user_id = " + userId +" AND withdrawn = false)");
        }


        if (movieList.size() == 0) {
            result.put("result", "Movie does not exist.");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        int mid;
        int id;

        String name;

        List<String> actorList;
        List<String> directorList;
        List<String> genreList;

        List<HashMap<String, Object>> actList;
        List<HashMap<String, Object>> directList;
        List<HashMap<String, Object>> genList;

        List<HashMap<String, Object>> nameList;

        HashMap<String, Object> rid;
        HashMap<String, Object> title;
        HashMap<String, Object> year;
        HashMap<String, Object> rating;
        HashMap<String, Object> price;
        HashMap<String, Object> movie;
        HashMap<String, Object> duration;
        HashMap<String, Object> language_option;
        HashMap<String, Object> subtitle_option;

        List<HashMap<String, Object>> returned = new ArrayList<>();

        for (int i = 0; i < movieList.size(); i++) {
            mid = (Integer) movieList.get(i).values().toArray()[0];
            actList = connector.executeQuery("SELECT actor_id FROM act WHERE movie_id = " + mid);
            directList = connector.executeQuery("SELECT director_id FROM direct WHERE movie_id = " + mid);
            genList = connector.executeQuery("SELECT genre_id FROM movie_genre WHERE movie_id = " + mid);

            actorList = new ArrayList<String>();
            directorList = new ArrayList<String>();
            genreList = new ArrayList<String>();

            movie = new HashMap<>();

            rid = connector.executeQuery("SELECT MAX(rent_id) FROM rent_movie WHERE movie_id = " + mid + " AND user_id = " + userId).get(0);
            title = connector.executeQuery("SELECT title FROM movie WHERE movie_id = " + mid).get(0);
            year = connector.executeQuery("SELECT production_year FROM movie WHERE movie_id = " + mid).get(0);
            rating = connector.executeQuery("SELECT overall_rating FROM movie WHERE movie_id = " + mid).get(0);
            price = connector.executeQuery("SELECT price FROM movie WHERE movie_id = " + mid).get(0);
            duration = connector.executeQuery("SELECT duration FROM movie WHERE movie_id = " + mid).get(0);
            language_option = connector.executeQuery("SELECT language_option FROM movie WHERE movie_id = " + mid).get(0);
            subtitle_option = connector.executeQuery("SELECT subtitle_option FROM movie WHERE movie_id = " + mid).get(0);

            for (int z = 0; z < actList.size(); z++) {
                id = (Integer) actList.get(z).values().toArray()[0];
                nameList = connector.executeQuery("SELECT actor_full_name FROM actor WHERE actor_id = " + id);
                name = (String) nameList.get(0).values().toArray()[0];
                actorList.add(name);
            }

            for (int z = 0; z < directList.size(); z++) {
                id = (Integer) directList.get(z).values().toArray()[0];
                nameList = connector.executeQuery("SELECT director_full_name FROM director WHERE director_id = " + id);
                name = (String) nameList.get(0).values().toArray()[0];
                directorList.add(name);
            }

            for (int z = 0; z < genList.size(); z++) {
                id = (Integer) genList.get(z).values().toArray()[0];
                nameList = connector.executeQuery("SELECT genre_name FROM genre WHERE genre_id = " + id);
                name = (String) nameList.get(0).values().toArray()[0];
                genreList.add(name);
            }

            movie.put("rent_id", rid.values().toArray()[0]);
            movie.put("title", title.values().toArray()[0]);
            movie.put("production_year", year.values().toArray()[0]);
            movie.put("overall_rating", rating.values().toArray()[0]);
            movie.put("price", price.values().toArray()[0]);
            movie.put("actors", actorList);
            movie.put("directors", directorList);
            movie.put("genres", genreList);
            movie.put("movie_id", mid);
            movie.put("duration", duration.values().toArray()[0]);
            movie.put("language_option", language_option.values().toArray()[0]);
            movie.put("subtitle_option", subtitle_option.values().toArray()[0]);

            returned.add(movie);
        }
        return new ResponseEntity<>(returned, HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<HashMap<String, Object>> withdrawMovie(@RequestBody HashMap<String, Object> requestBody) {
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
