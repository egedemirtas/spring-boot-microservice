package com.beginner.rest.webservices.restfulwebservices.helloworld;

import com.beginner.rest.webservices.restfulwebservices.socialmedia.dao.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController // makes this REST API
@RequestMapping("basic")
public class HelloWorldController {

    @RequestMapping(method = RequestMethod.GET, path = "/hello-world")
    public String helloWorld() {
        return "Hello world";
    }

    // better way of doing GET mapping is to use @GetMapping
    @GetMapping(path = "/hello-earth")
    public String helloEarth() {
        return "Hello earth";
    }

    @GetMapping(path = "/users")
    public List<User> getUsers() {
        return List.of(new User(1, "Ege", LocalDate.now()));
    }

    // using path parameters
    @GetMapping(path = "/users/{id}")
    public User getUserById(@PathVariable Integer id) {
        return new User(id, "Ege" + id, LocalDate.now());
    }

}
