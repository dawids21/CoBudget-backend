package xyz.stasiak.cobudget.entry;

import io.vavr.collection.List;

record CategoryWithSubcategoriesReadModel(long id, Long parentId, List<CategoryReadModel> subcategories) {
}
