package cz.upce.fei.bookdb_backend.controller;

import cz.upce.fei.bookdb_backend.domain.*;
import cz.upce.fei.bookdb_backend.dto.BookRequestDtoV1;
import cz.upce.fei.bookdb_backend.dto.ReviewRequestDtoV1;
import cz.upce.fei.bookdb_backend.service.*;
import cz.upce.fei.bookdb_backend.values.DefaultValues;
import cz.upce.fei.bookdb_backend.exception.ResourceNotFoundException;
import cz.upce.fei.bookdb_backend.dto.BookRequestParamDtoV1;
import cz.upce.fei.bookdb_backend.dto.BookResponseDtoV1;
import cz.upce.fei.bookdb_backend.repository.specification.BookSpecification;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final ReviewService reviewService;
    private final CategoryService categoryService;
    private final PublisherService publisherService;
    private final GenreService genreService;
    private final AuthorService authorService;
    private final AppUserService appUserService;

    @GetMapping("")
    public ResponseEntity<List<BookResponseDtoV1>> findAll(
            @RequestParam(required = false) @Min(1) @Max(Integer.MAX_VALUE) Integer page,
            @RequestParam(required = false) @Min(1) @Max(512) Integer pageSize,
            @RequestBody(required = false) @Validated BookRequestParamDtoV1 parameters) {
        Sort.Direction direction = null;
        String orderBy = null;
        Specification<Book> specification = Specification.where(null);
        Integer currentPage = DefaultValues.DEFAULT_PAGE;
        Integer currentPageSize = DefaultValues.BOOK_DEFAULT_PAGE_SIZE;
        List<Book> result;

        if(page != null && pageSize != null) {
            currentPage = page - 1;
            currentPageSize = pageSize;
        }

        if(parameters != null) {
            // Sort direction (ASC/DESC)
            if(parameters.getOrderParameter() != null) {
                if (parameters.getOrderParameter().equalsIgnoreCase("ASC")) {
                    direction = Sort.Direction.ASC;
                } else if (parameters.getOrderParameter().equalsIgnoreCase("DESC")) {
                    direction = Sort.Direction.DESC;
                }
            }

            // OrderBy
            if(parameters.getOrderBy() != null) {
                orderBy = parameters.getOrderBy();
            }

            // Filters
            // Category
            if(parameters.getCategory() != null) {
                specification.and(parameters.getCategory().stream()
                        .map(categoryId -> BookSpecification.hasCategoryId(categoryId))
                        .reduce((spec1, spec2) -> spec1.and(spec2))
                        .orElse(null));
            }

            // Genre
            if(parameters.getGenre() != null) {
                specification.and(parameters.getGenre().stream()
                    .map(genreId -> BookSpecification.hasGenreId(genreId))
                    .reduce((spec1, spec2) -> spec1.and(spec2))
                    .orElse(null));
            }

            // Publisher
            if(parameters.getPublisher() != null) {
                specification.and(parameters.getPublisher().stream()
                        .map(publisherId -> BookSpecification.hasPublisherId(publisherId))
                        .reduce((spec1, spec2) -> spec1.and(spec2))
                        .orElse(null));
            }
        }

        if(direction == null || orderBy == null) {
            result = bookService.findAllBy(specification, PageRequest.of(currentPage, currentPageSize));
        } else {
            result = bookService.findAllBy(specification, PageRequest.of(currentPage, currentPageSize, direction, orderBy));
        }

        List<BookResponseDtoV1> responseBooks = result
                .stream()
                .map(Book::toDto)
                .collect(Collectors.toList());

        // Ideální by bylo vše provést v jednom dotazu, ale nevím jak
        responseBooks.forEach(bookDto -> {
            reviewService.getReviewCountOfBook(bookDto.getId());
            reviewService.getAvgRatingOfBook(bookDto.getId());
        });

        return ResponseEntity.ok(responseBooks);
    }

    @GetMapping("{bookId}")
    public ResponseEntity<BookResponseDtoV1> findById(@PathVariable Long bookId) {
        Optional<Book> book = bookService.findById(bookId);
        if(book.isPresent()) {
            Book currentBook = book.get();
            long reviewCount = reviewService.getReviewCountOfBook(currentBook.getId());
            Long avgRating = reviewService.getAvgRatingOfBook(currentBook.getId());
            BookResponseDtoV1 bookDto = currentBook.toDto();

            bookDto.setReviewCount(reviewCount);
            bookDto.setRating(avgRating);

            return ResponseEntity.ok(bookDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createBook(@RequestBody @Validated BookRequestDtoV1 bookDto) {
        try {
            Category category = null;
            if(bookDto.getCategory() != null) {
                category = categoryService.findById(bookDto.getCategory()).orElseThrow(ResourceNotFoundException::new);
            }

            Publisher publisher = null;
            if(bookDto.getPublisher() != null) {
                publisher = publisherService.findById(bookDto.getPublisher()).orElseThrow(ResourceNotFoundException::new);
            }

            Genre genre = null;
            if(bookDto.getGenre() != null) {
                genre = genreService.findById(bookDto.getGenre()).orElseThrow(ResourceNotFoundException::new);
            }

            List<Author> authors = null;
            if(bookDto.getAuthors() != null) {
                authors = bookDto.getAuthors().stream()
                    .map(authorService::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
            }

            Book book = new Book(
                null, bookDto.getTitle(), bookDto.getSubtitle(), bookDto.getIsbn(), bookDto.getLanguage(),
                bookDto.getPublicationDate(), bookDto.getPageCount(),bookDto.getDescription(), bookDto.getFilename(),
                null, publisher, authors, category, genre);

            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("").toUriString());
            return ResponseEntity.created(uri).body(book.toDto());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Failed to create book");
        }
    }

    @PutMapping("{bookId}")
    public ResponseEntity<?> updateBook(@PathVariable Long bookId, @RequestBody @Validated BookRequestDtoV1 bookDto) {
        Optional<Book> book = bookService.findById(bookId);
        if(book.isPresent()) {
            try {
                Book existingBook = book.get();

                // Update data
                existingBook.setTitle(bookDto.getTitle());
                existingBook.setSubtitle(bookDto.getSubtitle());
                existingBook.setPageCount(bookDto.getPageCount());
                existingBook.setLanguage(bookDto.getLanguage());
                existingBook.setIsbn(bookDto.getIsbn());
                existingBook.setPublicationDate(bookDto.getPublicationDate());
                existingBook.setDescription(bookDto.getDescription());
                existingBook.setFilename(bookDto.getFilename());

                // Update category
                if(bookDto.getCategory() != null) {
                    Category category = categoryService.findById(bookDto.getCategory()).orElseThrow(ResourceNotFoundException::new);
                    existingBook.setCategory(category);
                }

                // Update publisher
                if(bookDto.getPublisher() != null) {
                    Publisher publisher = publisherService.findById(bookDto.getPublisher()).orElseThrow(ResourceNotFoundException::new);
                    existingBook.setPublisher(publisher);
                }

                // Update genre
                if(bookDto.getGenre() != null) {
                    Genre genre = genreService.findById(bookDto.getGenre()).orElseThrow(ResourceNotFoundException::new);
                    existingBook.setGenre(genre);
                }

                // Update authors
                if(bookDto.getAuthors() != null) {
                    List<Author> authors = bookDto.getAuthors().stream()
                            .map(authorService::findById)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .toList();
                    existingBook.setAuthors(authors);
                }

                URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(String.format("{%d}", bookId)).toUriString());
                return ResponseEntity.created(uri).body(existingBook.toDto());
            } catch (Exception ex) {
                return ResponseEntity.badRequest().body("Failed to update book");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId) {
        Optional<Book> book = bookService.findById(bookId);
        if (book.isPresent()) {
            bookService.delete(bookId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("{bookId}/review")
    public ResponseEntity<?> createReview(@PathVariable Long bookId, @RequestBody @Validated ReviewRequestDtoV1 reviewDto) {
        try {
            // Get current logged-in user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = ((UserDetails)authentication.getPrincipal()).getUsername();

            // Get app user object
            AppUser appUser = appUserService.findUserByEmail(username).orElseThrow(ResourceNotFoundException::new);
            // Get book
            Book book = bookService.findById(bookId).orElseThrow(ResourceNotFoundException::new);

            Review review = new Review(null, reviewDto.getText(), reviewDto.getRating(), LocalDateTime.now(), appUser, book);
            reviewService.create(review);

            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(String.format("{%d}/review", bookId)).toUriString());
            return ResponseEntity.created(uri).body(null);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Failed to create review");
        }
    }

    @PutMapping("{bookId}/review/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable Long bookId, @PathVariable Long reviewId, @RequestBody @Validated ReviewRequestDtoV1 reviewDto) {
        try {
            // Get current logged-in user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = ((UserDetails)authentication.getPrincipal()).getUsername();

            // Get app user object
            AppUser appUser = appUserService.findUserByEmail(username).orElseThrow(ResourceNotFoundException::new);
            // Get book
            Book book = bookService.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found."));

            // Update review
            Review review = reviewService.findById(reviewId).orElseThrow(ResourceNotFoundException::new);
            review.setText(reviewDto.getText());
            review.setRating(reviewDto.getRating());
            reviewService.update(review);

            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(String.format("{%d}/review/{%d}", bookId, reviewId)).toUriString());
            return ResponseEntity.created(uri).body(null);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Failed to update review");
        }
    }

    @DeleteMapping("{bookId}/review/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long bookId, @PathVariable Long reviewId) {
        // Both book id and review id should exist (but review id is enough)
        Optional<Book> book = bookService.findById(bookId);
        Optional<Review> review = reviewService.findById(reviewId);

        if (book.isPresent() && review.isPresent()) {
            reviewService.delete(reviewId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
