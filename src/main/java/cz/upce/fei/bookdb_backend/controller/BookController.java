package cz.upce.fei.bookdb_backend.controller;

import cz.upce.fei.bookdb_backend.domain.Book;
import cz.upce.fei.bookdb_backend.dto.BookRequestDtoV1;
import cz.upce.fei.bookdb_backend.dto.BookFilterParameters;
import cz.upce.fei.bookdb_backend.dto.BookResponseDtoV1;
import cz.upce.fei.bookdb_backend.exception.ConflictEntityException;
import cz.upce.fei.bookdb_backend.exception.ResourceNotFoundException;
import cz.upce.fei.bookdb_backend.repository.specification.BookSpecification;
import cz.upce.fei.bookdb_backend.service.*;
import cz.upce.fei.bookdb_backend.values.DefaultValues;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.net.URI;
import java.util.List;

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

    @GetMapping("")
    public ResponseEntity<List<BookResponseDtoV1>> findAll(
            @RequestParam(required = false) @Min(1) @Max(Integer.MAX_VALUE) Integer page,
            @RequestParam(required = false) @Min(1) @Max(512) Integer pageSize,
            @RequestBody(required = false) @Validated BookFilterParameters parameters) {
        Specification<Book> specification = Specification.where(null);
        Integer currentPage = DefaultValues.DEFAULT_PAGE;
        Integer currentPageSize = DefaultValues.BOOK_DEFAULT_PAGE_SIZE;
        Pageable pageable;
        Page<Book> booksPage;

        // Set page and page size
        if(page != null && pageSize != null) {
            currentPage = page - 1;
            currentPageSize = pageSize;
        }

        pageable = PageRequest.of(currentPage, currentPageSize);

        // Set parameters
        if(parameters != null) {
            // Sort direction (ASC/DESC)
            if(parameters.getOrderDirection() != null && parameters.getOrderBy() != null) {
                // Sort direction (ASC or DESC) and sorting field (name)
                Sort sort = Sort.by(Sort.Direction.fromString(parameters.getOrderDirection()), parameters.getOrderBy());
                // Create pageable object
                pageable = PageRequest.of(currentPage, currentPageSize, sort);
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

        booksPage = bookService.findAllBy(specification, pageable);

        List<BookResponseDtoV1> responseBooks = booksPage.getContent()
                .stream()
                .map(Book::toDto)
                .toList();

        // Ideální by bylo vše provést v jednom dotazu, ale nevím jak
        responseBooks.forEach(bookDto -> {
            reviewService.getReviewCountOfBook(bookDto.getId());
            reviewService.getAvgRatingOfBook(bookDto.getId());
        });

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(booksPage.getTotalElements()))
                .header("X-Total-Pages", String.valueOf(booksPage.getTotalPages()))
                .body(responseBooks);
    }

    @GetMapping("{bookId}")
    public ResponseEntity<BookResponseDtoV1> findById(@PathVariable Long bookId) throws ResourceNotFoundException {
        Book book = bookService.findById(bookId);
        long reviewCount = reviewService.getReviewCountOfBook(bookId);
        Long avgRating = reviewService.getAvgRatingOfBook(bookId);

        BookResponseDtoV1 bookDto = book.toDto();
        bookDto.setReviewCount(reviewCount);
        bookDto.setRating(avgRating);

        return ResponseEntity.ok(bookDto);
    }

    @PostMapping("")
    public ResponseEntity<BookResponseDtoV1> createBook(@RequestBody @Validated BookRequestDtoV1 bookDto)
            throws ResourceNotFoundException, ConflictEntityException {
        Book book = bookService.create(bookDto);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("").toUriString());
        return ResponseEntity.created(uri).body(book.toDto());

    }

    @PutMapping("{bookId}")
    public ResponseEntity<BookResponseDtoV1> updateBook(@PathVariable Long bookId, @RequestBody @Validated BookRequestDtoV1 bookDto)
            throws ResourceNotFoundException, ConflictEntityException {
        Book book = bookService.update(bookDto, bookId);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(String.format("/{%d}", bookId)).toUriString());
        return ResponseEntity.created(uri).body(book.toDto());
    }

    @DeleteMapping("{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId) throws ResourceNotFoundException {
        bookService.delete(bookId);
        return ResponseEntity.noContent().build();
    }
}
