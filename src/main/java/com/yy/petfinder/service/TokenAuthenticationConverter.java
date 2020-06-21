package com.yy.petfinder.service;

import static java.util.function.Predicate.not;

import java.util.List;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class TokenAuthenticationConverter implements ServerAuthenticationConverter {
  private static final String BEARER = "Bearer ";
  private static final String NO_PASSWORD = "";

  private TokenService tokenService;

  public TokenAuthenticationConverter(final TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Override
  public Mono<Authentication> convert(ServerWebExchange serverWebExchange) {
    return Mono.justOrEmpty(serverWebExchange)
        .map(this::getTokenFromRequest)
        .filter(not(String::isEmpty))
        .map(this::getAuthentication);
  }

  private String getTokenFromRequest(ServerWebExchange serverWebExchange) {
    final String token =
        serverWebExchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if (token.contains(BEARER)) {
      return token.substring(BEARER.length());
    } else {
      return Strings.EMPTY;
    }
  }

  private Authentication getAuthentication(String token) {
    final String userName = tokenService.getUserFromToken(token);
    final User user = new User(userName, NO_PASSWORD, List.of());
    return new UsernamePasswordAuthenticationToken(user, token);
  }
}
