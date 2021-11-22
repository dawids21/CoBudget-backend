package xyz.stasiak.cobudget.category.query;

import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import xyz.stasiak.cobudget.common.UserId;

@RequiredArgsConstructor
class CategoryQueryService {

    private final CategoryQueryRepository queryRepository;

    List<CategoryReadModel> getCategories(UserId userId) {
        return queryRepository.findCategories(userId.id());
    }

    List<CategoryReadModel> getSubcategories(long categoryId, UserId userId) {
        return queryRepository.findSubcategories(categoryId, userId.id());
    }

    List<CategorySubcategoryReadModel> getAllCategories(UserId userId) {

        var subcategories = queryRepository.findSubcategories(userId.id()).groupBy(CategoryReadModel::parentId);

        return queryRepository.findCategories(userId.id())
                .map(category -> new CategorySubcategoryReadModel(category.id(), category.name(), subcategories.getOrElse(category.id(), List.empty())));
    }

}
