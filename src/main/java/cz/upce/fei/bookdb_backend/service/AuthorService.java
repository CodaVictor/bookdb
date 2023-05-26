package cz.upce.fei.bookdb_backend.service;

import cz.upce.fei.bookdb_backend.domain.Author;
import cz.upce.fei.bookdb_backend.dto.AuthorRequestDtoV1;
import cz.upce.fei.bookdb_backend.exception.ConflictEntityException;
import cz.upce.fei.bookdb_backend.exception.ResourceNotFoundException;
import cz.upce.fei.bookdb_backend.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    public Author findById(Long id) throws ResourceNotFoundException {
        return authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author not found."));
    }

    @Transactional(readOnly = true)
    public boolean existsByFirstNameAndLastNameAndBirthdate(String firstName, String lastName, LocalDateTime birthdate) {
        return authorRepository.existsByFirstNameAndLastNameAndBirthdate(firstName, lastName, birthdate);
    }

    @Transactional(readOnly = true)
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    public Author create(final AuthorRequestDtoV1 authorDto) throws ConflictEntityException {
        Author author = new Author();
        dtoToAuthor(authorDto, author);

        log.info("Saving new author '{}' to the database.", authorDto.getFirstName() + " " + authorDto.getLastName());
        return authorRepository.save(author);
    }

    public Author update(final AuthorRequestDtoV1 authorDto, final Long authorId)
            throws ResourceNotFoundException, ConflictEntityException {
        Author author = authorRepository.findById(authorId).orElseThrow(() -> new ResourceNotFoundException("Author not found."));
        dtoToAuthor(authorDto, author);

        log.info("Saving updated author '{}' to the database.", author.getFirstName() + " " + author.getLastName());
        return authorRepository.save(author);
    }

    public void delete(final Long authorId) throws ResourceNotFoundException {
        boolean exists = authorRepository.existsById(authorId);
        if(!exists) {
            throw new ResourceNotFoundException("Author not found.");
        }

        log.info("Deleting author with id {}.", authorId);
        authorRepository.deleteById(authorId);
    }

    private void dtoToAuthor(AuthorRequestDtoV1 authorDto, Author author) throws ConflictEntityException {
        boolean exists = authorRepository.existsByFirstNameAndLastNameAndBirthdate(
                authorDto.getFirstName(), authorDto.getLastName(), authorDto.getBirthdate());

        if(exists) {
            throw new ConflictEntityException(
                String.format("Author %s %s, birthdate: %s already exists", authorDto.getFirstName(), authorDto.getLastName(), authorDto.getBirthdate()));
        }

        author.setFirstName(authorDto.getFirstName());
        author.setLastName(authorDto.getLastName());
        author.setBirthdate(authorDto.getBirthdate());
        author.setDescription(authorDto.getDescription());
    }
}
