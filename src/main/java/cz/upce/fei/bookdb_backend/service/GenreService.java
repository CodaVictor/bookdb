package cz.upce.fei.bookdb_backend.service;

import cz.upce.fei.bookdb_backend.domain.Genre;
import cz.upce.fei.bookdb_backend.dto.GenreRequestDtoV1;
import cz.upce.fei.bookdb_backend.exception.ConflictEntityException;
import cz.upce.fei.bookdb_backend.repository.GengeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GenreService {

    private final GengeRepository gengeRepository;

    @Transactional(readOnly = true)
    public Optional<Genre> findById(final Long id) {
        return gengeRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsByGenreName(final String genre) {
        return gengeRepository.existsByGenreName(genre);
    }

    @Transactional(readOnly = true)
    public List<Genre> findAll() {
        return (List<Genre>) gengeRepository.findAll();
    }

    public Genre create(final GenreRequestDtoV1 genreDto) throws ConflictEntityException {
        boolean exists = gengeRepository.existsByGenreName(genreDto.getGenreName());

        if(exists) {
            throw new ConflictEntityException(
                    String.format("Genre %s already exists", genreDto.getGenreName()));
        }

        Genre genre = new Genre();
        genre.setGenreName(genreDto.getGenreName());
        genre.setDescription(genreDto.getDescription());

        log.info("Saving new genre '{}' to the database.", genre.getGenreName());
        return gengeRepository.save(genre);
    }

    public Genre update(final Genre genre) {
        log.info("Saving updated genre '{}' to the database.", genre.getGenreName());
        return gengeRepository.save(genre);
    }

    public void delete(final Long id) {
        log.info("Deleting genre with id {}.", id);
        gengeRepository.deleteById(id);
    }
}
