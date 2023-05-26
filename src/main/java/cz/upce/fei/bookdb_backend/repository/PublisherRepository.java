package cz.upce.fei.bookdb_backend.repository;

import cz.upce.fei.bookdb_backend.domain.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    boolean existsByName(final String name);

    boolean existsById(final Long id);
}
