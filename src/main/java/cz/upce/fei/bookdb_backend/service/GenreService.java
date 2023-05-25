package cz.upce.fei.bookdb_backend.service;

import cz.upce.fei.bookdb_backend.domain.AppUser;
import cz.upce.fei.bookdb_backend.domain.Genre;
import cz.upce.fei.bookdb_backend.domain.Publisher;
import cz.upce.fei.bookdb_backend.exception.ResourceNotFoundException;
import cz.upce.fei.bookdb_backend.repository.GengeRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GenreService {

    private final GengeRepository gengeRepository;

    @Transactional(readOnly = true)
    public Optional<Genre> findById(final Long id) throws ResourceNotFoundException {
        return gengeRepository.findById(id);
    }

    public Genre create(final Genre genre) {
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
