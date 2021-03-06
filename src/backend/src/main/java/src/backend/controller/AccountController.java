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
    public ResponseEntity<HashMap<String, Object>> addFriend(@RequestBody HashMap<String, Object> requestBody) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            List<HashMap<String, Object>> returned = connector.executeQuery("SELECT * FROM user WHERE user_id = " + requestBody.get("whoSendRequest"));
            if(returned.size() == 0) {
                result.put("result", "Friend request failed! User who sent the request does not exist..");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            returned = connector.executeQuery("SELECT * FROM user WHERE user_id = " + requestBody.get("whoReceiveRequest"));
            if(returned.size() == 0) {
                result.put("result", "Friend request failed! User who received the request does not exist.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }

            if(requestBody.get("whoSendRequest") == requestBody.get("whoReceiveRequest")) {
                result.put("result", "Friend request failed! You cannot send a request to yourself.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            connector.executeUpdate("INSERT INTO friends VALUES(" + requestBody.get("whoReceiveRequest") + ", " + requestBody.get("whoSendRequest") + ", 0)");
            result.put("result", "Friend request sent!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.put("result", "Friend request failed due to an exception!");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/answer-request")
    public ResponseEntity<HashMap<String, Object>> answerRequest(@RequestBody HashMap<String, Object> requestBody) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            List<HashMap<String, Object>> returned = connector.executeQuery("SELECT * FROM user WHERE user_id = " + requestBody.get("whoSendRequest"));
            if(returned.size() == 0) {
                result.put("result", "Friend request answer failed! User who sent the request does not exist.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            returned = connector.executeQuery("SELECT * FROM user WHERE user_id = " + requestBody.get("whoAnswerRequest"));
            if(returned.size() == 0) {
                result.put("result", "Friend request answer failed! User who answered the request does not exist.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            if(requestBody.get("requestId").equals("true")) {
                connector.executeUpdate("UPDATE friends SET is_accepted = 1 WHERE request_receiver = " + requestBody.get("whoAnswerRequest") + " AND request_sender = " + requestBody.get("whoSendRequest"));
                result.put("result", "Friend request answer successful! User is added as a friend.");
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
            else if(requestBody.get("requestId").equals("false")) {
                connector.executeUpdate("DELETE FROM friends WHERE request_receiver = " + requestBody.get("whoAnswerRequest") + " AND request_sender = " + requestBody.get("whoSendRequest"));
                result.put("result", "Friend request answer successful! User is not added as a friend and the friend request is deleted.");
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
            else {
                result.put("result", "Friend request answer failed! requestId does not have a valid value.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
        result.put("result", "Friend request answer failed due to an exception!");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete-friend")
    public ResponseEntity<HashMap<String, Object>> deleteFriend(@RequestBody HashMap<String, Object> requestBody) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            List<HashMap<String, Object>> returned = connector.executeQuery("SELECT * FROM user WHERE user_id = " + requestBody.get("whoDelete"));
            if(returned.size() == 0) {
                result.put("result", "Friend delete failed! User who deletes a friend does not exist.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            returned = connector.executeQuery("SELECT * FROM user WHERE user_id = " + requestBody.get("whoIsDeleted"));
            if(returned.size() == 0) {
                result.put("result", "Friend delete failed! User who you are trying to delete does not exist.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            returned = connector.executeQuery("SELECT * FROM friends WHERE request_receiver = " + requestBody.get("whoDelete") + " AND request_sender = " + requestBody.get("whoIsDeleted") + " AND is_accepted = 1");
            if(returned.size() == 1) {
                connector.executeUpdate("DELETE FROM friends WHERE request_receiver = " + requestBody.get("whoDelete") + " AND request_sender = " + requestBody.get("whoIsDeleted"));
                result.put("result", "Friend delete successful!");
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
            returned = connector.executeQuery("SELECT * FROM friends WHERE request_receiver = " + requestBody.get("whoIsDeleted") + " AND request_sender = " + requestBody.get("whoDelete") + " AND is_accepted = 1");
            if(returned.size() == 1) {
                connector.executeUpdate("DELETE FROM friends WHERE request_receiver = " + requestBody.get("whoIsDeleted") + " AND request_sender = " + requestBody.get("whoDelete"));
                result.put("result", "Friend delete successful!");
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
            result.put("result", "Friend delete failed! You are not friends.");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            result.put("result", "Friend delete failed due to an exception!");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/show-friends")
    public ResponseEntity<?> showFriends(@RequestParam("userId") Integer userId) {
        try {
            List<HashMap<String, Object>> returned = connector.executeQuery("SELECT * FROM user WHERE user_id = " + userId);
            if(returned.size() == 0) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("result", "Retrieval of friends failed! User does not exist.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            List<HashMap<String, Object>> friends = connector.executeQuery("SELECT * FROM user WHERE user_id IN (SELECT request_receiver FROM friends WHERE request_sender = " + userId + " AND is_accepted = 1 UNION SELECT request_sender FROM friends WHERE request_receiver = " + userId + " AND is_accepted = 1)");
            return new ResponseEntity<>(friends, HttpStatus.OK);
        }
        catch (Exception e) {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "Retrieval of friends failed due to an exception!");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

     @GetMapping("/show-non-friends")
     public ResponseEntity<?> showNonFriends(@RequestParam("userId") Integer userId) {
        try {
            List<HashMap<String, Object>> returned = connector.executeQuery("SELECT * FROM user WHERE user_id = " + userId);
            if(returned.size() == 0) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("result", "Retrieval of non-friends failed! User does not exist.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            List<HashMap<String, Object>> nonFriends = connector.executeQuery("SELECT * FROM user WHERE user_id NOT IN (SELECT request_receiver FROM friends WHERE request_sender = " + userId + " UNION SELECT request_sender FROM friends WHERE request_receiver = " + userId + " UNION SELECT " + userId + ")");
            return new ResponseEntity<>(nonFriends, HttpStatus.OK);
        }
        catch (Exception e) {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "Retrieval of non-friends failed due to an exception!");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/show-receiving-friend-requests")
    public ResponseEntity<?> showReceivingFriendRequests(@RequestParam("userId") Integer userId) {
        try {
            List<HashMap<String, Object>> returned = connector.executeQuery("SELECT * FROM user WHERE user_id = " + userId);
            if(returned.size() == 0) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("result", "Retrieval of receiving friend requests failed! User does not exist.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            List<HashMap<String, Object>> receivingFriends = connector.executeQuery("SELECT * FROM user WHERE user_id IN (SELECT request_sender FROM friends WHERE request_receiver = " + userId + " AND is_accepted = 0)");
            return new ResponseEntity<>(receivingFriends, HttpStatus.OK);
        }
        catch (Exception e) {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "Retrieval of receiving friend requests failed due to an exception!");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/show-sent-friend-requests")
    public ResponseEntity<?> showSentFriendRequests(@RequestParam("userId") Integer userId) {
        try {
            List<HashMap<String, Object>> returned = connector.executeQuery("SELECT * FROM user WHERE user_id = " + userId);
            if(returned.size() == 0) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("result", "Retrieval of receiving friend requests failed! User does not exist.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            List<HashMap<String, Object>> receivingFriends = connector.executeQuery("SELECT * FROM user WHERE user_id IN (SELECT request_receiver FROM friends WHERE request_sender = " + userId + " AND is_accepted = 0)");
            return new ResponseEntity<>(receivingFriends, HttpStatus.OK);
        }
        catch (Exception e) {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "Retrieval of coming friend requests failed due to an exception!");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/recommend-movie")
    public ResponseEntity<HashMap<String, Object>> recommendMovie(@RequestBody HashMap<String, Object> requestBody) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            List<HashMap<String, Object>> returned = connector.executeQuery("SELECT * FROM user WHERE user_id = " + requestBody.get("recommender"));
            if(returned.size() == 0) {
                result.put("result", "Recommendation failed! Recommender user does not exist.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            returned = connector.executeQuery("SELECT * FROM user WHERE user_id = " + requestBody.get("friend"));
            if(returned.size() == 0) {
                result.put("result", "Recommendation failed! Friend you are trying to recommend a movie to does not exist.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            returned = connector.executeQuery("SELECT * FROM movie WHERE movie_id = " + requestBody.get("movieId") );
            if(returned.size() == 0) {
                result.put("result", "Recommendation failed! Movie you are trying to recommend does not exist.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            returned = connector.executeQuery("SELECT * FROM friends WHERE request_sender = " + requestBody.get("friend") + " AND request_receiver = " + requestBody.get("recommender") + " AND is_accepted = 1 UNION SELECT * FROM friends WHERE request_sender = " + requestBody.get("recommender") + " AND request_receiver = " + requestBody.get("friend") + " AND is_accepted = 1");
            if(returned.size() == 0) {
                result.put("result", "Recommendation failed! You are not friends.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            returned = connector.executeQuery("SELECT * FROM recommendation WHERE recommender = " + requestBody.get("recommender") + " AND friend = " + requestBody.get("friend") + " AND movie_id = " + requestBody.get("movieId") + ";");
            if(returned.size() > 0) {
                result.put("result", "Recommendation failed! You have already recommended this movie to this friend.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            else {
                connector.executeUpdate("INSERT INTO recommendation (recommender, friend, movie_id) VALUES (" + requestBody.get("recommender") + ", " + requestBody.get("friend") + ", " + requestBody.get("movieId") + ")");
                result.put("result", "Recommendation successful!");
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        }
        catch (Exception e) {
            result.put("result", "Recommendation failed due to an exception!");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-recommended-movies")
    public ResponseEntity<?> getRecommendedMovies(@RequestParam("userId") Integer userId) {
        int mid;
        int id;

        String name;

        List<String> recommenderList;
        List<String> directorList;
        List<String> genreList;

        List<HashMap<String, Object>> recommendList;
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

        List<HashMap<String, Object>> returned2 = new ArrayList<>();

        try {
            List<HashMap<String, Object>> returned = connector.executeQuery("SELECT * FROM user WHERE user_id = " + userId);
            if(returned.size() == 0) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("result", "Retrieval of recommended movies failed! User does not exist.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }

            List<HashMap<String, Object>> recommended = connector.executeQuery("SELECT DISTINCT movie_id FROM recommendation WHERE friend = " + userId);
            for (int i = 0; i < recommended.size(); i++) {
                mid = (Integer) recommended.get(i).values().toArray()[0];

                recommendList = connector.executeQuery("SELECT recommender FROM recommendation WHERE friend = " + userId + " AND movie_id = " + mid);
                directList = connector.executeQuery("SELECT director_id FROM direct WHERE movie_id = " + mid);
                genList = connector.executeQuery("SELECT genre_id FROM movie_genre WHERE movie_id = " + mid);


                recommenderList = new ArrayList<String>();
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

                for (int z = 0; z < recommendList.size(); z++) {
                    id = (Integer) recommendList.get(z).values().toArray()[0];
                    nameList = connector.executeQuery("SELECT full_name FROM user WHERE user_id = " + id);
                    name = (String) nameList.get(0).values().toArray()[0];
                    recommenderList.add(name);
                }

                movie.put("recommender", recommenderList);
                movie.put("title", title.values().toArray()[0]);
                movie.put("production_year", year.values().toArray()[0]);
                movie.put("overall_rating", rating.values().toArray()[0]);
                movie.put("price", price.values().toArray()[0]);
                movie.put("directors", directorList);
                movie.put("genres", genreList);
                movie.put("movie_id", mid);
                movie.put("duration", duration.values().toArray()[0]);
                movie.put("language_option", language_option.values().toArray()[0]);
                movie.put("subtitle_option", subtitle_option.values().toArray()[0]);

                returned2.add(movie);
            }
            return new ResponseEntity<>(returned2, HttpStatus.OK);
        }
        catch (Exception e) {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "Retrieval of recommended movies failed due to an exception!");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/add-favorite")
    public ResponseEntity<HashMap<String, Object>> addFavorite(@RequestBody HashMap<String, Object> requestBody) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            List<HashMap<String, Object>> returned = connector.executeQuery("SELECT * FROM user WHERE user_id = " + requestBody.get("userId"));
            if (returned.size() == 0) {
                result.put("result", "Adding a movie to favorites failed! User does not exist.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            returned = connector.executeQuery("SELECT * FROM movie WHERE movie_id = " + requestBody.get("movieId"));
            if (returned.size() == 0) {
                result.put("result", "Adding a movie to favorites failed! Movie you are trying to favorite does not exist.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            returned = connector.executeQuery("SELECT * FROM favorite WHERE user_id = " + requestBody.get("userId") + " AND  movie_id = " + requestBody.get("movieId"));
            if (returned.size() != 0) {
                result.put("result", "Adding a movie to favorites failed! Movie is already in your favorites.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            connector.executeUpdate("INSERT INTO favorite VALUES (" + requestBody.get("userId") + ", " + requestBody.get("movieId") + ")");
            result.put("result", "Adding a movie to favorites is successful!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (Exception e) {
            result.put("result", "Adding a movie to favorites failed due to an exception!");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete-favorite")
    public ResponseEntity<HashMap<String, Object>> deleteFavorite(@RequestBody HashMap<String, Object> requestBody) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            List<HashMap<String, Object>> returned = connector.executeQuery("SELECT * FROM user WHERE user_id = " + requestBody.get("userId"));
            if (returned.size() == 0) {
                result.put("result", "Deleting a movie fom favorites failed! User does not exist.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            returned = connector.executeQuery("SELECT * FROM movie WHERE movie_id = " + requestBody.get("movieId"));
            if (returned.size() == 0) {
                result.put("result", "Deleting a movie fom favorites failed! Movie you are trying to delete from favorites does not exist.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            returned = connector.executeQuery("SELECT * FROM favorite WHERE user_id = " + requestBody.get("userId") + " AND  movie_id = " + requestBody.get("movieId"));
            if (returned.size() == 0) {
                result.put("result", "Deleting a movie fom favorites failed! Movie is not in your favorites.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            connector.executeUpdate("DELETE FROM favorite WHERE user_id = " + requestBody.get("userId") + " AND movie_id = " + requestBody.get("movieId"));
            result.put("result", "Deleting a movie fom favorites is successful!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (Exception e) {
            result.put("result", "Deleting a movie fom favorites failed due to an exception!");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-favorite-list")
    public ResponseEntity<?> getFavoriteList(@RequestParam("userId") Integer userId) {
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



        List<HashMap<String, Object>> returned2 = new ArrayList<>();

        try {
            List<HashMap<String, Object>> returned = connector.executeQuery("SELECT * FROM user WHERE user_id = " + userId);
            if (returned.size() == 0) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("result", "Retrieval of favorite movies failed! User does not exist.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            List<HashMap<String, Object>> favorites = connector.executeQuery("SELECT movie_id FROM favorite WHERE user_id = " + userId);
            for (int i = 0; i < favorites.size(); i++) {
                mid = (Integer) favorites.get(i).values().toArray()[0];
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
                movie.put("movie_id", mid);
                movie.put("duration", duration.values().toArray()[0]);
                movie.put("language_option", language_option.values().toArray()[0]);
                movie.put("subtitle_option", subtitle_option.values().toArray()[0]);

                returned2.add(movie);
            }
            return new ResponseEntity<>(returned2, HttpStatus.OK);
        } catch (Exception e) {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "Retrieval of favorite movies failed due to an exception!");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/write-review")
    public ResponseEntity<HashMap<String, Object>> writeReview(@RequestBody HashMap<String, Object> requestBody) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            List<HashMap<String, Object>> returned = connector.executeQuery("SELECT * FROM rent_movie WHERE movie_id = " + requestBody.get("movieId") + " and user_id = " + requestBody.get("userId"));
            if (returned.size() == 0) {
                returned = connector.executeQuery("SELECT * FROM movie WHERE movie_id = " + requestBody.get("movieId"));
                if (returned.size() == 0) {
                    result.put("result", "Review failed! Movie does not exist");
                    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                }
            }
            connector.executeUpdate("INSERT INTO review VALUES(" + requestBody.get("userId") + ", " + requestBody.get("movieId") + ", '" + requestBody.get("reviewComment") + "')");
            result.put("result", "Review successful!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.put("result", "Review failed due to an exception!");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/rate")
    public ResponseEntity<HashMap<String, Object>> rate(@RequestBody HashMap<String, Object> requestBody) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            connector.executeUpdate("INSERT INTO rating(user_id, movie_id, rating) VALUES(" + requestBody.get("userId") + ", " + requestBody.get("movieId") + ", " + requestBody.get("ratingScore") + ")");
            result.put("result", "The movie is rated.");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.put("result", "Failure due to exception.");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-reviews-and-ratings")
    public ResponseEntity<List<HashMap<String, Object>>> getReviewsAndRatings(@RequestParam("userId") Integer userId) {
        List<HashMap<String, Object>> result = new ArrayList<>();
        String query = "SELECT movie_id, title, rating, review FROM rating NATURAL JOIN review NATURAL JOIN movie WHERE user_id = " + userId + ";";
        try {
            result = connector.executeQuery(query);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/show-movie-history")
    public ResponseEntity<?> showMovieHistory(@RequestParam("userId") Integer userId) {
        HashMap<String, Object> result = new HashMap<>();
        String query = "SELECT movie_id, title, production_year, price, rent_date, last_renew_date, renew_times FROM movie NATURAL JOIN rent_movie NATURAL JOIN rent WHERE user_id = " + userId + " and withdrawn = true";
        try {
            List<HashMap<String, Object>> returned = connector.executeQuery(query);
            return new ResponseEntity<>(returned, HttpStatus.OK);
        } catch (Exception e) {
            result.put("result", "Failure due to exception.");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

}
