package xyz.stasiak.cobudget.plan;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import xyz.stasiak.cobudget.category.exception.CategoryIdNotFound;
import xyz.stasiak.cobudget.common.ErrorMessage;
import xyz.stasiak.cobudget.common.UserId;
import xyz.stasiak.cobudget.common.UserIdNotFound;
import xyz.stasiak.cobudget.plan.exception.PlanNotFound;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/plan")
@RequiredArgsConstructor
@CrossOrigin(origins = "${security.cors.origins}")
@Slf4j
class PlanController {

    private final PlanApplicationService planApplicationService;

    @PostMapping
    Plan createPlan(@RequestBody PlanWriteModel planWriteModel, @AuthenticationPrincipal Jwt jwt) {
        UserId userId = UserId.get(jwt).getOrElseThrow(() -> new UserIdNotFound(jwt.getSubject()));

        return planApplicationService.createPlan(userId, planWriteModel.date());
    }

    @GetMapping
    PlanReadModel readPlan(@RequestParam String date, @AuthenticationPrincipal Jwt jwt) {
        UserId userId = UserId.get(jwt).getOrElseThrow(() -> new UserIdNotFound(jwt.getSubject()));

        return planApplicationService.readPlan(userId, LocalDate.parse(date));
    }

    @DeleteMapping("/{planId}")
    void deletePlan(@PathVariable Long planId) {
        planApplicationService.deletePlan(planId);
    }

    @PostMapping("/{planId}/category/{categoryId}")
    void planCategory(@PathVariable Long planId, @PathVariable Integer categoryId, @RequestParam int amount) {
        planApplicationService.planCategory(planId, categoryId, amount);
    }

    @GetMapping("/{planId}/category/{categoryId}")
    int getAmountPlannedFor(@PathVariable Long planId, @PathVariable Integer categoryId) {
        return planApplicationService.getAmountPlannedFor(planId, categoryId);
    }

    @PatchMapping("/{planId}/category/{categoryId}")
    void changePlannedAmountForCategory(@PathVariable Long planId, @PathVariable Integer categoryId, @RequestParam int newAmount) {
        planApplicationService.changeCategoryPlan(planId, categoryId, newAmount);
    }

    @DeleteMapping("/{planId}/category/{categoryId}")
    void deletePlannedAmountForCategory(@PathVariable Long planId, @PathVariable Integer categoryId) {
        planApplicationService.deletePlannedCategory(planId, categoryId);
    }

    @ExceptionHandler(PlanNotFound.class)
    ResponseEntity<ErrorMessage> handleCategoryNotFound(CategoryIdNotFound exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.badRequest().body(ErrorMessage.of(exception));
    }
}
