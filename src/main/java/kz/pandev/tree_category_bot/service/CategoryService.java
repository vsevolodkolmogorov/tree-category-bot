package kz.pandev.tree_category_bot.service;
import kz.pandev.tree_category_bot.error.CategoryNotFoundException;
import kz.pandev.tree_category_bot.model.Category;

import java.io.File;


/**
 * Service for managing categories in the tree.
 */
public interface CategoryService {

    /**
     * Adds a new root category.
     *
     * @param name the name of the category to create
     * @return the created category
     */
    Category addElement(String name);

    /**
     * Adds a child category to the parent category.
     *
     * @param parentName the name of the parent category
     * @param childrenName the name of the child category to create
     * @return the created child category
     * @throws CategoryNotFoundException if parent category is not found
     */
    Category addElement(String parentName, String childrenName);

    /**
     * Removes a category and its children from the tree.
     *
     * @param name the name of the category to remove
     * @throws CategoryNotFoundException if the category is not found
     */
    void removeElement(String name);

    /**
     * Returns the full category tree in a human-readable text format.
     *
     * @return the category tree as a string
     */
    String viewTree();

    File downloadTreeAsExcel();
}
