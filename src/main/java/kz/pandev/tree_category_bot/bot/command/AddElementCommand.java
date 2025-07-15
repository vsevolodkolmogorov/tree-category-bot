package kz.pandev.tree_category_bot.bot.command;

import kz.pandev.tree_category_bot.dto.CommandResult;
import kz.pandev.tree_category_bot.error.CategoryNotFoundException;
import kz.pandev.tree_category_bot.model.Category;
import kz.pandev.tree_category_bot.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * Command to add a new category or attach a child category to a parent.
 * Usage:
 * <pre>
 * /addElement <name> - creates a root category
 * /addElement <parent> <child> - attaches a child category to the parent
 * </pre>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AddElementCommand implements BotCommand {

    private final CategoryService categoryService;

    @Override
    public String getCommand() {
        return "/addElement";
    }

    @Override
    public String getDescription() {
        return "/addElement <name> - add root element \n/addElement <name parent> <name child> - add element to parent";
    }


    @Override
    public CommandResult execute(String[] args, Long chatId) {
        if (args.length == 0) {
            log.warn("/addElement command need <name> or <name parent> <name child> to execute the command!");
            return CommandResult.text("Need <name> or <name parent> <name child> to execute the command! \n" + getDescription());
        }

        try {
            Category category = args.length > 1 ? categoryService.addElement(args[0], args[1]) : categoryService.addElement(args[0]);
            return CommandResult.text("Element  \"" + category.getName() + "\" has been successfully created! \nUse /viewTree command to see the changes");
        } catch (CategoryNotFoundException e) {
            log.warn(e.getMessage());
            return CommandResult.text(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error in AddElementCommand", e);
            return CommandResult.text("There's been an unforeseen error: " + e.getMessage());
        }
    }
}
