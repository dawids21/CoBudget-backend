package xyz.stasiak.cobudget.category.query;

import io.vavr.collection.List;

record CategorySubcategoryReadModel(long id, String name, List<CategoryReadModel> subcategories) {
}
