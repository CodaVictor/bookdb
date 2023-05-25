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
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("")
    public ResponseEntity<List<GenreResponseDtoV1>> findAll() {
        List<Genre> result = genreService.findAll();

        return ResponseEntity.ok(result
                .stream()
                .map(Genre::toDto)
                .collect(Collectors.toList())
        );
    }

    @GetMapping("{genreId}")
    public ResponseEntity<GenreResponseDtoV1> findById(@PathVariable Long genreId) {
        Optional<Genre> genre = genreService.findById(genreId);
        if(genre.isPresent()) {
            return ResponseEntity.ok(genre.get().toDto());
        } else {
            return ResponseEntity.notFound().build();
        }
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
        Genre genre = genreService.findById(genreId).orElseThrow(() -> new ResourceNotFoundException("Genre not found."));
        boolean exists = genreService.existsByGenreName(genreDto.getGenreName());

        if(exists) {
            throw new ConflictEntityException(String.format("Genre with name %s already exists.", genreDto.getGenreName()));
        }

        genre.setGenreName(genreDto.getGenreName());
        genre.setDescription(genreDto.getDescription());
        genreService.update(genre);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("{id}").toUriString());
        return ResponseEntity.created(uri).body(genre.toDto());
    }

    @DeleteMapping("{genreId}")
    public ResponseEntity<?> deleteGenre(@PathVariable Long genreId) throws ResourceNotFoundException {
        Genre genre = genreService.findById(genreId).orElseThrow(() -> new ResourceNotFoundException("Genre not found."));
        genreService.delete(genre.getId());

        return ResponseEntity.noContent().build();
    }
}
