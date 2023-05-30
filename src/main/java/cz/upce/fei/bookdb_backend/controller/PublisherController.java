package cz.upce.fei.bookdb_backend.controller;

import cz.upce.fei.bookdb_backend.domain.Publisher;
import cz.upce.fei.bookdb_backend.dto.PublisherRequestDtoV1;
import cz.upce.fei.bookdb_backend.dto.PublisherResponseDtoV1;
import cz.upce.fei.bookdb_backend.exception.ConflictEntityException;
import cz.upce.fei.bookdb_backend.exception.ResourceNotFoundException;
import cz.upce.fei.bookdb_backend.service.PublisherService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/publishers")
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService publisherService;

    @GetMapping("")
    public ResponseEntity<List<PublisherResponseDtoV1>> findAll() {
        List<PublisherResponseDtoV1> result = publisherService.findAll().stream()
                .map(Publisher::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("{publisherId}")
    public ResponseEntity<PublisherResponseDtoV1> findById(@PathVariable Long publisherId) throws ResourceNotFoundException {
        Publisher publisher = publisherService.findById(publisherId);
        return ResponseEntity.ok(publisher.toDto());

    }

    @PostMapping("")
    public ResponseEntity<PublisherResponseDtoV1> createPublisher(@RequestBody @Validated PublisherRequestDtoV1 publisherDto) throws ConflictEntityException {
        Publisher publisher = publisherService.create(publisherDto);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("").toUriString());
        return ResponseEntity.created(uri).body(publisher.toDto());
    }

    @PutMapping("{publisherId}")
    public ResponseEntity<PublisherResponseDtoV1> updatePublisher(@PathVariable Long publisherId, @RequestBody @Validated PublisherRequestDtoV1 publisherDto)
            throws ConflictEntityException, ResourceNotFoundException {
        Publisher publisher = publisherService.update(publisherDto, publisherId);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(String.format("/{%d}", publisherId)).toUriString());
        return ResponseEntity.created(uri).body(publisher.toDto());
    }

    @DeleteMapping("{publisherId}")
    public ResponseEntity<?> deletePublisher(@PathVariable Long publisherId) throws ConflictEntityException, ResourceNotFoundException {
        publisherService.delete(publisherId);

        return ResponseEntity.noContent().build();
    }
}
