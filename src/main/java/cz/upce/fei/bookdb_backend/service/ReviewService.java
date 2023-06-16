package cz.upce.fei.bookdb_backend.service;

import cz.upce.fei.bookdb_backend.domain.AppUser;
import cz.upce.fei.bookdb_backend.domain.Book;
import cz.upce.fei.bookdb_backend.domain.Review;
import cz.upce.fei.bookdb_backend.dto.ReviewRequestDtoV1;
import cz.upce.fei.bookdb_backend.repository.AppUserRepository;
import cz.upce.fei.bookdb_backend.repository.BookRepository;
import cz.upce.fei.bookdb_backend.repository.ReviewRepository;
import cz.upce.fei.bookdb_backend.service.exception.ConflictEntityException;
import cz.upce.fei.bookdb_backend.service.exception.ResourceNotFoundException;
import cz.upce.fei.bookdb_backend.service.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final AppUserRepository appUserRepository;

    @Transactional(readOnly = true)
    public Review findById(Long id) throws ResourceNotFoundException {
        return reviewRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Review not found."));
    }

    @Transactional(readOnly = true)
    public Review findByReviewIdAndBookId(final Long reviewId, final Long bookId) throws ResourceNotFoundException {
        return reviewRepository.findByIdAndBookId(reviewId, bookId).orElseThrow(()-> new ResourceNotFoundException("Reviews not found."));
    }

    @Transactional(readOnly = true)
    public List<Review> findAllByBookId(final Long bookId) {
        return reviewRepository.findAllByBookId(bookId);
    }

    @Transactional(readOnly = true)
    public long getReviewCountOfBook(final Long bookId) {
        return reviewRepository.countAllByBookId(bookId);
    }

    @Transactional(readOnly = true)
    public Float getAvgRatingOfBook(final Long bookId) {
        return reviewRepository.getReviewAvgRatingByBookId(bookId);
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public Review create(final ReviewRequestDtoV1 reviewDto, final Long bookId) throws ResourceNotFoundException, ConflictEntityException {
        // Get current logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();

        AppUser user = appUserRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User not found."));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found."));

        if(reviewRepository.countByBookIdAndUserId(book.getId(), user.getId()) > 0) {
            throw new ConflictEntityException(String.format("User '%s' already wrote review to the book '%s'", user.getEmail(), book.getTitle()));
        }

        Review review = new Review(null, reviewDto.getText(), reviewDto.getRating(), LocalDateTime.now(), user, book);

        log.info("Saving new review to book {}, with user {}, to the database.", review.getBook().getTitle(), review.getUser().getEmail());
        return reviewRepository.save(review);
    }

    public Review update(final ReviewRequestDtoV1 reviewDto, final Long reviewId, final Long bookId) throws ResourceNotFoundException {
        // Get current logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();

        AppUser user = appUserRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User not found."));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found."));

        Review review = reviewRepository.findByIdBookIdAndUserId(reviewId, bookId, user.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Review of the book and of current user not found."));

        review.setText(reviewDto.getText());
        review.setRating(review.getRating());

        log.info("Saving updated review to book {}, with user {}, to the database.", review.getBook().getTitle(), review.getUser().getEmail());
        return reviewRepository.save(review);
    }

    public void delete(final Long bookId, final Long reviewId) throws ResourceNotFoundException, UnauthorizedAccessException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();

        AppUser user = appUserRepository.findByEmail(username).orElseThrow(() -> new UnauthorizedAccessException("Unauthorized user."));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found."));
        Review review;

        // Check if current user (non review creator) is authorized for this operation
        boolean isAuthorized = authentication.getAuthorities().stream()
                .anyMatch((authority) -> {
                    return authority.getAuthority().equals("ROLE_EDITOR") || authority.getAuthority().equals("ROLE_ADMIN");
                });
        if(isAuthorized) {
            // Only reviewId is enough
            review = reviewRepository.findById(reviewId).orElseThrow(() -> new ResourceNotFoundException("Review not found."));
        } else {
            review = reviewRepository.findByIdAndUserId(reviewId, user.getId()).orElseThrow(() -> new ResourceNotFoundException("Review of current user not found."));
        }

        log.info("Deleting review with id {}.", reviewId);
        reviewRepository.delete(review);
    }
}
