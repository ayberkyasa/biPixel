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
    public ResponseEntity<HashMap<String, Object>> login(@RequestBody HashMap<String, Object> requestBody) {
        HashMap<String, Object> response = new HashMap<>();
        String loginQuery = "SELECT * FROM user " +
                "WHERE email = '" + requestBody.get("email") + "' " + "and password = '" + requestBody.get("password") + "';";

        List<HashMap<String, Object>> user = connector.executeQuery(loginQuery);
        if(!user.isEmpty()){
            Integer userId = (Integer) user.get(0).get("user_id");
            response.put("id", userId);
            String empQuery = "SELECT * FROM employee " +
                    "WHERE user_id = " + userId + ";";
            List<HashMap<String, Object>> emp = connector.executeQuery(empQuery);
            if(!emp.isEmpty()){
                response.put("userType", "Employee");
            }else{
                response.put("userType", "Customer");
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/register")
    public ResponseEntity<HashMap<String, Object>> register(@RequestBody HashMap<String, Object> requestBody) {
        HashMap<String, Object> response = new HashMap<>();

        String userCheckQuery = "SELECT * FROM user WHERE email = '" + requestBody.get("email") + "';";
        List<HashMap<String, Object>> user = connector.executeQuery(userCheckQuery);

        if(!user.isEmpty()){
            response.put("message", "User exists");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }else{
            response.put("message", "User does not exist");
        }

        String userQuery = "INSERT INTO user (full_name, birth_date, email, password) " +
                "VALUES (" +
                "'" + requestBody.get("fullName") + "'" + "," +
                "'" + requestBody.get("birthDate") + "'" + "," +
                "'" + requestBody.get("email") + "'" + "," +
                "'" + requestBody.get("password") + "'" +
                ");";

        connector.executeUpdate(userQuery);

        String getUserQuery = "SELECT * FROM user WHERE email = '" + requestBody.get("email") + "';";
        Integer user_id = (Integer) connector.executeQuery(getUserQuery).get(0).get("user_id");

        String customerQuery = "INSERT INTO customer (user_id, status) " +
                "VALUES (" +
                user_id + "," +
                "'new'" +
                ");";
        connector.executeUpdate(customerQuery);
        response.put("message", "Register was successfully done.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
