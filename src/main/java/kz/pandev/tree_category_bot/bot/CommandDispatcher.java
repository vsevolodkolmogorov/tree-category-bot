package kz.pandev.tree_category_bot.bot;

import kz.pandev.tree_category_bot.bot.command.BotCommand;
import kz.pandev.tree_category_bot.dto.CommandResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Central dispatcher that processes user input and executes corresponding {@link BotCommand}.
 * <p>
 * Finds a command by its name and delegates the execution with provided arguments.
 * Returns the result message or a default error if the command is unknown.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CommandDispatcher {

    private final List<BotCommand> commands;

    /**
     * Processes the user input, searches for a matching command, and executes it.
     *
     * @param input the raw user input (e.g., "/addElement parent child")
     * @return the command execution result or an error message if the command is not found
     */
    public CommandResult handleCommand(String input, Long chatId) {
        String[] parts = input.trim().split("\\s+");
        String commandName = parts[0];
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        log.info("Received command: '{}', arguments: {}", commandName, Arrays.toString(args));

        return commands.stream()
                .filter(cmd -> cmd.getCommand().equalsIgnoreCase(commandName))
                .findFirst()
                .map(cmd -> {
                    log.info("Executing command handler: {}", cmd.getClass().getSimpleName());
                    return cmd.execute(args, chatId);
                })
                .orElse(CommandResult.text("Unknown command. Write /help"));
    }
}
