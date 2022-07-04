package xyz.stasiak.cobudget.plan;

import lombok.Data;

@Data
class PlannedCategory {

    private Long plan;
    private Long categoryKey;
    private int amount;

    PlannedCategory(Long plan, Long categoryKey) {
        this.plan = plan;
        this.categoryKey = categoryKey;
        this.amount = 0;
    }
}
