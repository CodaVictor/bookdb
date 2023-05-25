package cz.upce.fei.bookdb_backend.controller;

import cz.upce.fei.bookdb_backend.domain.Author;
import cz.upce.fei.bookdb_backend.dto.AuthorRequestDtoV1;
import cz.upce.fei.bookdb_backend.dto.AuthorResponseDtoV1;
import cz.upce.fei.bookdb_backend.exception.ConflictEntityException;
import cz.upce.fei.bookdb_backend.exception.ResourceNotFoundException;
import cz.upce.fei.bookdb_backend.service.AuthorService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDtoV1> findById(@PathVariable Long id) {
        Optional<Author> author = authorService.findById(id);
        if(author.isPresent()) {
            return ResponseEntity.ok(author.get().toDto());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createAuthor(@RequestBody @Validated AuthorRequestDtoV1 authorDto) throws ConflictEntityException {
        Author author = authorService.create(authorDto);
        return ResponseEntity.ok(author.toDto());
    }
}
