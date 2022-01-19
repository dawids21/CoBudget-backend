package xyz.stasiak.cobudget.category.exception;

public class MainCategoryNotFound extends RuntimeException {

    public MainCategoryNotFound(String categoryName) {
        super(String.format("Main category for subcategory %s not found", categoryName));
    }

}
