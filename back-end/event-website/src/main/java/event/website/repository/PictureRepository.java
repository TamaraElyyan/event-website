package event.website.repository;


import event.website.model.Picture;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PictureRepository extends CrudRepository<Picture, Integer> {
    List<Picture> findByListing(int listing);
    Optional<Picture> findByListingAndDeletedFalse(Long listing);

    List<Picture> findByListing(Integer listingId);

    // List<Picture> findByListingId(Integer listingId);
}
