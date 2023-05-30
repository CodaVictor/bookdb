package cz.upce.fei.bookdb_backend.service;

import cz.upce.fei.bookdb_backend.domain.Category;
import cz.upce.fei.bookdb_backend.dto.CategoryRequestDtoV1;
import cz.upce.fei.bookdb_backend.exception.ConflictEntityException;
import cz.upce.fei.bookdb_backend.exception.ResourceNotFoundException;
import cz.upce.fei.bookdb_backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Category findById(final Long id) throws ResourceNotFoundException {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found."));
    }

    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category create(final CategoryRequestDtoV1 categoryDto) throws ConflictEntityException {
        Category category = new Category();
        dtoToCategory(categoryDto, category);

        log.info("Saving new category '{}' to the database.", category.getName());
        return categoryRepository.save(category);
    }

    public Category update(final CategoryRequestDtoV1 categoryDto, final Long categoryId) throws ResourceNotFoundException, ConflictEntityException {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found."));
        dtoToCategory(categoryDto, category);

        log.info("Saving updated category '{}' to the database.", category.getName());
        return categoryRepository.save(category);
    }

    public void delete(final Long categoryId) throws ResourceNotFoundException {
        boolean exists = categoryRepository.existsById(categoryId);

        if(!exists) {
            throw new ResourceNotFoundException("Category not found.");
        }

        log.info("Deleting category with id {}.", categoryId);
        categoryRepository.deleteById(categoryId);
    }

    private void dtoToCategory(CategoryRequestDtoV1 categoryDto, Category category) throws ConflictEntityException {
        boolean exists = categoryRepository.existsByName(categoryDto.getName());

        if(exists) {
            throw new ConflictEntityException(String.format("Category %s already exists.", categoryDto.getName()));
        }

        category.setName(categoryDto.getName());
        category.setDescription(category.getDescription());
    }
}
