package cz.upce.fei.bookdb_backend.service;

import cz.upce.fei.bookdb_backend.domain.Book;
import cz.upce.fei.bookdb_backend.domain.Review;
import cz.upce.fei.bookdb_backend.exception.ResourceNotFoundException;
import cz.upce.fei.bookdb_backend.repository.BookRepository;
import cz.upce.fei.bookdb_backend.repository.ReviewRepository;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;

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

    @Transactional
    public Book create(final Book book) {
        return bookRepository.save(book);
    }

    @Transactional
    public Book update(final Book toEntity) {
        return bookRepository.save(toEntity);
    }

    @Transactional
    public void delete(final Long id) {
        bookRepository.deleteById(id);
    }
}
