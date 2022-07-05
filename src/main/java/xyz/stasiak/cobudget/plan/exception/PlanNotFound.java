package xyz.stasiak.cobudget.plan.exception;

public class PlanNotFound extends RuntimeException {

    public PlanNotFound(Long planId) {
        super(String.format("Plan with id %d not found", planId));
    }
}
