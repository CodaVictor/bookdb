package cz.upce.fei.bookdb_backend.repository;

import cz.upce.fei.bookdb_backend.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByBookId(final Long id);

    Optional<Review> findByIdAndUserId(final Long reviewId, final Long userId);
    Optional<Review> findByIdAndBookId(final Long reviewId, final Long bookId);

    Optional<Review> findByIdBookIdAndUserId(final Long reviewId, final Long bookId, final Long userId);

    @Query("SELECT COUNT(*) FROM Review WHERE book_id = ?1 AND user_id = ?2 LIMIT 1")
    boolean existsByBookIdAndUserId(final Long bookId, final Long userId);

    // Gets review count of the book with specified id
    @Query("SELECT COUNT(*) FROM Review WHERE book_id = ?1")
    long countAllByBookId(final Long id);

    @Query("SELECT AVG(rating) FROM Review WHERE book_Id = ?1")
    Long getReviewAvgRatingByBookId(final Long id);
}
