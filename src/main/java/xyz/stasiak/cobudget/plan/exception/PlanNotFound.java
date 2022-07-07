package xyz.stasiak.cobudget.plan.exception;

import java.time.LocalDate;

import static java.lang.String.format;

public class PlanNotFound extends RuntimeException {

    public PlanNotFound(Long planId) {
        super(format("Plan with id %d not found", planId));
    }

    public PlanNotFound(LocalDate date) {
        super(format("Plan for %s %d not found", date.getMonth(), date.getYear()));
    }
}
