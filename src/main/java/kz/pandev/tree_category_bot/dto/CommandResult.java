package kz.pandev.tree_category_bot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.InputFile;

@Data
@AllArgsConstructor
public class CommandResult {

    private String text;
    private InputFile file;
    private String fileCaption;

    public static CommandResult text(String text) {
        return new CommandResult(text, null, null);
    }

    public static CommandResult file(InputFile file, String caption) {
        return new CommandResult(null, file, caption);
    }
}
