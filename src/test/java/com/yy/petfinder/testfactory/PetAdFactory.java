package com.yy.petfinder.testfactory;

import com.yy.petfinder.model.PetAd;
import com.yy.petfinder.model.PetType;
import com.yy.petfinder.model.SearchArea;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.bson.types.ObjectId;

public final class PetAdFactory {
  public static PetAd.PetAdBuilder petAdBuilderWithDefaults() {
    final String id = new ObjectId().toHexString();
    final SearchArea searchArea =
        SearchArea.of(
            List.of(
                List.of(27.417068481445312, 53.885826945065915),
                List.of(27.420544624328613, 53.881248454798666),
                List.of(27.4273681640625, 53.884385154154224),
                List.of(27.425780296325684, 53.88805277023041),
                List.of(27.417068481445312, 53.885826945065915)));
    final PetType petType = PetType.DOG;
    final String name = "Fido";
    final String ownerId = UUID.randomUUID().toString();
    final List<String> photoUrls =
        List.of(
            "https://res.cloudinary.com/demo/image1",
            "https://res.cloudinary.com/demo/image2",
            "https://res.cloudinary.com/demo/image3");
    final List<String> colors = List.of("black");
    final String breed = "labrador";

    return PetAd.builder()
        .id(id)
        .searchArea(searchArea)
        .petType(petType)
        .name(name)
        .breed(breed)
        .ownerId(ownerId)
        .photoUrls(photoUrls)
        .colors(colors)
        .createdAt(Instant.now().truncatedTo(ChronoUnit.MILLIS));
  }
}
