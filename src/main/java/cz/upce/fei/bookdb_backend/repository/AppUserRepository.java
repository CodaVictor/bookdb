package cz.upce.fei.bookdb_backend.repository;

import cz.upce.fei.bookdb_backend.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);

    void deleteAppUserByEmail(String email);
}
