package com.yy.petfinder.security.service;

import static com.yy.petfinder.model.User.PASSWORD_PLACEHOLDER;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yy.petfinder.exception.OAuth2FlowException;
import com.yy.petfinder.model.OAuth2Provider;
import com.yy.petfinder.model.User;
import com.yy.petfinder.persistence.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.userinfo.DefaultReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CustomOAuth2UserService extends DefaultReactiveOAuth2UserService {
  private final UserRepository userRepository;

  @Autowired
  public CustomOAuth2UserService(UserRepository userRepository, ObjectMapper objectMapper) {
    this.userRepository = userRepository;
    setWebClient(
        WebClient.builder()
            .filter(
                ExchangeFilterFunction.ofResponseProcessor(
                    response ->
                        response
                            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                            .map(
                                respBody -> {
                                  if (respBody.containsKey("response")) {
                                    return ((List) respBody.get("response")).get(0);
                                  }
                                  return respBody;
                                })
                            .map(
                                data -> {
                                  try {
                                    return objectMapper.writeValueAsString(data);
                                  } catch (JsonProcessingException e) {
                                    throw new OAuth2FlowException();
                                  }
                                })
                            .map(data -> ClientResponse.from(response).body(data).build())))
            .build());
  }

  @Override
  public Mono<OAuth2User> loadUser(OAuth2UserRequest userRequest) {
    final String registrationId = userRequest.getClientRegistration().getRegistrationId();
    final Optional<OAuth2Provider> oAuth2ProviderOpt = OAuth2Provider.of(registrationId);
    return oAuth2ProviderOpt
        .map(
            oAuth2Provider -> {
              final Mono<OAuth2User> oAuth2User = super.loadUser(userRequest);
              return oAuth2User.flatMap(
                  oauthData -> {
                    final String email = oauthData.getAttribute("email");
                    return userRepository
                        .findByEmail(email)
                        .map(user -> user.toBuilder().oAuth2Provider(oAuth2Provider).build())
                        .flatMap(user -> userRepository.save(user))
                        .switchIfEmpty(
                            userRepository.save(
                                User.builder()
                                    .id(new ObjectId().toHexString())
                                    .email(email)
                                    .password(PASSWORD_PLACEHOLDER)
                                    .oAuth2Provider(oAuth2Provider)
                                    .build()))
                        .map(ignore -> oauthData);
                  });
            })
        .orElseThrow(
            () ->
                new OAuth2AuthenticationException(
                    new OAuth2Error(
                        "unsupported_oauth2_provider",
                        "Only Google and Facebook are currently supported",
                        null)));
  }
}
