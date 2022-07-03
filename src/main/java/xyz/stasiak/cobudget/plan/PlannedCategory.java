package xyz.stasiak.cobudget.plan;

import lombok.Data;

@Data
class PlannedCategory {

    private Long plan;
    private Long categoryKey;
    private int amount;
}
