package cz.upce.fei.bookdb_backend.service;

import cz.upce.fei.bookdb_backend.domain.Publisher;
import cz.upce.fei.bookdb_backend.domain.Review;
import cz.upce.fei.bookdb_backend.repository.PublisherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PublisherService {

    private PublisherRepository publisherRepository;

    @Transactional
    public Publisher create(final Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    @Transactional
    public Publisher update(final Publisher toEntity) {
        return publisherRepository.save(toEntity);
    }

    @Transactional
    public void delete(final Long id) {
        publisherRepository.deleteById(id);
    }
}
