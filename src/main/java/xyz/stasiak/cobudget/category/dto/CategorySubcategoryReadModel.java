package xyz.stasiak.cobudget.category.dto;

import io.vavr.collection.List;

public record CategorySubcategoryReadModel(long id, List<CategoryReadModel> subcategories) {
}
