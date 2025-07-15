package kz.pandev.tree_category_bot.bot.command;

import kz.pandev.tree_category_bot.dto.CommandResult;
import kz.pandev.tree_category_bot.error.EmptyElementTreeException;
import kz.pandev.tree_category_bot.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Command to view the entire category tree.
 * Usage:
 * <pre>
 * /viewTree - view the entire category tree
 * </pre>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ViewTreeCommand implements BotCommand {

    private final CategoryService categoryService;

    @Override
    public String getCommand() {
        return "/viewTree";
    }

    @Override
    public String getDescription() {
        return "/viewTree - view the entire category tree";
    }

    @Override
    public CommandResult execute(String[] args, Long chatId) {
        try {
            return CommandResult.text(categoryService.viewTree());
        } catch (EmptyElementTreeException e) {
            log.warn(e.getMessage());
            return CommandResult.text(e.getMessage());
        }
    }
}
