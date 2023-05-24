package cz.upce.fei.bookdb_backend.service;

import cz.upce.fei.bookdb_backend.domain.Author;
import cz.upce.fei.bookdb_backend.domain.Review;
import cz.upce.fei.bookdb_backend.exception.ResourceNotFoundException;
import cz.upce.fei.bookdb_backend.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public Review findById(Long id) throws ResourceNotFoundException {
        Optional<Review> result = reviewRepository.findById(id);

        if (result.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        return result.get();
    }

    @Transactional(readOnly = true)
    public long getReviewCountOfBook(final Long bookId) {
        return reviewRepository.countAllByBookId(bookId);
    }

    @Transactional(readOnly = true)
    public Long getAvgRatingOfBook(final Long bookId) {
        return reviewRepository.getReviewAvgRatingByBookId(bookId);
    }

    public List<Review> findAll() {
        return (List<Review>)reviewRepository.findAll();
    }

    public Review create(Review review) {
        log.info("Saving new review to book {}, with user {}, to the database.", review.getBook().getTitle(), review.getUser().getEmail());
        return reviewRepository.save(review);
    }

    public Review update(Review review) {
        log.info("Saving updated review to book {}, with user {}, to the database.", review.getBook().getTitle(), review.getUser().getEmail());
        return reviewRepository.save(review);
    }

    public void delete(final Long id) {
        log.info("Deleting review with id {}.", id);
        reviewRepository.deleteById(id);
    }
}
