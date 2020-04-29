package com.yy.petfinder.rest;

import com.yy.petfinder.rest.model.CreateUser;
import com.yy.petfinder.rest.model.UserView;
import com.yy.petfinder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {
  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/{email}")
  public Mono<UserView> getUser(@PathVariable("email") final String email) {
    return userService.getUser(email);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<UserView> createUser(@RequestBody CreateUser user) {
    return userService.createUser(user);
  }
}
