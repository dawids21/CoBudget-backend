package xyz.stasiak.cobudget.entry;

import io.vavr.collection.List;

record CategoryWithSubcategoriesReadModel(long id, List<CategoryReadModel> subcategories) {
}
