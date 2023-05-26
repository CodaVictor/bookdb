package cz.upce.fei.bookdb_backend.repository;

import cz.upce.fei.bookdb_backend.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    boolean existsById(final Long id);
    boolean existsByIsbn(final String isbn);
}
