package xyz.stasiak.cobudget.category;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CategoryApplicationService {

    private final CategoryRepository repository;

    Category add(Category category) {
        return repository.save(category);
    }

}
