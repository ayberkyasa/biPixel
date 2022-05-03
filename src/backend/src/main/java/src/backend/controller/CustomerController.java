package src.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import src.backend.connector.Connector;

import java.util.HashMap;
import java.util.List;

@RestController
public class CustomerController {
    private final Connector connector;

    @Autowired
    public CustomerController(Connector connector) {
        this.connector = connector;
    }

    @PostMapping("/request-movie")
    public HashMap<String, Object> requestMovie(@RequestBody HashMap<String, Object> requestBody) {
        // TODO: From RequestBody, all information about movie will come. Check ER Diagram from requested_movie table.
        return null;
    }
}
