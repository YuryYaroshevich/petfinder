package com.yy.petfinder.rest.model;

import com.yy.petfinder.model.PetType;
import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class PetAdResponse implements Identifiable {
  private String id;
  @NonNull private PetType petType;
  private List<String> colors;
  private String breed;
  @NonNull private String name;
  @NonNull private List<String> photoUrls;
  @NonNull private SearchAreaView searchArea;
  @NonNull private String ownerId;
  @NonNull private Instant createdAt;
}
