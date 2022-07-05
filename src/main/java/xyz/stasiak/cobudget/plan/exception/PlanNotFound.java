package xyz.stasiak.cobudget.plan.exception;

import java.time.LocalDate;

import static java.lang.String.format;

public class PlanNotFound extends RuntimeException {

    public PlanNotFound(Long planId) {
        super(format("Plan with id %d not found", planId));
    }

    public PlanNotFound(String userId, LocalDate date) {
        super(format("Plan for user %s for %s %d", userId, date.getMonth(), date.getYear()));
    }
}
