package src.backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import src.backend.connector.Connector;

import java.util.HashMap;
import java.util.List;

@RestController
public class LoginRegisterController {
    private final Connector connector;

    @Autowired
    public LoginRegisterController(Connector connector) {
        this.connector = connector;
    }

    @PostMapping("/login")
    public List<HashMap<String, Object>> login(@RequestBody HashMap<String, Object> requestBody) {
        try {
            return connector.executeQuery("SELECT * FROM student WHERE sid = " + requestBody.get("id") + " and sname = '" + requestBody.get("name") + "'");
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/register")
    public ResponseEntity<HashMap<String, Object>> register(@RequestBody HashMap<String, Object> requestBody) {
        // TODO: Register, check if the user already exists. Input validations will be done in frontend.
        HashMap<String, Object> response = new HashMap<>();

        String userCheckQuery = "SELECT * FROM user WHERE email = '" + requestBody.get("email") + "';";
        List<HashMap<String, Object>> user = connector.executeQuery(userCheckQuery);

        if(!user.isEmpty()){
            response.put("message", "User exists");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }else{
            response.put("message", "User does not exist");
        }

        String userQuery = "";
        if(requestBody.get("middle_name") == null){
             userQuery = "INSERT INTO user (first_name, middle_name, last_name, birth_date, email, password) " +
                    "VALUES (" +
                    "'" + requestBody.get("first_name") + "'" + "," +
                    requestBody.get("middle_name") + "," +
                    "'" + requestBody.get("last_name") + "'" + "," +
                    "'" + requestBody.get("birth_date") + "'" + "," +
                    "'" + requestBody.get("email") + "'" + "," +
                    requestBody.get("password") +
                    ");";
        }else {
            userQuery = "INSERT INTO user (first_name, middle_name, last_name, birth_date, email, password) " +
                    "VALUES (" +
                    "'" + requestBody.get("first_name") + "'" + "," +
                    "'" + requestBody.get("middle_name") + "'" + "," +
                    "'" + requestBody.get("last_name") + "'" + "," +
                    "'" + requestBody.get("birth_date") + "'" + "," +
                    "'" + requestBody.get("email") + "'" + "," +
                    requestBody.get("password") +
                    ");";
        }

        connector.executeUpdate(userQuery);

        String getUserQuery = "SELECT * FROM user WHERE email = '" + requestBody.get("email") + "';";
        Integer user_id = (Integer) connector.executeQuery(getUserQuery).get(0).get("user_id");

        String customerQuery = "INSERT INTO customer (user_id, status) " +
                "VALUES (" +
                user_id + "," +
                "'new'" +
                ");";
        connector.executeUpdate(customerQuery);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
