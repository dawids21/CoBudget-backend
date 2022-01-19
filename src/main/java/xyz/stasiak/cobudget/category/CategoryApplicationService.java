package xyz.stasiak.cobudget.category;

import lombok.RequiredArgsConstructor;
import xyz.stasiak.cobudget.category.exception.CategoryIdNotFound;

@RequiredArgsConstructor
class CategoryApplicationService {

    private final CategoryRepository repository;

    Category add(Category category) {
        return repository.save(category);
    }

    void disable(long id) {
        var category = repository.findById(id).getOrElseThrow(() -> new CategoryIdNotFound(id));
        category.setDisabled(true);
        repository.save(category);
    }
}
