package xyz.stasiak.cobudget.category;

import io.vavr.collection.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.stasiak.cobudget.category.exception.CategoryIdNotFound;

@RequiredArgsConstructor
@Slf4j
class CategoryApplicationService {

    private final CategoryRepository repository;

    Category add(Category category) {
        return repository.save(category);
    }

    void disable(long id) {
        Category category = repository.findById(id).getOrElseThrow(() -> new CategoryIdNotFound(id));
        category.setDisabled(true);
        log.debug("Disabled category with id {}", id);
        if (category.isMainCategory()) {
            Set<Category> subcategories = repository.findAllByParentId(category.getId());
            for (Category subcategory : subcategories) {
                subcategory.setDisabled(true);
                log.debug("Disabled category with id {}", subcategory.getId());
            }
        }
        repository.save(category);
    }
}
