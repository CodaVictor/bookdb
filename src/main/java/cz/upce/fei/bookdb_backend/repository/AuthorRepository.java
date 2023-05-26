package cz.upce.fei.bookdb_backend.repository;

import cz.upce.fei.bookdb_backend.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsByFirstNameAndLastNameAndBirthdate(String firstName, String lastName, LocalDateTime birthdate);
}
