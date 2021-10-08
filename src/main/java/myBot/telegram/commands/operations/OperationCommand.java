package myBot.telegram.commands.operations;

import myBot.calculation.ArithmeticService;
import myBot.calculation.Calculator;
import myBot.enums.OperationEnum;
import myBot.fileProcessor.WordFileProcessorImpl;
import myBot.telegram.Bot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class OperationCommand extends BotCommand {
    private Logger logger = LoggerFactory.getLogger(OperationCommand.class);
    private ArithmeticService service;

    public OperationCommand(String identifier, String description) {
        super(identifier, description);
        this.service = new ArithmeticService(new WordFileProcessorImpl(), new Calculator());
    }

    void sendAnswer(AbsSender absSender, Long chatId, List<OperationEnum> operations, String description,
                    String commandName, String userName) {
        try {
            absSender.execute(createDocument(chatId, operations, description));
        } catch (IOException | RuntimeException e) {
            logger.error(String.format("Ошибка %s. Команда %s. Пользователь: %s.", e.getMessage(), commandName, userName));
            sendError(absSender, chatId, commandName, userName);
            e.printStackTrace();
        } catch (TelegramApiException e) {
            logger.error(String.format("Ошибка %s. Команда %s. Пользователь: %s.", e.getMessage(), commandName, userName));
            e.printStackTrace();
        }
    }

    private SendDocument createDocument(Long chatId, List<OperationEnum> operations, String fileName) throws IOException {
        FileInputStream stream = service.getFile(operations, Bot.getUserSettings(chatId));
        SendDocument document = new SendDocument();
        document.setChatId(chatId.toString());
        document.setDocument(new InputFile(stream, String.format("%s.docx", fileName)));
        return document;
    }

    private void sendError(AbsSender absSender, Long chatId, String commandName, String userName) {
        try {
            absSender.execute(new SendMessage(chatId.toString(), "Похоже, я сломался. Попробуйте позже"));
        } catch (TelegramApiException e) {
            logger.error(String.format("Ошибка %s. Команда %s. Пользователь: %s.", e.getMessage(), commandName, userName));
            e.printStackTrace();
        }
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

    }
}
