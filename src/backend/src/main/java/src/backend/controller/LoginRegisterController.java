package src.backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
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
    public HashMap<String, Object> register(@RequestBody HashMap<String, Object> requestBody) {
        // TODO: Register, check if the user already exists. Input validations will be done in frontend.
        return null;
    }
}
