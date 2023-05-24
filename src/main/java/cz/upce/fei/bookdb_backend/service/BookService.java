package cz.upce.fei.bookdb_backend.service;

import cz.upce.fei.bookdb_backend.domain.AppUser;
import cz.upce.fei.bookdb_backend.domain.Book;
import cz.upce.fei.bookdb_backend.domain.Review;
import cz.upce.fei.bookdb_backend.dto.ReviewRequestDtoV1;
import cz.upce.fei.bookdb_backend.exception.ResourceNotFoundException;
import cz.upce.fei.bookdb_backend.repository.AppUserRepository;
import cz.upce.fei.bookdb_backend.repository.BookRepository;
import cz.upce.fei.bookdb_backend.repository.ReviewRepository;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final AppUserRepository appUserRepository;

    @Transactional(readOnly = true)
    public Book findById(final Long id) throws ResourceNotFoundException {
        Optional<Book> result = bookRepository.findById(id);

        if (result.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        return result.get();
    }

    @Transactional(readOnly = true)
    public List<Book> findAll(@Nullable Specification<Book> specification, Pageable pageable) {
        return bookRepository.findAll(specification, pageable).getContent();
    }

    @Transactional(readOnly = true)
    public List<Book> findAllBy(Specification<Book> specification, Pageable pageable) {
        return bookRepository.findAll(specification, pageable).getContent();
    }

    @Transactional(readOnly = true)
    public List<Review> findAllReviewByBookId(final Long bookId) {
        return reviewRepository.findAllByBookId(bookId);
    }

    @Transactional(readOnly = true)
    public long getBookReviewCount(final Long bookId) {
        return reviewRepository.countAllByBookId(bookId);
    }

    @Transactional(readOnly = true)
    public long countAllReviewsByBookId(final Long bookId) {
        return reviewRepository.countAllByBookId(bookId);
    }

    public void create(final Book book) {
        log.info("Saving new book with title {} to the database.", book.getTitle());
        bookRepository.save(book);
    }

    public void update(final Book book) {
        log.info("Saving updated book with title {} to the database.", book.getTitle());
        bookRepository.save(book);
    }

    public void delete(final Long id) {
        log.info("Deleting book with id {}.", id);
        bookRepository.deleteById(id);
    }

    public void addReviewToBook(ReviewRequestDtoV1 reviewDto, String username, Long bookId) {
        AppUser user = appUserRepository.findByEmail(username);
        if(user == null) {
            log.error("User not found in the database.");
        }

        Optional<Book> book = bookRepository.findById(bookId);
        if(!book.isPresent()) {
            log.error("Book not found in the database.");
        }

        Review review = new Review(null, reviewDto.getText(), reviewDto.getRating(), LocalDateTime.now(), user, book.get());
        reviewRepository.save(review);
    }
}
