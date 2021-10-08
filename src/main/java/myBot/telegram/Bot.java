package myBot.telegram;

import lombok.Getter;
import myBot.UtilsBot;
import myBot.telegram.commands.operations.*;
import myBot.telegram.commands.service.HelpCommand;
import myBot.telegram.commands.service.SettingsCommand;
import myBot.telegram.commands.service.StartCommand;
import myBot.telegram.nonCommand.NonCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import myBot.telegram.nonCommand.Settings;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;


public class Bot extends TelegramLongPollingCommandBot {
    private Logger logger = LoggerFactory.getLogger(Bot.class);

    private final String BOT_NAME;
    private final String BOT_TOKEN;

    @Getter
    private static final Settings defaultSettings = new Settings(1, 15,1);
    private final NonCommand nonCommand;

    @Getter
    private static Map<Long, Settings> userSettings;

    public Bot(String botName, String botToken) {
        super();
        logger.debug("Конструктор суперкласса отработал");
        this.BOT_NAME = botName;
        this.BOT_TOKEN = botToken;
        logger.debug("Имя и токен присвоены");

        this.nonCommand = new NonCommand();
        logger.debug("Класс обработки сообщения, не являющегося командой, создан");

        register(new StartCommand("start", "Старт"));
        logger.debug("Команда start создана");

        register(new PlusCommand("plus", "Сложение"));
        logger.debug("Команда plus создана");

        register(new MinusCommand("minus", "Вычитание"));
        logger.debug("Команда minus создана");

        register(new PlusMinusCommand("plusminus", "Сложение и вычитание"));
        logger.debug("Команда plusminus создана");

        register(new MultiplicationCommand("multiply", "Умножение"));
        logger.debug("Команда multiply создана");

        register(new DivisionCommand("divide", "Деление"));
        logger.debug("Команда divide создана");

        register(new MultiplicationDivisionCommand("multdivide", "Умножение и деление"));
        logger.debug("Команда multdivide создана");

        register(new AllCommand("all", "4 действия"));
        logger.debug("Команда all создана");

        register(new HelpCommand("help","Помощь"));
        logger.debug("Команда help создана");

        register(new SettingsCommand("settings", "Мои настройки"));
        logger.debug("Команда settings создана");

        userSettings = new HashMap<>();
        logger.info("Бот создан!");
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        Message msg = update.getMessage();
        Long chatId = msg.getChatId();
        String userName = UtilsBot.getUserName(msg);

        String answer = nonCommand.nonCommandExecute(chatId, userName, msg.getText());
        setAnswer(chatId, userName, answer);
    }

    public static Settings getUserSettings(Long chatId) {
        Map<Long, Settings> userSettings = Bot.getUserSettings();
        Settings settings = userSettings.get(chatId);
        if (settings == null) {
            return defaultSettings;
        }
        return settings;
    }

    private void setAnswer(Long chatId, String userName, String text) {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            logger.error(String.format("Ошибка %s. Сообщение, не являющееся командой. Пользователь: %s", e.getMessage(),
                    userName));
            e.printStackTrace();
        }
    }
}
