package xyz.stasiak.cobudget.plan;

import io.vavr.collection.List;

import java.time.LocalDate;

record PlanReadModel(Long id, LocalDate date, List<PlannedCategoryReadModel> plannedCategories) {
}
