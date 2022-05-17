package src.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import src.backend.connector.Connector;

import java.util.HashMap;
import java.util.List;

@RestController
public class ReportsController {
    private final Connector connector;

    @Autowired
    public ReportsController(Connector connector) {
        this.connector = connector;
    }

    /**
     * This report shows how many movies users have rented and the total amount of money they spent on this app.
     */
    @GetMapping("/get-report-one")
    public ResponseEntity<List<HashMap<String, Object>>> getReportOne() {
        String query = "SELECT full_name, COUNT(user_id) AS num_rent, SUM(price) AS money_spent FROM user_rent_view " +
                "GROUP BY user_id ORDER BY num_rent DESC;";
        List<HashMap<String,Object>> result = connector.executeQuery(query);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * This report lists how many times each movie in the system has been recommended among users.
     */
    @GetMapping("/get-report-two")
    public ResponseEntity<List<HashMap<String, Object>>> getReportTwo() {
        String query = "SELECT title, COUNT(title) as num_recommend FROM recommendation NATURAL JOIN movie " +
                "GROUP BY title ORDER BY num_recommend DESC;";
        List<HashMap<String,Object>> result = connector.executeQuery(query);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * This report shows the genre names, ratings and the movie titles with the most rated movie in that specific genre.
     */
    @GetMapping("/get-report-three")
    public ResponseEntity<List<HashMap<String, Object>>> getReportThree() {
        String query = "SELECT gr.genre_name, title, max_rating FROM (SELECT genre_name, MAX(overall_rating) AS max_rating " +
                "FROM movie m NATURAL JOIN movie_genre mg NATURAL JOIN genre g GROUP BY g.genre_name) a, movie mov NATURAL JOIN " +
                "movie_genre mgr NATURAL JOIN genre gr WHERE mov.overall_rating = a.max_rating AND a.genre_name = gr.genre_name;";
        List<HashMap<String,Object>> result = connector.executeQuery(query);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
