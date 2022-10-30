package xyz.stasiak.cobudget.plan;

import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import xyz.stasiak.cobudget.common.UserId;
import xyz.stasiak.cobudget.plan.exception.PlanNotFound;

import java.time.LocalDate;

@RequiredArgsConstructor
class PlanApplicationService {

    private final PlanRepository planRepository;

    Plan createPlan(UserId userId, LocalDate yearAndMonth) {
        Plan plan = new Plan(userId.id(), yearAndMonth);
        return planRepository.save(plan);
    }

    Option<Plan> getPlanFor(UserId userId, LocalDate yearAndMonth) {
        return planRepository.findByUserIdAndYearAndMonth(
                userId.id(), yearAndMonth.getYear(), yearAndMonth.getMonthValue()
        );
    }

    void planCategory(UserId userId, Long planId, Integer categoryId, int amount) {
        Plan plan = planRepository.findByIdAndUserId(planId, userId.id())
                .getOrElseThrow(() -> new PlanNotFound(planId));
        plan.planCategory(categoryId, amount);
        planRepository.save(plan);
    }

    int getAmountPlannedFor(UserId userId, Long planId, Integer categoryId) {
        Plan plan = planRepository.findByIdAndUserId(planId, userId.id())
                .getOrElseThrow(() -> new PlanNotFound(planId));
        return plan.getAmountPlannedForCategory(categoryId);
    }

    Plan changeCategoryPlan(UserId userId, Long planId, Integer categoryId, int newAmount) {
        Plan plan = planRepository.findByIdAndUserId(planId, userId.id())
                .getOrElseThrow(() -> new PlanNotFound(planId));
        plan.changePlanForCategory(categoryId, newAmount);
        return planRepository.save(plan);
    }

    void deletePlan(UserId userId, Long planId) {
        planRepository.deleteById(planId, userId.id());
    }

    PlanReadModel readPlan(UserId userId, LocalDate yearAndMonth) {
        return planRepository.readPlanByUserIdAndYearAndMonth(
                        userId.id(), yearAndMonth.getYear(), yearAndMonth.getMonthValue()
                )
                .getOrElse(() -> PlanReadModel.empty(yearAndMonth));
    }

    Plan deletePlannedCategory(UserId userId, Long planId, Integer categoryId) {
        Plan plan = planRepository.findByIdAndUserId(planId, userId.id())
                .getOrElseThrow(() -> new PlanNotFound(planId));
        plan.removePlanForCategory(categoryId);
        return planRepository.save(plan);
    }
}
