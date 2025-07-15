package kz.pandev.tree_category_bot.error;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String name) {
        super("Category '" + name + "' was not found.");
    }
}
