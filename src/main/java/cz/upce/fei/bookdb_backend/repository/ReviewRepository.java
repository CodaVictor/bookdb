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

    @Query("SELECT r FROM Review r WHERE r.id = ?1 AND r.user.id = ?2")
    Optional<Review> findByIdAndUserId(final Long reviewId, final Long userId);

    @Query(value = "SELECT r FROM Review r WHERE r.id = ?1 AND r.book.id = ?2")
    Optional<Review> findByIdAndBookId(final Long reviewId, final Long bookId);

    @Query(value = "SELECT r FROM Review r WHERE r.id = ?1 AND r.book.id = ?2 AND r.user.id = ?3")
    Optional<Review> findByIdBookIdAndUserId(final Long reviewId, final Long bookId, final Long userId);

    @Query("SELECT COUNT(r.id) FROM Review r WHERE r.book.id = ?1 AND r.user.id = ?2")
    long countByBookIdAndUserId(final Long bookId, final Long userId);

    // Gets review count of the book with specified id
    @Query("SELECT COUNT(r.id) FROM Review r WHERE r.book.id = ?1")
    long countAllByBookId(final Long id);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = ?1")
    Float getReviewAvgRatingByBookId(final Long id);
}
