package kz.pandev.tree_category_bot.bot.command;

import kz.pandev.tree_category_bot.dto.CommandResult;
import kz.pandev.tree_category_bot.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;

@Component
@RequiredArgsConstructor
@Slf4j
public class DownloadElementCommand implements BotCommand{

    private final CategoryService categoryService;


    /**
     * Returns the command keyword (e.g., "/addElement").
     *
     * @return the command name used to trigger this command
     */
    @Override
    public String getCommand() {
        return "/download";
    }

    /**
     * Returns a short description of the command,
     * which will be shown in the /help message.
     *
     * @return a human-readable description of the command
     */
    @Override
    public String getDescription() {
        return "/download - downloads an Excel document with a category tree";
    }

    /**
     * Executes the command with the given arguments.
     *
     * @param args the arguments passed by the user (maybe empty)
     * @return the result message that will be sent back to the user
     */
    @Override
    public CommandResult execute(String[] args, Long chatId) {
        try {
            File excelFile = categoryService.downloadTreeAsExcel();
            return CommandResult.file(new InputFile(excelFile), "Category tree");
        } catch (Exception e) {
            return CommandResult.text("Error while generating or sending Excel file: " + e.getMessage());
        }
    }
}
