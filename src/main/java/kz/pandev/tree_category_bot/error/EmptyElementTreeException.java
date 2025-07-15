package kz.pandev.tree_category_bot.error;

public class EmptyElementTreeException extends RuntimeException{
    public EmptyElementTreeException() {
        super("The tree does not contain any elements");
    }
}
