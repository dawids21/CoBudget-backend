package xyz.stasiak.cobudget.plan;

import lombok.Data;

@Data
class PlannedCategory {

    private int amount;

    PlannedCategory(int amount) {
        this.amount = amount;
    }

    void changePlan(int amount) {
        this.amount = amount;
    }
}
