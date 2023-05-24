package cz.upce.fei.bookdb_backend.service;

import cz.upce.fei.bookdb_backend.domain.AppUser;
import cz.upce.fei.bookdb_backend.domain.Category;
import cz.upce.fei.bookdb_backend.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CategoryService {

    private CategoryRepository categoryRepository;

    @Transactional
    public Category create(final Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public Category update(final Category toEntity) {
        return categoryRepository.save(toEntity);
    }

    @Transactional
    public void delete(final Long id) {
        categoryRepository.deleteById(id);
    }
}
