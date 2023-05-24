package cz.upce.fei.bookdb_backend.service;

import cz.upce.fei.bookdb_backend.domain.Book;
import cz.upce.fei.bookdb_backend.domain.Review;
import cz.upce.fei.bookdb_backend.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewService {

    private ReviewRepository reviewRepository;

    @Transactional
    public long getReviewCountOfBook(final Long bookId) {
        return reviewRepository.countAllByBookId(bookId);
    }

    @Transactional
    public Long getAvgRatingOfBook(final Long bookId) {
        return reviewRepository.getReviewAvgRatingByBookId(bookId);
    }

    @Transactional
    public List<Review> findAll() {
        return (List<Review>)reviewRepository.findAll();
    }

    /*
    @Transactional
    public Review create(final Review review) {
        return reviewRepository.save(review);
    }

    @Transactional
    public Review update(final Review toEntity) {
        return reviewRepository.save(toEntity);
    }

    @Transactional
    public void delete(final Long id) {
        reviewRepository.deleteById(id);
    }
    */
}
