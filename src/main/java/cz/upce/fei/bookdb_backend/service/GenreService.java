package cz.upce.fei.bookdb_backend.service;

import cz.upce.fei.bookdb_backend.domain.Genre;
import cz.upce.fei.bookdb_backend.dto.GenreRequestDtoV1;
import cz.upce.fei.bookdb_backend.exception.ConflictEntityException;
import cz.upce.fei.bookdb_backend.exception.ResourceNotFoundException;
import cz.upce.fei.bookdb_backend.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GenreService {

    private final GenreRepository genreRepository;

    @Transactional(readOnly = true)
    public Genre findById(final Long id) throws ResourceNotFoundException {
        return genreRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Genre not found."));
    }

    @Transactional(readOnly = true)
    public boolean existsByGenreName(final String genre) {
        return genreRepository.existsByGenreName(genre);
    }

    @Transactional(readOnly = true)
    public List<Genre> findAll() {
        return (List<Genre>) genreRepository.findAll();
    }

    public Genre create(final GenreRequestDtoV1 genreDto) throws ConflictEntityException {
        Genre genre = new Genre();
        dtoToGenre(genreDto, genre);

        log.info("Saving new genre '{}' to the database.", genre.getGenreName());
        return genreRepository.save(genre);
    }

    public Genre update(final GenreRequestDtoV1 genreDto, final Long genreId)
            throws ResourceNotFoundException, ConflictEntityException {
        Genre genre = genreRepository.findById(genreId).orElseThrow(() -> new ResourceNotFoundException("Genre not found."));
        dtoToGenre(genreDto, genre);

        log.info("Saving updated genre '{}' to the database.", genre.getGenreName());
        return genreRepository.save(genre);
    }

    public void delete(final Long genreId) throws ResourceNotFoundException {
        boolean exists = genreRepository.existsById(genreId);
        if(!exists) {
            throw new ResourceNotFoundException("Genre not found");
        }

        log.info("Deleting genre with id {}.", genreId);
        genreRepository.deleteById(genreId);
    }

    private void dtoToGenre(GenreRequestDtoV1 genreDto, Genre genre) throws ConflictEntityException {
        boolean exists = genreRepository.existsByGenreName(genreDto.getGenreName());

        if(exists) {
            throw new ConflictEntityException(
                    String.format("Genre %s already exists.", genreDto.getGenreName()));
        }

        genre.setGenreName(genreDto.getGenreName());
        genre.setDescription(genreDto.getDescription());
    }
}
