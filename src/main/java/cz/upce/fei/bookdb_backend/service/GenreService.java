package cz.upce.fei.bookdb_backend.service;

import cz.upce.fei.bookdb_backend.domain.AppUser;
import cz.upce.fei.bookdb_backend.domain.Genre;
import cz.upce.fei.bookdb_backend.repository.GengeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class GenreService {

    private GengeRepository gengeRepository;

    @Transactional
    public Genre create(final Genre genre) {
        return gengeRepository.save(genre);
    }

    @Transactional
    public Genre update(final Genre toEntity) {
        return gengeRepository.save(toEntity);
    }

    @Transactional
    public void delete(final Long id) {
        gengeRepository.deleteById(id);
    }
}
