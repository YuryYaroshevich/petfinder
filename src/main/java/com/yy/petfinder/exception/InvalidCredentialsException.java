package com.yy.petfinder.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends BaseException {
  private static final int CODE = 100008;

  public InvalidCredentialsException(final String message, final HttpStatus status) {
    super(message, status, CODE);
  }

  public static final InvalidCredentialsException invalidCredentials() {
    return new InvalidCredentialsException("Invalid credentials", HttpStatus.UNAUTHORIZED);
  }

  public static final InvalidCredentialsException oldPasswordNotMatch() {
    return new InvalidCredentialsException("Old password doesn't match", HttpStatus.BAD_REQUEST);
  }
}
