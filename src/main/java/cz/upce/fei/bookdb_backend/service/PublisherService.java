package cz.upce.fei.bookdb_backend.service;

import cz.upce.fei.bookdb_backend.domain.Publisher;
import cz.upce.fei.bookdb_backend.exception.ConflictEntityException;
import cz.upce.fei.bookdb_backend.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PublisherService {

    private final PublisherRepository publisherRepository;

    @Transactional(readOnly = true)
    public Optional<Publisher> findById(final Long id) {
        return publisherRepository.findById(id);
    }

    public Publisher create(final String publisher) throws ConflictEntityException {
        boolean exists = publisherRepository.existsByName(publisher);

        if(exists) {
            throw new ConflictEntityException(
                    String.format("Publisher %s already exists", publisher));
        }

        Publisher newPublisher = new Publisher();
        newPublisher.setName(publisher);

        log.info("Saving new publisher '{}' to the database.", newPublisher.getName());
        return publisherRepository.save(newPublisher);
    }

    public Publisher update(final Publisher publisher) {
        log.info("Saving updated publisher '{}' to the database.", publisher.getName());
        return publisherRepository.save(publisher);
    }

    public void delete(final Long id) {
        log.info("Deleting publisher with id {}.", id);
        publisherRepository.deleteById(id);
    }
}
