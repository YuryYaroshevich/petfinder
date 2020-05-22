package com.yy.petfinder.persistence;

import com.yy.petfinder.model.PetAd;
import com.yy.petfinder.rest.model.PetSearchRequest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;

public class PetAdRepositoryImpl implements PetAdRepositoryCustom {
  private static final String SEARCH_AREA_FIELD = "searchArea";
  private static final String PET_TYPE_FIELD = "petType";

  private final ReactiveMongoTemplate mongoTemplate;

  public PetAdRepositoryImpl(final ReactiveMongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  // TODO: paging needed
  @Override
  public Flux<PetAd> findPetAds(final PetSearchRequest petSearchReq) {
    final GeoJsonPoint point =
        new GeoJsonPoint(petSearchReq.getLongitude(), petSearchReq.getLatitude());
    final Criteria criteria =
        Criteria.where(SEARCH_AREA_FIELD).nearSphere(point).maxDistance(petSearchReq.getRadius());
    if (petSearchReq.getPetType() != null) {
      criteria.where(PET_TYPE_FIELD).is(petSearchReq.getPetType());
    }
    return mongoTemplate.find(new Query(criteria), PetAd.class);
  }
}
