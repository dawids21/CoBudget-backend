package xyz.stasiak.cobudget.plan.exception;

import xyz.stasiak.cobudget.common.UserId;

import java.time.LocalDate;

import static java.lang.String.format;

public class PlanNotFound extends RuntimeException {

    public PlanNotFound(Long planId) {
        super(format("Plan with id %d not found", planId));
    }

    public PlanNotFound(UserId userId, LocalDate date) {
        super(format("Plan for user %s for %s %d", userId.id(), date.getMonth(), date.getYear()));
    }
}
