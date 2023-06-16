package cz.upce.fei.bookdb_backend.controller;

import cz.upce.fei.bookdb_backend.domain.Review;
import cz.upce.fei.bookdb_backend.dto.ReviewRequestDtoV1;
import cz.upce.fei.bookdb_backend.dto.ReviewResponseDtoV1;
import cz.upce.fei.bookdb_backend.service.ReviewService;
import cz.upce.fei.bookdb_backend.service.exception.ConflictEntityException;
import cz.upce.fei.bookdb_backend.service.exception.ResourceNotFoundException;
import cz.upce.fei.bookdb_backend.service.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("books/{bookId}/reviews")
    public ResponseEntity<List<ReviewResponseDtoV1>> findAllReviewsByBookId(@PathVariable Long bookId) {
        List<Review> reviews = reviewService.findAllByBookId(bookId);
        List<ReviewResponseDtoV1> reviewsDto = reviews.stream()
                .map(Review::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(reviewsDto);
    }

    @PostMapping("reviews/{bookId}")
    public ResponseEntity<ReviewResponseDtoV1> createReview(@PathVariable Long bookId, @RequestBody @Validated ReviewRequestDtoV1 reviewDto)
            throws ResourceNotFoundException, ConflictEntityException {
        Review review = reviewService.create(reviewDto, bookId);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(String.format("/reviews/{%d}", bookId)).toUriString());
        return ResponseEntity.created(uri).body(review.toDto());
    }

    @PutMapping("reviews/{bookId}/{reviewId}")
    public ResponseEntity<ReviewResponseDtoV1> updateReview(@PathVariable Long bookId, @PathVariable Long reviewId, @RequestBody @Validated ReviewRequestDtoV1 reviewDto)
            throws ResourceNotFoundException {
        Review review = reviewService.update(reviewDto, reviewId, bookId);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(String.format("/reviews/{%d}/{%d}", bookId, reviewId)).toUriString());
        return ResponseEntity.created(uri).body(review.toDto());

    }

    @DeleteMapping("reviews/{bookId}/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long bookId, @PathVariable Long reviewId)
            throws UnauthorizedAccessException, ResourceNotFoundException {
        reviewService.delete(bookId, reviewId);
        return ResponseEntity.noContent().build();

    }
}
