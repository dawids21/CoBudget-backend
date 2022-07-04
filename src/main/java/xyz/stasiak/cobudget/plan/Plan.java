package xyz.stasiak.cobudget.plan;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public void planCategory(Long categoryId, int amount) {
        PlannedCategory plannedCategory = new PlannedCategory(id, categoryId, amount);
        categories.put(categoryId, plannedCategory);
    }

    public PlannedCategory getPlanForCategory(Long categoryId) {
        return categories.get(categoryId);
    }
}
