package xyz.stasiak.cobudget.category.dto;

import io.vavr.collection.List;

record CategorySubcategoryReadModel(long id, String name, List<CategoryReadModel> subcategories) {
}
