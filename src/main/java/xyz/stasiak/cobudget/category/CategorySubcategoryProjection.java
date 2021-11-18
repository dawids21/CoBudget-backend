package xyz.stasiak.cobudget.category;

import io.vavr.collection.List;
import xyz.stasiak.cobudget.category.dto.CategoryReadModel;
import xyz.stasiak.cobudget.category.dto.CategorySubcategoryReadModel;

record CategorySubcategoryProjection(long categoryId, String category, long subcategoryId, String subcategory) {

    static CategorySubcategoryReadModel toCategorySubcategoryReadModel(List<CategorySubcategoryProjection> projections) {
        return new CategorySubcategoryReadModel(projections.get().categoryId, projections.get().category, projections.map(CategorySubcategoryProjection::toCategoryReadModel));
    }

    private CategoryReadModel toCategoryReadModel() {
        return new CategoryReadModel(subcategoryId, categoryId, subcategory);
    }

}
