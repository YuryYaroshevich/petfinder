package com.yy.petfinder.rest.model;

import com.yy.petfinder.model.PetType;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class PetAdView {
  private String uuid;
  @NonNull private PetType petType;
  private String color;
  @NonNull private String name;
  @NonNull private byte[] imageBlob;
  @NonNull private SearchAreaView searchArea;
  @NonNull private String ownerId;
}
