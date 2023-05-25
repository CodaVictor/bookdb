package cz.upce.fei.bookdb_backend.repository;

import cz.upce.fei.bookdb_backend.domain.Genre;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GengeRepository extends PagingAndSortingRepository<Genre, Long> {
    boolean existsByGenreName(String genreName);
}
