package cz.upce.fei.bookdb_backend.service;

import cz.upce.fei.bookdb_backend.domain.Book;
import cz.upce.fei.bookdb_backend.dto.BookRequestDtoV1;
import cz.upce.fei.bookdb_backend.dto.BookResponseDtoV1;
import cz.upce.fei.bookdb_backend.exception.ConflictEntityException;
import cz.upce.fei.bookdb_backend.exception.ResourceNotFoundException;
import cz.upce.fei.bookdb_backend.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookServiceTests {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private PublisherRepository publisherRepository;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    public void BookService_CreateBook_ReturnResponseDto() throws ConflictEntityException, ResourceNotFoundException {
        Book book = new Book();
        book.setTitle("Book 1");
        book.setIsbn("1-1");

        BookRequestDtoV1 bookDto = new BookRequestDtoV1();
        bookDto.setTitle("Book 1");
        bookDto.setIsbn("1-1");

        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book);

        BookResponseDtoV1 savedBookResponseDto = bookService.create(bookDto).toDto();

        Assertions.assertNotNull(savedBookResponseDto);
    }

    @Test
    public void BookService_GetAllBooks_ReturnResponseDto() {
        //BookResponseDtoV1 bookResponse = Mockito.mock(BookResponseDtoV1.class);
        Page<Book> books = Mockito.mock(Page.class);

        Mockito.when(bookRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(books);

        Page<Book> savedBooks = bookService.findAllBy(Specification.where(null), PageRequest.of(1,10));

        Assertions.assertNotNull(savedBooks);
    }

    @Test
    public void BookService_GetBookById_ReturnResponseDto() throws ConflictEntityException, ResourceNotFoundException {
        Book book = new Book();
        book.setTitle("Book 1");
        book.setIsbn("1-1");

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.ofNullable(book));

        BookResponseDtoV1 savedBookResponseDto = bookService.findById(1L).toDto();

        Assertions.assertNotNull(savedBookResponseDto);
    }

    @Test
    public void BookService_UpdateBook_ReturnResponseDto() throws ConflictEntityException, ResourceNotFoundException {
        Book book = new Book();
        book.setTitle("Book 1");
        book.setIsbn("1-1");

        BookRequestDtoV1 bookRequestDto = new BookRequestDtoV1();
        bookRequestDto.setTitle("Book 2");
        bookRequestDto.setIsbn("1-1");

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.ofNullable(book));
        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book);

        Book savedBook = bookService.update(bookRequestDto, 1L);

        Assertions.assertNotNull(savedBook);
        Assertions.assertEquals(bookRequestDto.getTitle(), savedBook.getTitle());
    }

    @Test
    public void BookService_DeleteBookById_ReturnResponseDto() throws ConflictEntityException, ResourceNotFoundException {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book 1");
        book.setIsbn("1-1");

        Mockito.when(bookRepository.existsById(book.getId())).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> bookService.delete(1L));
    }
}
