package com.yy.petfinder.service;

import com.yy.petfinder.rest.model.Login;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class LoginService {
  private PasswordEncoder passwordEncoder;
  private UserDetailsService userDetailsService;
  private TokenService tokenService;

  public Mono<JWTToken> authenticate(final Login login) {
    final Mono<UserDetails> authenticatedUser =
        userDetailsService
            .findByUsername(login.getEmail())
            .filter(user -> passwordEncoder.matches(login.getPassword(), user.getPassword()))
            .switchIfEmpty(
                Mono.error(new BadCredentialsException("email or password is incorrect")));
    return authenticatedUser
        .map(UserDetails::getUsername)
        .map(tokenService::createToken)
        .map(JWTToken::new);
  }
}