package cz.upce.fei.bookdb_backend.service;

import cz.upce.fei.bookdb_backend.domain.Author;
import cz.upce.fei.bookdb_backend.domain.Book;
import cz.upce.fei.bookdb_backend.domain.Review;
import cz.upce.fei.bookdb_backend.exception.ResourceNotFoundException;
import cz.upce.fei.bookdb_backend.repository.AuthorRepository;
import cz.upce.fei.bookdb_backend.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    public Author findById(Long id) throws ResourceNotFoundException {
        Optional<Author> result = authorRepository.findById(id);

        if (result.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        return result.get();
    }

    @Transactional(readOnly = true)
    public List<Author> findAll() {
        return (List<Author>)authorRepository.findAll();
    }

    public Author create(final Author author) {
        log.info("Saving new author '{}' to the database.", author.getFirstName() + " " + author.getLastName());
        return authorRepository.save(author);
    }

    public Author update(final Author author) {
        log.info("Saving updated author '{}' to the database.", author.getFirstName() + " " + author.getLastName());
        return authorRepository.save(author);
    }

    public void delete(final Long id) {
        log.info("Deleting author with id {}.", id);
        authorRepository.deleteById(id);
    }
}
