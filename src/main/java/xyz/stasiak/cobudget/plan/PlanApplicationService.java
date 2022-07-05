package xyz.stasiak.cobudget.plan;

import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import xyz.stasiak.cobudget.plan.exception.PlanNotFound;

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

    void planCategory(Long planId, Integer categoryId, int amount) {
        Plan plan = planRepository.findById(planId)
                .getOrElseThrow(() -> new PlanNotFound(planId));
        plan.planCategory(categoryId, amount);
        planRepository.save(plan);
    }

    int getAmountPlannedFor(Long planId, Integer categoryId) {
        Plan plan = planRepository.findById(planId)
                .getOrElseThrow(() -> new PlanNotFound(planId));
        return plan.getAmountPlannedForCategory(categoryId);
    }

    Plan changeCategoryPlan(Long planId, Integer categoryId, int newAmount) {
        Plan plan = planRepository.findById(planId)
                .getOrElseThrow(() -> new PlanNotFound(planId));
        plan.changePlanForCategory(categoryId, newAmount);
        return planRepository.save(plan);
    }
}
