package xyz.stasiak.cobudget.plan;

import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
class PlanApplicationService {

    private final PlanRepository planRepository;

    Plan createPlan(String userId, LocalDate yearAndMonth) {
        Plan plan = new Plan(userId, yearAndMonth);
        return planRepository.save(plan);
    }

    Option<Plan> getPlanFor(String userId, LocalDate yearAndMonth) {
        return planRepository.findByUserIdAndYearAndMonth(
                userId, yearAndMonth.getYear(), yearAndMonth.getMonthValue()
        );
    }

    void planCategory(Long planId, Long categoryId, int amount) {
        Plan plan = planRepository.findById(planId)
                .getOrElseThrow(() -> new IllegalArgumentException(String.format("Plan with id %d not found", planId)));
        plan.planCategory(categoryId, amount);
    }

    int getAmountPlannedFor(Long planId, Long categoryId) {
        Plan plan = planRepository.findById(planId)
                .getOrElseThrow(() -> new IllegalArgumentException(String.format("Plan with id %d not found", planId)));
        return plan.getAmountPlannedForCategory(categoryId);
    }
}
