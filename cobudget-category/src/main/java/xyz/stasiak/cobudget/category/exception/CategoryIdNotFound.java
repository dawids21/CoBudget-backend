package xyz.stasiak.cobudget.category.exception;

public class CategoryIdNotFound extends RuntimeException {

    public CategoryIdNotFound(long id) {
        super(String.format("Category with id %d not found", id));
    }

}
