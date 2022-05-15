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
public class RentController {
    private final Connector connector;

    @Autowired
    public RentController(Connector connector) {
        this.connector = connector;
    }

    @GetMapping("/search-movie")
    public ResponseEntity<?> searchBy(@RequestParam("key") String searchKey,
                                                  @RequestParam("userId") Integer userId) {

        List<HashMap<String, Object>> movieList;
        HashMap<String, Object> result = new HashMap<>();
        if(!searchKey.equals("")){
            movieList = connector.executeQuery("SELECT DISTINCT movie.movie_id" +
                    " FROM (movie NATURAL JOIN act NATURAL JOIN actor NATURAL JOIN direct NATURAL JOIN director NATURAL JOIN movie_genre NATURAL JOIN genre) LEFT JOIN rent_movie ON rent_movie.movie_id = movie.movie_id" +
                    " WHERE movie.movie_id NOT IN (SELECT movie_id FROM rent_movie WHERE user_id = " + userId +" AND withdrawn = false)" +
                    " AND (title RLIKE '" + searchKey + "' OR actor_full_name RLIKE '" + searchKey + "' OR director_full_name RLIKE '" + searchKey + "')");
        }else{
            movieList = connector.executeQuery("SELECT DISTINCT movie.movie_id" +
                    " FROM (movie NATURAL JOIN act NATURAL JOIN actor NATURAL JOIN direct NATURAL JOIN director NATURAL JOIN movie_genre NATURAL JOIN genre) LEFT JOIN rent_movie ON rent_movie.movie_id = movie.movie_id" +
                    " WHERE movie.movie_id NOT IN (SELECT movie_id FROM rent_movie WHERE user_id = " + userId +" AND withdrawn = false)");
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

            movie.put("title", title.values().toArray()[0]);
            movie.put("production_year", year.values().toArray()[0]);
            movie.put("overall_rating", rating.values().toArray()[0]);
            movie.put("price", price.values().toArray()[0]);
            movie.put("actors", actorList);
            movie.put("directors", directorList);
            movie.put("genres", genreList);
            movie.put("duration", duration.values().toArray()[0]);
            movie.put("language_option", language_option.values().toArray()[0]);
            movie.put("subtitle_option", subtitle_option.values().toArray()[0]);
            movie.put("mid", mid);

            returned.add(movie);
        }
        return new ResponseEntity<>(returned, HttpStatus.OK);
    }

    @PostMapping("/rent")
    public ResponseEntity<HashMap<String, Object>> rentMovie(@RequestBody HashMap<String, Object> requestBody) {

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
        int renewTimes;
        HashMap<String, Object> result = new HashMap<>();
        String query1 = "SELECT renew_times FROM rent WHERE rent_id = " + requestBody.get("rentId");
        String query2 = "UPDATE rent" +
                " SET last_renew_date = CURRENT_DATE(), due_date = date_add(CURRENT_DATE(), INTERVAL 7 DAY), renew_times = renew_times + 1" +
                " WHERE rent_id = " + requestBody.get("rentId");
        try {
            List<HashMap<String, Object>> returned = connector.executeQuery(query1);
            renewTimes = (Integer) returned.get(0).values().toArray()[0];

            if (renewTimes == 3) {
                result.put("result", "You can renew the rental period at most 3 times");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            else {
                connector.executeUpdate(query2);
                result.put("result", "The movie is renewed.");
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        } catch (Exception e) {
            result.put("result", "Failure due to exception.");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }


}
