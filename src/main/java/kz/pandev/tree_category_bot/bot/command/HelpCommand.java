package kz.pandev.tree_category_bot.bot.command;

import kz.pandev.tree_category_bot.dto.CommandResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Command to show list of all available commands with descriptions.
 * Usage:
 * <pre>
 * /help  - show list of all available commands
 * </pre>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class HelpCommand implements BotCommand{

    private final List<BotCommand> commands;

    @Override
    public String getCommand() {
        return "/help";
    }

    @Override
    public String getDescription() {
        return "/help - show list of all available commands";
    }

    @Override
    public CommandResult execute(String[] args, Long chatId) {
        log.info("The \"/help\" command has been executed");
        return CommandResult.text("Main commands: \n" + commands.stream()
                .map(BotCommand::getDescription)
                .collect(Collectors.joining("\n")));
    }
}
