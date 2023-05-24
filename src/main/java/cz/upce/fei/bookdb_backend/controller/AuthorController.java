package cz.upce.fei.bookdb_backend.controller;

import cz.upce.fei.bookdb_backend.domain.Author;
import cz.upce.fei.bookdb_backend.dto.AuthorResponseDtoV1;
import cz.upce.fei.bookdb_backend.exception.ResourceNotFoundException;
import cz.upce.fei.bookdb_backend.service.AuthorService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
    public ResponseEntity<AuthorResponseDtoV1> findById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(authorService.findById(id).toDto());
    }
}
