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

    @Query("SELECT r FROM Review r WHERE id = ?1 AND user_id = ?2")
    Optional<Review> findByIdAndUserId(final Long reviewId, final Long userId);

    @Query(value = "SELECT r FROM Review r WHERE id = ?1 AND book_id = ?2")
    Optional<Review> findByIdAndBookId(final Long reviewId, final Long bookId);

    @Query(value = "SELECT r FROM Review r WHERE id = ?1 AND book_id = ?2 AND user_id = ?3")
    Optional<Review> findByIdBookIdAndUserId(final Long reviewId, final Long bookId, final Long userId);

    @Query("SELECT COUNT(*) FROM Review WHERE book_id = ?1 AND user_id = ?2")
    long countByBookIdAndUserId(final Long bookId, final Long userId);

    // Gets review count of the book with specified id
    @Query("SELECT COUNT(*) FROM Review WHERE book_id = ?1")
    long countAllByBookId(final Long id);

    @Query("SELECT AVG(rating) FROM Review WHERE book_Id = ?1")
    Long getReviewAvgRatingByBookId(final Long id);
}
