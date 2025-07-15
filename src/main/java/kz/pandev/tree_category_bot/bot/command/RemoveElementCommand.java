package kz.pandev.tree_category_bot.bot.command;

import kz.pandev.tree_category_bot.dto.CommandResult;
import kz.pandev.tree_category_bot.error.CategoryNotFoundException;
import kz.pandev.tree_category_bot.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Command to remove element and all his children.
 * Usage:
 * <pre>
 * /removeElement <name> - remove element and all his children
 * </pre>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RemoveElementCommand implements BotCommand {

    private final CategoryService categoryService;

    @Override
    public String getCommand() {
        return "/removeElement";
    }

    @Override
    public String getDescription() {
        return "/removeElement <name> - remove element and all his children";
    }

    @Override
    public CommandResult execute(String[] args, Long chatId) {
        if (args.length == 0) return CommandResult.text("Need <name> to execute the command! \n" + getDescription());

        try {
            categoryService.removeElement(args[0]);
        } catch (CategoryNotFoundException e) {
            log.warn(e.getMessage());
            return CommandResult.text(e.getMessage());
        }

        return CommandResult.text("The element " + args[0] + " was successfully deleted! \nUse /viewTree command to see the changes");
    }
}
