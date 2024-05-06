package com.beginner.rest.webservices.restfulwebservices.socialmedia.dao;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDaoService {
    // in service class you need to do DB related methods that are associated with REST API

    // created static list to avoid DB implementation for now
    private static List<User> userList = new ArrayList<>();

    static {
        userList.add(new User(1, "Ege", LocalDate.now().minusYears(24L)));
        userList.add(new User(2, "Pipet", LocalDate.now().minusYears(30L)));
    }

    public List<User> findAll() {
        return userList;
    }

    public User findById(Integer id) {
        return userList.stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
    }

    public User createUser(User user) {
        userList.add(user);
        return user;
    }

    public void deleteById(Integer id) {
        userList.removeIf(user -> user.getId().equals(id));
    }
}
