package xyz.stasiak.cobudget.plan;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.stasiak.cobudget.common.ErrorMessage;
import xyz.stasiak.cobudget.common.UserId;
import xyz.stasiak.cobudget.plan.exception.PlanNotFound;

import java.security.Principal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/plan")
@RequiredArgsConstructor
@CrossOrigin(origins = "${security.cors.origins}")
@Slf4j
class PlanController {

    private final PlanApplicationService planApplicationService;

    @PostMapping
    Plan createPlan(@RequestBody PlanWriteModel planWriteModel, Principal principal) {
        var userId = new UserId(principal.getName());

        return planApplicationService.createPlan(userId, planWriteModel.date());
    }

    @GetMapping
    PlanReadModel readPlan(@RequestParam String date, Principal principal) {
        var userId = new UserId(principal.getName());

        return planApplicationService.readPlan(userId, LocalDate.parse(date));
    }

    @DeleteMapping("/{planId}")
    void deletePlan(@PathVariable Long planId, Principal principal) {
        var userId = new UserId(principal.getName());
        planApplicationService.deletePlan(userId, planId);
    }

    @PostMapping("/{planId}/category/{categoryId}")
    void planCategory(@PathVariable Long planId, @PathVariable Integer categoryId, @RequestParam int amount, Principal principal) {
        var userId = new UserId(principal.getName());
        planApplicationService.planCategory(userId, planId, categoryId, amount);
    }

    @GetMapping("/{planId}/category/{categoryId}")
    int getAmountPlannedFor(@PathVariable Long planId, @PathVariable Integer categoryId, Principal principal) {
        var userId = new UserId(principal.getName());
        return planApplicationService.getAmountPlannedFor(userId, planId, categoryId);
    }

    @PatchMapping("/{planId}/category/{categoryId}")
    void changePlannedAmountForCategory(@PathVariable Long planId, @PathVariable Integer categoryId, @RequestParam int newAmount, Principal principal) {
        var userId = new UserId(principal.getName());
        planApplicationService.changeCategoryPlan(userId, planId, categoryId, newAmount);
    }

    @DeleteMapping("/{planId}/category/{categoryId}")
    void deletePlannedAmountForCategory(@PathVariable Long planId, @PathVariable Integer categoryId, Principal principal) {
        var userId = new UserId(principal.getName());
        planApplicationService.deletePlannedCategory(userId, planId, categoryId);
    }

    @ExceptionHandler(PlanNotFound.class)
    ResponseEntity<ErrorMessage> handleCategoryNotFound(PlanNotFound exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.badRequest().body(ErrorMessage.of(exception));
    }
}
