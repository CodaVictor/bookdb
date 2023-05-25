package cz.upce.fei.bookdb_backend.service;

import cz.upce.fei.bookdb_backend.domain.Category;
import cz.upce.fei.bookdb_backend.domain.Publisher;
import cz.upce.fei.bookdb_backend.domain.Review;
import cz.upce.fei.bookdb_backend.exception.ResourceNotFoundException;
import cz.upce.fei.bookdb_backend.repository.PublisherRepository;
import lombok.AllArgsConstructor;
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
    public Optional<Publisher> findById(final Long id) throws ResourceNotFoundException {
        return publisherRepository.findById(id);
    }

    public Publisher savePublisher(final Publisher publisher) {
        log.info("Saving new publisher '{}' to the database.", publisher.getName());
        return publisherRepository.save(publisher);
    }

    public Publisher updatePublisher(final Publisher publisher) {
        log.info("Saving updated publisher '{}' to the database.", publisher.getName());
        return publisherRepository.save(publisher);
    }

    public void deletePublisher(final Long id) {
        log.info("Deleting publisher with id {}.", id);
        publisherRepository.deleteById(id);
    }
}
