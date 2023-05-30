package cz.upce.fei.bookdb_backend.service;

import cz.upce.fei.bookdb_backend.domain.*;
import cz.upce.fei.bookdb_backend.dto.BookRequestDtoV1;
import cz.upce.fei.bookdb_backend.exception.ConflictEntityException;
import cz.upce.fei.bookdb_backend.exception.ResourceNotFoundException;
import cz.upce.fei.bookdb_backend.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final AppUserRepository appUserRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final ReviewService reviewService;

    @Transactional(readOnly = true)
    public Book findById(final Long bookId) throws ResourceNotFoundException {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found."));
        return book;
    }

    @Transactional(readOnly = true)
    public Page<Book> findAllBy(Specification<Book> specification, Pageable pageable) {
        return bookRepository.findAll(specification, pageable);
    }

    public Book create(final BookRequestDtoV1 bookDto) throws ResourceNotFoundException, ConflictEntityException {
        Book book = new Book();
        dtoToBook(bookDto, book);

        log.info("Saving new book with title {} to the database.", book.getTitle());
        return bookRepository.save(book);
    }

    public Book update(final BookRequestDtoV1 bookDto, final Long bookId) throws ResourceNotFoundException, ConflictEntityException {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        dtoToBook(bookDto, book);

        log.info("Saving updated book with title {} to the database.", book.getTitle());
        return bookRepository.save(book);
    }

    public void delete(final Long bookId) throws ResourceNotFoundException {
        boolean exists = appUserRepository.existsById(bookId);
        if(!exists) {
            throw new ResourceNotFoundException("Book not found");
        }

        log.info("Deleting book with id {}.", bookId);
        bookRepository.deleteById(bookId);
    }

    private void dtoToBook(BookRequestDtoV1 bookDto, Book book) throws ResourceNotFoundException, ConflictEntityException {
        if(bookDto.getIsbn() != null && bookRepository.existsByIsbn(bookDto.getIsbn())) {
            throw new ConflictEntityException(String.format("Book with isbn: %s already exists.", bookDto.getIsbn()));
        }

        book.setIsbn(bookDto.getIsbn());
        book.setTitle(bookDto.getTitle());
        book.setSubtitle(bookDto.getSubtitle());
        book.setPageCount(bookDto.getPageCount());
        book.setLanguage(bookDto.getLanguage());
        book.setPublicationDate(bookDto.getPublicationDate());
        book.setDescription(bookDto.getDescription());
        book.setFilename(bookDto.getFilename());

        Category category = null;
        if(bookDto.getCategory() != null) {
            category = categoryRepository.findById(bookDto.getCategory()).orElseThrow(() -> new ResourceNotFoundException("Category not found."));
        }
        book.setCategory(category);

        Publisher publisher = null;
        if(bookDto.getPublisher() != null) {
            publisher = publisherRepository.findById(bookDto.getPublisher()).orElseThrow(() -> new ResourceNotFoundException("Publisher not found."));
        }
        book.setPublisher(publisher);

        Genre genre = null;
        if(bookDto.getGenre() != null) {
            genre = genreRepository.findById(bookDto.getGenre()).orElseThrow(() -> new ResourceNotFoundException("Genre not found."));
        }
        book.setGenre(genre);

        List<Author> authors = new ArrayList<>();
        for (Long authorId : bookDto.getAuthors()) {
            Author author = authorRepository.findById(authorId).orElseThrow(() -> new ResourceNotFoundException("Author not found"));
            authors.add(author);
        }
        book.setAuthors(authors);
    }
}
