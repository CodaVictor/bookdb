package cz.upce.fei.bookdb_backend.service;

import cz.upce.fei.bookdb_backend.domain.Publisher;
import cz.upce.fei.bookdb_backend.dto.PublisherRequestDtoV1;
import cz.upce.fei.bookdb_backend.repository.PublisherRepository;
import cz.upce.fei.bookdb_backend.service.exception.ConflictEntityException;
import cz.upce.fei.bookdb_backend.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PublisherService {

    private final PublisherRepository publisherRepository;

    @Transactional(readOnly = true)
    public Publisher findById(final Long id) throws ResourceNotFoundException {
        return publisherRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Publisher not found."));
    }

    @Transactional(readOnly = true)
    public List<Publisher> findAll() {
        return publisherRepository.findAll();
    }

    public Publisher create(final PublisherRequestDtoV1 publisherDto) throws ConflictEntityException {
        Publisher publisher = new Publisher();
        dtoToPublisher(publisherDto, publisher);

        log.info("Saving new publisher '{}' to the database.", publisher.getName());
        return publisherRepository.save(publisher);
    }

    public Publisher update(final PublisherRequestDtoV1 publisherDto, final Long publisherId) throws ResourceNotFoundException, ConflictEntityException {
        Publisher publisher = publisherRepository.findById(publisherId).orElseThrow(() -> new ResourceNotFoundException("Publisher not found."));
        dtoToPublisher(publisherDto, publisher);

        log.info("Saving updated publisher '{}' to the database.", publisher.getName());
        return publisherRepository.save(publisher);
    }

    public void delete(final Long publisherId) throws ResourceNotFoundException {
        boolean exists = publisherRepository.existsById(publisherId);

        if(!exists) {
            throw new ResourceNotFoundException("Publisher not found.");
        }

        log.info("Deleting publisher with id {}.", publisherId);
        publisherRepository.deleteById(publisherId);
    }

    private void dtoToPublisher(PublisherRequestDtoV1 publisherDto, Publisher publisher) throws ConflictEntityException {
        boolean exists = publisherRepository.existsByName(publisherDto.getName());

        if(exists) {
            throw new ConflictEntityException(String.format("Publisher %s already exists.", publisherDto.getName()));
        }

        publisher.setName(publisherDto.getName());
    }
}
