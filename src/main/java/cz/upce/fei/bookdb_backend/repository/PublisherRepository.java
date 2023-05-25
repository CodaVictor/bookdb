package cz.upce.fei.bookdb_backend.repository;

import cz.upce.fei.bookdb_backend.domain.Publisher;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends PagingAndSortingRepository<Publisher, Long> {
    boolean existsByName(String name);
}
