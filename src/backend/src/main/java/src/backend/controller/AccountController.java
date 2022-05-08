package src.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import src.backend.connector.Connector;

import java.util.HashMap;
import java.util.List;

@RestController
public class AccountController {
    private final Connector connector;

    @Autowired
    public AccountController(Connector connector) {
        this.connector = connector;
    }

    @GetMapping("/get-user")
    public ResponseEntity<HashMap<String, Object>> getUser(@RequestParam("id") Integer id) {
        HashMap<String, Object> response = new HashMap<>();
        response = connector.executeQuery("SELECT * FROM user WHERE user_id = " + id).get(0);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/add-friend")
    public HashMap<String, Object> addFriend(@RequestBody HashMap<String, Object> requestBody) {
        // TODO: From RequestBody, "whoSendRequest" and "whoReceiveRequest" will come.
        return null;
    }

    @DeleteMapping("/delete-friend")
    public HashMap<String, Object> deleteFriend(@RequestBody HashMap<String, Object> requestBody) {
        // TODO: From RequestBody, "whoDelete" and "whoIsDeleted" will come.
        return null;
    }

    @GetMapping("/show-friends")
    public List<HashMap<String, Object>> showFriends(@RequestParam("userId") Integer userId) {
        // TODO: Return list of all friends of the user specified by "userId"
        return null;
    }

    @PostMapping("/recommend-movie")
    public HashMap<String, Object> recommendMovie(@RequestBody HashMap<String, Object> requestBody) {
        // TODO: From RequestBody, "recommender", "friend", and "movieId" will come.
        return null;
    }

    @GetMapping("/get-recommended-movies")
    public List<HashMap<String, Object>> getRecommendedMovies(@RequestParam("userId") Integer userId) {
        // TODO: return list of all recommended movies of the user specified by "userId"
        return null;
    }

    @PostMapping("/add-favorite")
    public HashMap<String, Object> addFavorite(@RequestBody HashMap<String, Object> requestBody) {
        // TODO: From RequestBody, "movieId" and "userId" will come.
        return null;
    }

    @DeleteMapping("/delete-favorite")
    public HashMap<String, Object> deleteFavorite(@RequestBody HashMap<String, Object> requestBody) {
        // TODO: From RequestBody, "movieId" and "userId" will come.
        return null;
    }

    @GetMapping("/get-favorite-list")
    public List<HashMap<String, Object>> getFavoriteList(@RequestParam("userId") Integer userId) {
        return null;
    }

    @PostMapping("/write-review")
    public HashMap<String, Object> writeReview(@RequestBody HashMap<String, Object> requestBody) {
        // TODO: From RequestBody, "movieId", "userId", "reviewComment" will come.
        return null;
    }

    @PostMapping("/rate")
    public HashMap<String, Object> rate(@RequestBody HashMap<String, Object> requestBody) {
        // TODO: From RequestBody, "movieId", "userId", "ratingScore" will come.
        return null;
    }

    @GetMapping("/show-movie-history")
    public List<HashMap<String, Object>> showMovieHistory(@RequestParam("userId") Integer userId) {
        // TODO: Return all previously rented movies of the user specified by "userId"
        // WARNING: This returned list should include movie ratings and reviews of the user.
        return null;
    }

    @GetMapping("/show-current-movies")
    public List<HashMap<String, Object>> showCurrentMovies(@RequestParam("userId") Integer userId) {
        // TODO: Return currently rented movies of the user specified by "userId"
        // WARNING: This returned list should include movie ratings and reviews of the user.
        return null;
    }
}
