package cz.upce.fei.bookdb_backend.controller;

import cz.upce.fei.bookdb_backend.domain.Author;
import cz.upce.fei.bookdb_backend.dto.AuthorRequestDtoV1;
import cz.upce.fei.bookdb_backend.dto.AuthorResponseDtoV1;
import cz.upce.fei.bookdb_backend.exception.ConflictEntityException;
import cz.upce.fei.bookdb_backend.exception.ResourceNotFoundException;
import cz.upce.fei.bookdb_backend.service.AuthorService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("")
    public ResponseEntity<List<AuthorResponseDtoV1>> findAll() {
        List<Author> result = authorService.findAll();

        return ResponseEntity.ok(result
                .stream()
                .map(Author::toDto)
                .collect(Collectors.toList())
        );
    }

    @GetMapping("{authorId}")
    public ResponseEntity<AuthorResponseDtoV1> findById(@PathVariable Long authorId) {
        Optional<Author> author = authorService.findById(authorId);
        if(author.isPresent()) {
            return ResponseEntity.ok(author.get().toDto());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<AuthorResponseDtoV1> createAuthor(@RequestBody @Validated AuthorRequestDtoV1 authorDto) throws ConflictEntityException {
        Author author = authorService.create(authorDto);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("").toUriString());
        return ResponseEntity.created(uri).body(author.toDto());
    }

    @PutMapping("{authorId}")
    public ResponseEntity<AuthorResponseDtoV1> updateAuthor(@PathVariable Long authorId, @RequestBody @Validated AuthorRequestDtoV1 authorDto) throws ConflictEntityException, ResourceNotFoundException {
        Author author = authorService.findById(authorId).orElseThrow(() -> new ResourceNotFoundException("Author not found."));

        // Check new author's attributes
        boolean exists = authorService.existsByFirstNameAndLastNameAndBirthdate(authorDto.getFirstName(), authorDto.getLastName(), authorDto.getBirthdate());
        if(exists) {
            throw new ConflictEntityException("Author with the current first name, last name and birth date already exists.");
        }

        author.setFirstName(authorDto.getFirstName());
        author.setLastName(authorDto.getLastName());
        author.setBirthdate(authorDto.getBirthdate());
        author.setDescription(author.getDescription());
        authorService.update(author);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/{id}").toUriString());
        return ResponseEntity.created(uri).body(author.toDto());
    }

    @DeleteMapping("{authorId}")
    public ResponseEntity<?> deleteAuthor(@PathVariable Long authorId) throws ConflictEntityException, ResourceNotFoundException {
        Author author = authorService.findById(authorId).orElseThrow(() -> new ResourceNotFoundException("Author not found."));
        authorService.delete(author.getId());

        return ResponseEntity.noContent().build();
    }
}
