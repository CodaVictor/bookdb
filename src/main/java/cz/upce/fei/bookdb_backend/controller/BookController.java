package cz.upce.fei.bookdb_backend.controller;

import cz.upce.fei.bookdb_backend.values.DefaultValues;
import cz.upce.fei.bookdb_backend.domain.Book;
import cz.upce.fei.bookdb_backend.exception.ResourceNotFoundException;
import cz.upce.fei.bookdb_backend.filter.BookParameters;
import cz.upce.fei.bookdb_backend.dto.BookResponseDtoV1;
import cz.upce.fei.bookdb_backend.repository.specification.BookSpecification;
import cz.upce.fei.bookdb_backend.service.BookService;
import cz.upce.fei.bookdb_backend.service.ReviewService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final ReviewService reviewService;

    @GetMapping("")
    public ResponseEntity<List<BookResponseDtoV1>> findAll(@RequestParam Integer page, @RequestParam Integer pageSize, @RequestBody @Validated BookParameters parameters) {
        Sort.Direction direction = null;
        String orderBy = null;
        Specification<Book> specification = Specification.where(null);
        int currentPage = DefaultValues.DEFAULT_PAGE;
        int currentPageSize = DefaultValues.BOOK_DEFAULT_PAGE_SIZE;
        List<Book> result;

        if(page != null && pageSize != null) {
            currentPage = page;
            currentPageSize = pageSize;
        }

        if(parameters != null) {
            if(parameters.getPage() != null) {
                currentPage = parameters.getPage() - 1;
            }

            if(parameters.getPageSize() != null) {
                currentPageSize = parameters.getPageSize();
            }

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

    /*
    @GetMapping("")
    public ResponseEntity<List<BookResponseDtoV1>> findAll() {
        List<Book> result = bookService.findAll(null, PageRequest.of(DefaultValues.DEFAULT_PAGE, DefaultValues.BOOK_DEFAULT_PAGE_SIZE));

        List<BookResponseDtoV1> responseBooks = result
                .stream()
                .map(Book::toDto)
                .collect(Collectors.toList());

        responseBooks.forEach(bookDto -> {
            reviewService.getReviewCountOfBook(bookDto.getId());
            reviewService.getAvgRatingOfBook(bookDto.getId());
        });

        return ResponseEntity.ok(responseBooks);
    }
    */

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDtoV1> findById(@PathVariable Long id) throws ResourceNotFoundException {
        Book book = bookService.findById(id);
        long reviewCount = reviewService.getReviewCountOfBook(book.getId());
        Long avgRating = reviewService.getAvgRatingOfBook(book.getId());
        BookResponseDtoV1 bookDto = bookService.findById(id).toDto();

        bookDto.setReviewCount(reviewCount);
        bookDto.setRating(avgRating);

        return ResponseEntity.ok(bookDto);
    }
}
