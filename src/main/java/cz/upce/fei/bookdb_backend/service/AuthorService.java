package cz.upce.fei.bookdb_backend.service;

import cz.upce.fei.bookdb_backend.domain.Author;
import cz.upce.fei.bookdb_backend.domain.Book;
import cz.upce.fei.bookdb_backend.domain.Review;
import cz.upce.fei.bookdb_backend.exception.ResourceNotFoundException;
import cz.upce.fei.bookdb_backend.repository.AuthorRepository;
import cz.upce.fei.bookdb_backend.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    public List<Author> findAll() {
        return (List<Author>)authorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Author findById(Long id) throws ResourceNotFoundException {
        Optional<Author> result = authorRepository.findById(id);

        if (result.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        return result.get();
    }

    @Transactional
    public Author create(final Author author) {
        return authorRepository.save(author);
    }

    @Transactional
    public Author update(final Author toEntity) {
        return authorRepository.save(toEntity);
    }

    @Transactional
    public void delete(final Long id) {
        authorRepository.deleteById(id);
    }
}
