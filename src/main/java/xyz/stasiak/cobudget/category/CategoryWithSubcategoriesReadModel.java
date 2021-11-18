package xyz.stasiak.cobudget.category;

import io.vavr.collection.List;

record CategoryWithSubcategoriesReadModel(long id, List<CategoryReadModel> subcategories) {
}
