package cz.upce.fei.bookdb_backend.controller;

import cz.upce.fei.bookdb_backend.domain.Author;
import cz.upce.fei.bookdb_backend.dto.AuthorRequestDtoV1;
import cz.upce.fei.bookdb_backend.dto.AuthorResponseDtoV1;
import cz.upce.fei.bookdb_backend.service.AuthorService;
import cz.upce.fei.bookdb_backend.service.exception.ConflictEntityException;
import cz.upce.fei.bookdb_backend.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("")
    public ResponseEntity<List<AuthorResponseDtoV1>> findAll() {
        List<AuthorResponseDtoV1> result = authorService.findAll().stream()
                .map(Author::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("{authorId}")
    public ResponseEntity<AuthorResponseDtoV1> findById(@PathVariable Long authorId) throws ResourceNotFoundException {
        Author author = authorService.findById(authorId);
        return ResponseEntity.ok(author.toDto());
    }

    @PostMapping("")
    public ResponseEntity<AuthorResponseDtoV1> createAuthor(@RequestBody @Validated AuthorRequestDtoV1 authorDto) throws ConflictEntityException {
        Author author = authorService.create(authorDto);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("").toUriString());
        return ResponseEntity.created(uri).body(author.toDto());
    }

    @PutMapping("{authorId}")
    public ResponseEntity<AuthorResponseDtoV1> updateAuthor(@PathVariable Long authorId, @RequestBody @Validated AuthorRequestDtoV1 authorDto)
            throws ConflictEntityException, ResourceNotFoundException {
        Author author = authorService.update(authorDto, authorId);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(String.format("/{%s}", author.getId())).toUriString());
        return ResponseEntity.created(uri).body(author.toDto());
    }

    @DeleteMapping("{authorId}")
    public ResponseEntity<?> deleteAuthor(@PathVariable Long authorId) throws ResourceNotFoundException {
        authorService.delete(authorId);

        return ResponseEntity.noContent().build();
    }
}
