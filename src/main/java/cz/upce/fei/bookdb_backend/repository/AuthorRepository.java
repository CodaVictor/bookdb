package cz.upce.fei.bookdb_backend.repository;

import cz.upce.fei.bookdb_backend.domain.Author;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends PagingAndSortingRepository<Author, Long> {
    boolean existsByFirstNameAndLastNameAndBirthdate(String firstName, String lastName, LocalDateTime birthdate);
}
