package xyz.stasiak.cobudget.plan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.vavr.control.Option;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
class Plan {

    @Id
    private Long id;

    @JsonIgnore
    private String userId;
    private LocalDate yearAndMonth;

    @MappedCollection(idColumn = "plan", keyColumn = "category_key")
    private Map<Integer, PlannedCategory> categories = new HashMap<>();

    Plan(String userId, LocalDate yearAndMonth) {
        this.userId = userId;
        this.yearAndMonth = yearAndMonth;
    }

    void planCategory(Integer categoryId, int amount) {
        PlannedCategory plannedCategory = new PlannedCategory(amount);
        categories.put(categoryId, plannedCategory);
    }

    int getAmountPlannedForCategory(Integer categoryId) {
        return Option.of(categories.get(categoryId))
                .map(PlannedCategory::getAmount)
                .getOrElse(0);
    }

    void changePlanForCategory(Integer categoryId, int newAmount) {
        PlannedCategory plannedCategory = Option.of(categories.get(categoryId))
                .getOrElse(() -> new PlannedCategory(newAmount));
        plannedCategory.changePlan(newAmount);
        categories.put(categoryId, plannedCategory);
    }
}
