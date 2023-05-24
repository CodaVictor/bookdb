package cz.upce.fei.bookdb_backend.repository;

import cz.upce.fei.bookdb_backend.domain.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends PagingAndSortingRepository<Review, Long> {

    List<Review> findAllByBookId(final Long id);


    // Gets review count of the book with specified id
    @Query("SELECT COUNT(*) FROM Review WHERE book_id = ?1")
    long countAllByBookId(final Long id);

    @Query("SELECT AVG(rating) FROM Review WHERE book_Id = ?1")
    Long getReviewAvgRatingByBookId(final Long id);
}
