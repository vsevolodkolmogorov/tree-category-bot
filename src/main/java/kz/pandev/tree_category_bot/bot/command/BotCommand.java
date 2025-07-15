package kz.pandev.tree_category_bot.bot.command;

import kz.pandev.tree_category_bot.dto.CommandResult;

/**
 * Represents a generic bot command that can be executed by the Telegram bot.
 *
 * <p>Each command must define:
 * <ul>
 *   <li>The command name (e.g., "/addElement").</li>
 *   <li>A short description for help messages.</li>
 *   <li>Execution logic based on provided arguments.</li>
 * </ul>
 */
public interface BotCommand {

    /**
     * Returns the command keyword (e.g., "/addElement").
     *
     * @return the command name used to trigger this command
     */
    String getCommand();

    /**
     * Returns a short description of the command,
     * which will be shown in the /help message.
     *
     * @return a human-readable description of the command
     */
    String getDescription();

    /**
     * Executes the command with the given arguments.
     *
     * @param args the arguments passed by the user (maybe empty)
     * @return the result message that will be sent back to the user
     */
    CommandResult execute(String[] args, Long chatId);
}
