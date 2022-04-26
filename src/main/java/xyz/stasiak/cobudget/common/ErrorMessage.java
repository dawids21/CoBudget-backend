package xyz.stasiak.cobudget.common;

public record ErrorMessage(String value) {
    public static ErrorMessage of(Exception exception) {
        return new ErrorMessage(exception.getMessage());
    }
}
