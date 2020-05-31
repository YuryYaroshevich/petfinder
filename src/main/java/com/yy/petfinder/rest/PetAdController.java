package com.yy.petfinder.rest;

import com.yy.petfinder.rest.model.PetAdView;
import com.yy.petfinder.rest.model.PetSearchRequest;
import com.yy.petfinder.service.PetAdService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/pets/ad")
public class PetAdController {
  private final PetAdService petAdService;

  public PetAdController(PetAdService petAdService) {
    this.petAdService = petAdService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<PetAdView> createPetAd(@RequestBody PetAdView petAd) {
    return petAdService.createAd(petAd);
  }

  @GetMapping("/{id}")
  public Mono<PetAdView> getPetAd(@PathVariable("id") final String id) {
    return petAdService.getAd(id);
  }

  @GetMapping
  public Mono<List<PetAdView>> searchPet(final PetSearchRequest petSearchReq) {
    return petAdService.searchPets(petSearchReq);
  }

  @PutMapping("/{id}")
  public Mono<PetAdView> updatePetAd(@PathVariable String id, @RequestBody PetAdView petAdView) {
    return petAdService.updateAd(id, petAdView);
  }
}
