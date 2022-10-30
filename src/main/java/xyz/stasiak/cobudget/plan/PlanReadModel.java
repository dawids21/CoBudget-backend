package xyz.stasiak.cobudget.plan;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

import java.time.LocalDate;

record PlanReadModel(Long id, LocalDate date, Set<PlannedCategoryReadModel> plannedCategories) {
    static PlanReadModel empty(LocalDate date) {
        return new PlanReadModel(null, date, HashSet.empty());
    }
}
