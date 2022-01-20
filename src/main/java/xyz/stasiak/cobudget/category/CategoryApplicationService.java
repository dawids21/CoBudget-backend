package xyz.stasiak.cobudget.category;

import io.vavr.collection.Set;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.stasiak.cobudget.category.exception.CategoryIdNotFound;
import xyz.stasiak.cobudget.category.exception.MainCategoryNotFound;

@RequiredArgsConstructor
@Slf4j
class CategoryApplicationService {

    private final CategoryRepository repository;

    Category add(Category category) {
        Option<Category> possibleExistingCategory = repository.findByParentIdAndName(category.getParentId(), category.getName(), category.getUserId());
        if (possibleExistingCategory.isDefined()) {
            return repository.save(enable(possibleExistingCategory.get()));
        }
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

    private Category enable(Category category) {
        if (category.isSubCategory()) {
            Category mainCategory = repository.findById(category.getParentId()).getOrElseThrow(() -> new MainCategoryNotFound(category.getName()));
            mainCategory.setDisabled(false);
            log.debug("Enabled category with id {}", mainCategory.getId());
            repository.save(mainCategory);
        }
        category.setDisabled(false);
        log.debug("Enabled category with id {}", category.getId());
        return category;
    }
}
