package xyz.stasiak.cobudget.plan;

import io.vavr.collection.Set;

record PlannedCategoryReadModel(long id, String name, Set<PlannedSubcategoryReadModel> plannedSubcategories) {
}
