package cz.upce.fei.bookdb_backend.controller;

import cz.upce.fei.bookdb_backend.domain.Category;
import cz.upce.fei.bookdb_backend.dto.CategoryRequestDtoV1;
import cz.upce.fei.bookdb_backend.dto.CategoryResponseDtoV1;
import cz.upce.fei.bookdb_backend.service.CategoryService;
import cz.upce.fei.bookdb_backend.service.exception.ConflictEntityException;
import cz.upce.fei.bookdb_backend.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<List<CategoryResponseDtoV1>> findAll() {
        List<CategoryResponseDtoV1> result = categoryService.findAll().stream()
                .map(Category::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("{categoryId}")
    public ResponseEntity<CategoryResponseDtoV1> findById(@PathVariable Long categoryId) throws ResourceNotFoundException {
        Category category = categoryService.findById(categoryId);
        return ResponseEntity.ok(category.toDto());

    }

    @PostMapping("")
    public ResponseEntity<CategoryResponseDtoV1> createCategory(@RequestBody @Validated CategoryRequestDtoV1 categoryDto) throws ConflictEntityException {
        Category category = categoryService.create(categoryDto);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("").toUriString());
        return ResponseEntity.created(uri).body(category.toDto());
    }

    @PutMapping("{categoryId}")
    public ResponseEntity<CategoryResponseDtoV1> updateCategory(@PathVariable Long categoryId, @RequestBody @Validated CategoryRequestDtoV1 categoryDto)
            throws ConflictEntityException, ResourceNotFoundException {
        Category category = categoryService.update(categoryDto, categoryId);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(String.format("/{%d}", categoryId)).toUriString());
        return ResponseEntity.created(uri).body(category.toDto());
    }

    @DeleteMapping("{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) throws ConflictEntityException, ResourceNotFoundException {
        categoryService.delete(categoryId);

        return ResponseEntity.noContent().build();
    }
}
