package xyz.stasiak.cobudget.plan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.vavr.control.Option;
import lombok.Data;
import org.springframework.data.annotation.Id;

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

    private Map<Long, PlannedCategory> categories = new HashMap<>();

    Plan(String userId, LocalDate yearAndMonth) {
        this.userId = userId;
        this.yearAndMonth = yearAndMonth;
    }

    void planCategory(Long categoryId, int amount) {
        PlannedCategory plannedCategory = new PlannedCategory(id, categoryId, amount);
        categories.put(categoryId, plannedCategory);
    }

    int getAmountPlannedForCategory(Long categoryId) {
        return Option.of(categories.get(categoryId))
                .map(PlannedCategory::getAmount)
                .getOrElse(0);
    }
}
