package com.yy.petfinder.persistence;

import com.yy.petfinder.model.PetAd;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PetAdRepository
    extends ReactiveCrudRepository<PetAd, String>, PetAdRepositoryCustom {
  Flux<PetAd> findPetAdsByOwnerId(String ownerId);

  Mono<PetAd> findByIdAndOwnerId(String id, String ownerId);
}
