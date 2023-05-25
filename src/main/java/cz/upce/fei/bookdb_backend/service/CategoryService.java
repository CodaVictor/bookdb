package cz.upce.fei.bookdb_backend.service;

import cz.upce.fei.bookdb_backend.domain.AppUser;
import cz.upce.fei.bookdb_backend.domain.Book;
import cz.upce.fei.bookdb_backend.domain.Category;
import cz.upce.fei.bookdb_backend.exception.ResourceNotFoundException;
import cz.upce.fei.bookdb_backend.repository.CategoryRepository;
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
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Optional<Category> findById(final Long id) throws ResourceNotFoundException {
        return categoryRepository.findById(id);
    }

    public Category create(final Category category) {
        log.info("Saving new category '{}' to the database.", category.getName());
        return categoryRepository.save(category);
    }

    public Category update(final Category category) {
        log.info("Saving updated category '{}' to the database.", category.getName());
        return categoryRepository.save(category);
    }

    public void delete(final Long id) {
        log.info("Deleting category with id {}.", id);
        categoryRepository.deleteById(id);
    }
}
