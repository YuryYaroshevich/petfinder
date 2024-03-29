package com.yy.petfinder.rest.model;

import com.yy.petfinder.model.PetType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
public class PetSearchRequest {
  private double longitude;
  private double latitude;
  private double radius;
  private PetType petType;
  private List<String> colors;
  private String breed;
}
