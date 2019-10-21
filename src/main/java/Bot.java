import model.User;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import service.UserService;

import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    public static final String SPREAD_SHEET = "https://docs.google.com/spreadsheets/d/15xpB2ckMNQLmgwTmJv0DsRjrvsjAYPAC0Im3QFWdqZM/edit#gid=516269660";
    private Parser parser = new Parser();
    private UserService userService = new UserService();

    //https://api.telegram.org/bot930074549:AAEzjxP7aFUkpBs4dgn8QbUgpb5JeDRd3vo/getUpdates
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {

            System.out.println("First name:" + update.getMessage().getFrom().getFirstName());
            System.out.println("Last name:" + update.getMessage().getFrom().getLastName());
            System.out.println("User id:" + update.getMessage().getFrom().getId());
            System.out.println();

            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            long user_id = update.getMessage().getFrom().getId();

            switch (message_text) {
                case "/start":
                    sendMsg(chat_id);
//                    User user = userService.findUser(user_id);
//                    if (user == null) {
//                        userService.saveUser(new User(user_id, chat_id));
//                        System.out.println("new user registered");
//                    } else {
//                        System.out.println("user registered already");
//                    }
                    break;
                case "All courses":
                    sendMsg(chat_id, SPREAD_SHEET);
                    break;
                case "Choose course":
                    sendCourses(chat_id);
                    break;
                case "Finish choosing process":
                    sendMsg(chat_id);
                    break;
                default:
                    sendMsg(chat_id, "Sorry, I don't understand you :(");

            }
        } else  if(update.hasCallbackQuery()) {
            answerCallbackQuery(update.getCallbackQuery().getId(), "subscribed!");
        }

    }

    public String getBotUsername() {
        return "InnoElectiveBot";
    }

    public String getBotToken() {
        return "930074549:AAEzjxP7aFUkpBs4dgn8QbUgpb5JeDRd3vo";
    }

    public synchronized void sendMsg(long chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
//        setInline(sendMessage);
//        setButtons(sendMessage);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
//            log.log(Level.SEVERE, "Exception: ", e.toString());
        }
    }

    public synchronized void sendMsg(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText("Hello! You can view schedule or choose elective course");
//        setInline(sendMessage);
        setButtons(sendMessage);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
//            log.log(Level.SEVERE, "Exception: ", e.toString());
        }
    }

    public synchronized void sendCourses(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText("Click to any course below to subscribe for it");
        setInline(sendMessage);
//        setFinishButtons(sendMessage);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
//            log.log(Level.SEVERE, "Exception: ", e.toString());
        }
    }

    public synchronized void setButtons(SendMessage sendMessage) {
        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add(new KeyboardButton("Choose course"));
        keyboardFirstRow.add(new KeyboardButton("All courses"));

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public synchronized void setFinishButtons(SendMessage sendMessage) {
        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add(new KeyboardButton("Finish choosing process"));

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    private void setInline(SendMessage sendMessage) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<String> courses = parser.getCourses();

        for (int i = 0; i < courses.size(); i++) {
            String course = courses.get(i);
            List<InlineKeyboardButton> button = new ArrayList<>();
            button.add(new InlineKeyboardButton().setText(course).setCallbackData(String.valueOf(i)));
            buttons.add(button);
        }

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        sendMessage.setReplyMarkup(markupKeyboard);
    }

    public synchronized void answerCallbackQuery(String callbackId, String message) {
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(callbackId);
        answer.setText(message);
        answer.setShowAlert(true);
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
