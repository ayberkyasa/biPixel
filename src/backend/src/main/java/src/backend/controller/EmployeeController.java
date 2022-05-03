package src.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import src.backend.connector.Connector;

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
    public HashMap<String, Object> addMovie(@RequestBody HashMap<String, Object> requestBody) {
        // TODO: From RequestBody, all information about movie will come. Check ER Diagram from movie table.
        return null;
    }

    @DeleteMapping("/remove-movie")
    public HashMap<String, Object> removeMovie(@RequestParam("movieId") Integer movieId) {
        // TODO: Delete the movie specified by "movieId"
        return null;
    }

    @DeleteMapping("/delete-customer")
    public HashMap<String, Object> deleteCustomer(@RequestBody HashMap<String, Object> requestBody) {
        // TODO: From RequestBody, "userId" will come
        return null;
    }

    @GetMapping("/get-all-customers")
    public List<HashMap<String, Object>> getAllCustomers() {
        // TODO: return all customers
        return null;
    }
}
