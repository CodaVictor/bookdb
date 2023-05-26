package cz.upce.fei.bookdb_backend.controller;

import cz.upce.fei.bookdb_backend.domain.Book;
import cz.upce.fei.bookdb_backend.dto.BookRequestDtoV1;
import cz.upce.fei.bookdb_backend.dto.BookRequestParamDtoV1;
import cz.upce.fei.bookdb_backend.dto.BookResponseDtoV1;
import cz.upce.fei.bookdb_backend.exception.ConflictEntityException;
import cz.upce.fei.bookdb_backend.exception.ResourceNotFoundException;
import cz.upce.fei.bookdb_backend.repository.specification.BookSpecification;
import cz.upce.fei.bookdb_backend.service.*;
import cz.upce.fei.bookdb_backend.values.DefaultValues;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
import java.util.stream.Collectors;

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
    public ResponseEntity<BookResponseDtoV1> findBookById(@PathVariable Long bookId) throws ResourceNotFoundException {
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
