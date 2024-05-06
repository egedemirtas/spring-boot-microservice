package com.beginner.rest.webservices.restfulwebservices.socialmedia.controller;

import com.beginner.rest.webservices.restfulwebservices.socialmedia.dao.User;
import com.beginner.rest.webservices.restfulwebservices.socialmedia.dao.UserDaoService;
import com.beginner.rest.webservices.restfulwebservices.socialmedia.exception.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("user-api")
public class UserController {
    @Autowired
    UserDaoService userDaoService;

    @GetMapping("/users")
    public List<User> getUsers() {
        return userDaoService.findAll();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Integer id) {
        User user = userDaoService.findById(id);
        if (user == null) {
            throw new UserNotFoundException("id: " + id);
        }
        return user;
    }

    @DeleteMapping("/users/{id}")
    public void deleteUserById(@PathVariable Integer id) {
        userDaoService.deleteById(id);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User createdUser = userDaoService.createUser(user);
        // we always think about consumer, we want to provide him a URI where he can reach the newly created User
        // this adds "Location" header to response
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()// gets current path: /users
                .path("/{id}") // adds /{id}
                .buildAndExpand(createdUser.getId()) // replace /{id}
                .toUri();
        return ResponseEntity.created(location).build();
    }

    // HATEOAS
    @GetMapping("/users/hateoas/{id}")
    public EntityModel<User> getUserByIdHATEOAS(@PathVariable Integer id) {
        User user = userDaoService.findById(id);
        if (user == null) {
            throw new UserNotFoundException("id: " + id);
        }
        EntityModel<User> entityModel = EntityModel.of(user); // need to wrap the object to add link
        WebMvcLinkBuilder link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getUsers()); //
        // link will be getUsers() method of this class
        entityModel.add(link.withRel("all-users"));
        return entityModel;
    }
}
