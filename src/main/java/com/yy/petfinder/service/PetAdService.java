package com.yy.petfinder.service;

import com.yy.petfinder.persistence.PetAdRepository;
import com.yy.petfinder.rest.model.PetAdView;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class PetAdService {
  private final PetAdRepository petAdRepository;

  public PetAdService(PetAdRepository petAdRepository) {
    this.petAdRepository = petAdRepository;
  }

  public Mono<PetAdView> createAd(PetAdView petAd) {
    final ObjectId objectId = new ObjectId();
    final String uuid = UUID.randomUUID().toString();

    final Mono<PetAdView> createdAd = petAdRepository.save(petAd);
    return createdAd;
  }
}
