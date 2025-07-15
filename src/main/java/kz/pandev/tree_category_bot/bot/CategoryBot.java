package kz.pandev.tree_category_bot.bot;

import kz.pandev.tree_category_bot.dto.CommandResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Telegram bot that handles category tree management commands.
 * <p>
 * Receives user messages, delegates command execution to {@link CommandDispatcher},
 * and returns the results back to the user.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CategoryBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;

    private final CommandDispatcher commandDispatcher;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            log.info("The message \"{}\" has been get from tg bot!", messageText);

            CommandResult result = commandDispatcher.handleCommand(messageText, chatId);

            if (result.getFile() != null) {
                sendDocument(chatId, result.getFile(), result.getFileCaption());
            } else {
                sendMessage(chatId, result.getText());
            }
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage(String.valueOf(chatId), text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendDocument(Long chatId, InputFile file, String caption) {
        SendDocument document = new SendDocument();
        document.setChatId(String.valueOf(chatId));
        document.setDocument(file);
        if (caption != null) document.setCaption(caption);

        try {
            execute(document);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

