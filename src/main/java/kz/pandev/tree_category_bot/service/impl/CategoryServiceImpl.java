package kz.pandev.tree_category_bot.service.impl;

import kz.pandev.tree_category_bot.error.CategoryNotFoundException;
import kz.pandev.tree_category_bot.error.EmptyElementTreeException;
import kz.pandev.tree_category_bot.model.Category;
import kz.pandev.tree_category_bot.repository.CategoryRepository;
import kz.pandev.tree_category_bot.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Override
    @Transactional(readOnly = true)
    public String viewTree() {
        List<Category> allCategory = repository.findRootsWithChildren();
        if (allCategory.isEmpty()) throw new EmptyElementTreeException();

        StringBuilder builder = new StringBuilder();

        for (Category root : allCategory) {
            buildTreeRecursive(root, builder, 0);
        }

        log.info("The tree has been successfully built");
        return builder.toString();
    }


    @Override
    public Category addElement(String name) {
        Category category = Category.builder()
                .name(name)
                .children(null)
                .parent(null)
                .build();

        log.info("Element '{}' has been successfully created!", name);
        return repository.save(category);
    }

    @Override
    public Category addElement(String parentName, String childrenName) {
        Category parent = repository.findByNameWithChildren(parentName).orElseThrow(() -> new CategoryNotFoundException(parentName));
        Category children = repository.findByName(childrenName).orElseGet(() -> addElement(childrenName));

        children.setParent(parent);
        parent.getChildren().add(children);

        repository.save(parent);
        repository.save(children);

        log.info("Element '{}' has been successfully add to parent '{}'!", children.getName(), parent.getName());
        return children;
    }

    @Override
    public void removeElement(String name) {
        Category category = repository.findByNameWithChildren(name)
                .orElseThrow(() -> new CategoryNotFoundException(name));

        repository.delete(category);
        log.info("Element '{}' has been successfully remove!", name);
    }

    @Override
    @Transactional(readOnly = true)
    public File downloadTreeAsExcel() {
        List<Category> allCategory = repository.findRootsWithChildren();
        if (allCategory.isEmpty()) throw new EmptyElementTreeException();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Category Tree");

            int rowIndex = 0;
            for (Category root : allCategory) {
                rowIndex = writeCategoryRecursive(root, sheet, rowIndex, 0);
            }

            File file = new File("category_tree.xlsx");
            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }

            return file;
        } catch (IOException e) {
            throw new RuntimeException("Error while generating Excel file", e);
        }
    }

    /**
     * Recursively builds a visual representation of the category tree.
     * <p>
     * Each level of depth is indented with two spaces, and every category is displayed as:
     * <pre>
     * - Parent
     *   - Child1
     *     - SubChild
     *   - Child2
     * </pre>
     *
     * @param category the current category to process
     * @param builder  the {@link StringBuilder} that accumulates the tree structure
     * @param depth    the current nesting level (used for indentation)
     */
    private void buildTreeRecursive(Category category, StringBuilder builder, int depth) {
        builder.append("  ".repeat(depth))
                .append("- ")
                .append(category.getName())
                .append("\n");

        for (Category child : category.getChildren()) {
            buildTreeRecursive(child, builder, depth + 1);
        }
    }

    /**
     * Recursively writes a category and its hierarchical children into an Excel sheet.
     *
     * <p>This method places the current category name in the specified row and column
     * (defined by {@code rowIndex} and {@code depth}), applies a background color
     * depending on the category depth, and then processes all children recursively.
     *
     * <p>If the category has children, they are written on subsequent rows with increasing depth.
     * After writing all children, the method groups their rows in the sheet for collapsible view.
     *
     * @param category  the current category to write
     * @param sheet     the Excel sheet where categories are written
     * @param rowIndex  the row index where the current category will be placed
     * @param depth     the depth level of the current category in the hierarchy
     *                  (used as the column index and for coloring)
     * @return the next available row index after writing the current category and all its children
     */
    private int writeCategoryRecursive(Category category, Sheet sheet, int rowIndex, int depth) {
        Row row = sheet.createRow(rowIndex);

        Cell cell = row.createCell(depth);
        cell.setCellValue(category.getName());

        CellStyle style = sheet.getWorkbook().createCellStyle();
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(getColorForDepth(sheet.getWorkbook(), depth));
        cell.setCellStyle(style);

        int startRow = rowIndex + 1;
        int currentRow = startRow;

        for (Category child : category.getChildren()) {
            currentRow = writeCategoryRecursive(child, sheet, currentRow, depth + 1);
        }

        if (!category.getChildren().isEmpty()) {
            sheet.groupRow(startRow, currentRow - 1);
            sheet.setRowGroupCollapsed(startRow, true);
        }

        return currentRow;
    }

    private short getColorForDepth(Workbook workbook, int depth) {
        return switch (depth % 5) {
            case 0 -> IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex();
            case 1 -> IndexedColors.LIGHT_GREEN.getIndex();
            case 2 -> IndexedColors.LIGHT_YELLOW.getIndex();
            case 3 -> IndexedColors.LEMON_CHIFFON.getIndex();
            default -> IndexedColors.LIGHT_ORANGE.getIndex();
        };
    }
}
