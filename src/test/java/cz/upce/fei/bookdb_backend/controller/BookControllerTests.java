package cz.upce.fei.bookdb_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.upce.fei.bookdb_backend.domain.AppUser;
import cz.upce.fei.bookdb_backend.domain.Book;
import cz.upce.fei.bookdb_backend.domain.Review;
import cz.upce.fei.bookdb_backend.dto.BookRequestDtoV1;
import cz.upce.fei.bookdb_backend.dto.ReviewRequestDtoV1;
import cz.upce.fei.bookdb_backend.service.*;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class BookControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookService bookService;
	@MockBean
	private UserDetailsService userDetailsService;
	@MockBean
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@MockBean
	private ReviewService reviewService;
	@MockBean
	private CategoryService categoryService;
	@MockBean
	private GenreService genreService;
	@MockBean
	private PublisherService publisherService;
	@MockBean
	private AuthorService authorService;

	@Autowired
	private ObjectMapper objectMapper;

	private Book book;
	private Review review;
	private AppUser appUser;
	private BookRequestDtoV1 bookRequestDto;
	private ReviewRequestDtoV1 reviewRequestDto;

	@BeforeEach
	public void init() {
		book = new Book();
		book.setId(1L);
		book.setTitle("Book 1");
		book.setIsbn("1-1");

		bookRequestDto = new BookRequestDtoV1();
		bookRequestDto.setTitle("Book res 1");
		bookRequestDto.setIsbn("1-1");

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
	public void BookController_CreateBook_ReturnCreatedBook() throws Exception {
		BDDMockito.given(bookService.create(ArgumentMatchers.any()))
				.willAnswer(invocation -> book);

		String bookJson = objectMapper.writeValueAsString(bookRequestDto);

		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/books")
						.contentType(MediaType.APPLICATION_JSON)
						.content(bookJson))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(book.getId().intValue())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(book.getTitle())));
	}

	@Test
	void BookController_GetAllBook_ReturnPageBook() throws Exception {
		Page<Book> bookPage = new PageImpl<>(Arrays.asList(book), Pageable.ofSize(10), 10);
		Mockito.when(bookService.findAllBy(Specification.where(null), PageRequest.of(0,10))).thenReturn(bookPage);
		ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/books")
				.contentType(MediaType.APPLICATION_JSON)
				.param("page", "1")
				.param("pageSize", "10"));

		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(bookPage.getContent().size())));
	}

	@Test
	public void BookController_BookById_ReturnBookDto() throws Exception {
		long bookId = 1;
		Mockito.when(bookService.findById(bookId)).thenReturn(book);

		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/books/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(bookRequestDto)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(book.getId().intValue())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(book.getTitle())));
	}

	@Test
	public void BookController_UpdateBook_ReturnBookDto() throws Exception {
		long bookId = 1;
		Mockito.when(bookService.update(bookRequestDto, bookId)).thenReturn(book);

		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/books/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(bookRequestDto)));

		resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(book.getId().intValue())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(book.getTitle())));
	}

	@Test
	public void BookController_DeleteBook_ReturnBookDto() throws Exception {
		long bookId = 1;
		Mockito.doNothing().when(bookService).delete(bookId);

		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/books/1")
				.contentType(MediaType.APPLICATION_JSON));

		resultActions.andExpect(MockMvcResultMatchers.status().isNoContent());
	}
}