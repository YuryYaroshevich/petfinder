package com.yy.petfinder.service;

import com.yy.petfinder.model.UserRandomKey;
import com.yy.petfinder.rest.model.PasswordUpdateEmail;
import java.time.Instant;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PasswdUpdateEmailService {
  private static final String NEW_PASSWORD_SUBJECT = "Password reset";
  private static final String LINK_PLACEHOLDER = "{link}";

  private final JavaMailSender emailSender;
  private final String appEmail;

  @Autowired
  public PasswdUpdateEmailService(
      final JavaMailSender emailSender, @Value("${spring.mail.username}") final String appEmail) {
    this.emailSender = emailSender;
    this.appEmail = appEmail;
  }

  public Mono<UserRandomKey> sendNewPasswordEmail(
      final PasswordUpdateEmail passwordUpdateEmail, final String userId) {

    return Mono.fromCallable(
            () -> {
              final SimpleMailMessage message = new SimpleMailMessage();
              final String randomKey = UUID.randomUUID().toString();
              message.setFrom(appEmail);
              message.setTo(passwordUpdateEmail.getEmail());
              message.setSubject(NEW_PASSWORD_SUBJECT);
              final String emailText = emailText(passwordUpdateEmail, userId, randomKey);
              message.setText(emailText);
              emailSender.send(message);
              return randomKey;
            })
        .map(
            randomKey ->
                UserRandomKey.builder()
                    .id(userId)
                    .randomKey(randomKey)
                    .createdAt(Instant.now())
                    .build());
  }

  private String emailText(
      final PasswordUpdateEmail passwordUpdateEmail, final String userId, final String randomKey) {
    final String link =
        String.format(
            "%s?key=%s&userId=%s", passwordUpdateEmail.getFrontendHost(), randomKey, userId);
    final String emailText =
        passwordUpdateEmail.getEmailMessageData().getText().replace(LINK_PLACEHOLDER, link);
    return emailText;
  }
}
