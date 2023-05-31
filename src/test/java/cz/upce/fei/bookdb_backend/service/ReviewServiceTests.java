package cz.upce.fei.bookdb_backend.service;

import cz.upce.fei.bookdb_backend.domain.AppUser;
import cz.upce.fei.bookdb_backend.domain.Book;
import cz.upce.fei.bookdb_backend.domain.Review;
import cz.upce.fei.bookdb_backend.dto.BookResponseDtoV1;
import cz.upce.fei.bookdb_backend.dto.ReviewRequestDtoV1;
import cz.upce.fei.bookdb_backend.dto.ReviewResponseDtoV1;
import cz.upce.fei.bookdb_backend.exception.ConflictEntityException;
import cz.upce.fei.bookdb_backend.exception.ResourceNotFoundException;
import cz.upce.fei.bookdb_backend.repository.AppUserRepository;
import cz.upce.fei.bookdb_backend.repository.BookRepository;
import cz.upce.fei.bookdb_backend.repository.ReviewRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTests {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private ReviewService reviewService;

    private Book book;
    private Review review;
    private AppUser appUser;
    private BookResponseDtoV1 bookResponseDto;
    private ReviewRequestDtoV1 reviewRequestDto;

    @BeforeEach
    public void init() {
        book = new Book();
        book.setTitle("Book 1");
        book.setIsbn("1-1");

        bookResponseDto = new BookResponseDtoV1();
        bookResponseDto.setTitle("Book 1");
        bookResponseDto.setIsbn("1-1");

        review = new Review();
        review.setText("Text");
        review.setRating(5F);

        reviewRequestDto = new ReviewRequestDtoV1();
        reviewRequestDto.setText("Text");
        reviewRequestDto.setRating(5F);

        appUser = new AppUser();
        appUser.setEmail("test@email.cz");
    }

    @Test
    public void ReviewService_CreateReview_ReturnReviewResponseDto() throws ConflictEntityException, ResourceNotFoundException {
        Mockito.when(appUserRepository.findByEmail(appUser.getEmail())).thenReturn(Optional.of(appUser));
        Mockito.when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        Mockito.when(reviewRepository.countByBookIdAndUserId(book.getId(), appUser.getId())).thenReturn(0L);

        // Mock static security util
        MockedStatic<SecurityContextHolder> contextHolderMockedStatic = Mockito.mockStatic(SecurityContextHolder.class);
        contextHolderMockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(appUser.getEmail());

        Mockito.when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(review);

        Review savedReview = reviewService.create(reviewRequestDto, book.getId());

        Assertions.assertNotNull(savedReview);
    }

    @Test
    public void ReviewService_GetReviewById_ReturnReviewDto() throws ResourceNotFoundException {
        long reviewId = 1;
        long bookId = 1;

        review.setBook(book);

        Mockito.when(reviewRepository.findByIdAndBookId(reviewId, bookId)).thenReturn(Optional.of(review));

        Review savedReview = reviewService.findByReviewIdAndBookId(reviewId, bookId);

        Assertions.assertNotNull(savedReview);
    }
}
