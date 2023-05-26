package cz.upce.fei.bookdb_backend.controller;

import cz.upce.fei.bookdb_backend.domain.Genre;
import cz.upce.fei.bookdb_backend.dto.GenreRequestDtoV1;
import cz.upce.fei.bookdb_backend.dto.GenreResponseDtoV1;
import cz.upce.fei.bookdb_backend.exception.ConflictEntityException;
import cz.upce.fei.bookdb_backend.exception.ResourceNotFoundException;
import cz.upce.fei.bookdb_backend.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("")
    public ResponseEntity<List<GenreResponseDtoV1>> findAll() {
        List<GenreResponseDtoV1> result = genreService.findAll().stream()
                .map(Genre::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("{genreId}")
    public ResponseEntity<GenreResponseDtoV1> findById(@PathVariable Long genreId) throws ResourceNotFoundException {
        Genre genre = genreService.findById(genreId);
        return ResponseEntity.ok(genre.toDto());

    }

    @PostMapping("")
    public ResponseEntity<GenreResponseDtoV1> createGenre(@RequestBody @Validated GenreRequestDtoV1 genreDto) throws ConflictEntityException {
        Genre genre = genreService.create(genreDto);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("").toUriString());
        return ResponseEntity.created(uri).body(genre.toDto());
    }

    @PutMapping("{genreId}")
    public ResponseEntity<GenreResponseDtoV1> updateGenre(@PathVariable Long genreId, @RequestBody @Validated GenreRequestDtoV1 genreDto)
            throws ConflictEntityException, ResourceNotFoundException {
        Genre genre = genreService.update(genreDto, genreId);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/genres/{id}").toUriString());
        return ResponseEntity.created(uri).body(genre.toDto());
    }

    @DeleteMapping("{genreId}")
    public ResponseEntity<?> deleteGenre(@PathVariable Long genreId) throws ResourceNotFoundException {
        genreService.delete(genreId);
        return ResponseEntity.noContent().build();
    }
}
