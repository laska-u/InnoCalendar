import model.Course;
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
import service.CourseService;
import service.UserService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    public static final String SPREAD_SHEET = "https://docs.google.com/spreadsheets/d/15xpB2ckMNQLmgwTmJv0DsRjrvsjAYPAC0Im3QFWdqZM/edit#gid=516269660";
    private Parser parser = new Parser();
    private static UserService userService = new UserService();
    private CourseService courseService = new CourseService();
    private static final long ADMIN_ID = 273255483;
    private static final int REMINDER_INTERVAL = 60000;

    //https://api.telegram.org/bot930074549:AAEzjxP7aFUkpBs4dgn8QbUgpb5JeDRd3vo/getUpdates
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            Bot myBot = new Bot();
            telegramBotsApi.registerBot(new Bot());

            while (true) {
                try {
                    Thread.sleep(REMINDER_INTERVAL);
                    HashMap<Integer, String> currentCourses = Parser.getCoursesByDatetime();
                    for (User user : userService.findAllUsers()) {
                        for (int courseId : currentCourses.keySet()) {
                            if ((user.getCourse() != null) && (user.getCourse().getId() == courseId)) {
                                myBot.sendMsg(user.getChat_id(), "Reminder: course " + currentCourses.get(courseId) + " in the next hour!");
                            }
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

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
            System.out.println("Chat_id:" + update.getMessage().getChatId());
            System.out.println();

            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            long user_id = update.getMessage().getFrom().getId();

            switch (message_text) {
                case "/start":
                    sendMsg(chat_id);
                    User user = userService.findUser(user_id);
                    if (user == null) {
                        userService.saveUser(new User(user_id, chat_id));
                        System.out.println("new user registered");
                    } else {
                        System.out.println("user registered already");
                    }
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
                case "Upload Courses":
                    if (user_id == ADMIN_ID) {
                        saveCoursesInDataBase();
                    }
                    break;
                default:
                    sendMsg(chat_id, "Sorry, I don't understand you :(");
            }


        } else if (update.hasCallbackQuery()) {
            boolean found = false;
            long course_id = Long.parseLong(update.getCallbackQuery().getData());
            User user = userService.findUser(update.getCallbackQuery().getFrom().getId());
            if ((user.getCourse() != null) && (user.getCourse().getId() == course_id)) {
                user.setCourse(null);
                userService.updateUser(user);
                answerCallbackQuery(update.getCallbackQuery().getId(), "unsubscribed!");
                sendCourses(user.getChat_id());
            } else {
                Course course = courseService.findCourse(course_id);
                user.setCourse(course);
                userService.updateUser(user);
                answerCallbackQuery(update.getCallbackQuery().getId(), "subscribed!");
                sendCourses(user.getChat_id());
            }
        }

    }

    private void saveCoursesInDataBase() {
        if (courseService.findAllCourses().size() == 0) {
            List<String> courses = parser.getCourses();
            for (int i = 0; i < courses.size(); i++) {
                String cours = courses.get(i);
                courseService.saveCourse(new Course(cours, i));
            }
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
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
        }
    }

    public synchronized void sendMsg(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText("Hello! You can view schedule or choose elective course");
        setButtons(sendMessage);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
        }
    }

    public synchronized void sendCourses(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText("Click to any course below to subscribe for it");
        setInline(sendMessage);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
        }
    }

    public synchronized void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Choose course"));
        keyboardFirstRow.add(new KeyboardButton("All courses"));
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public synchronized void setFinishButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Finish choosing process"));
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    private void setInline(SendMessage sendMessage) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<String> courses = parser.getCourses();

        User user = userService.findUser(Long.parseLong(sendMessage.getChatId()));
        Course user_course = user.getCourse();

        for (int i = 0; i < courses.size(); i++) {
            String course = courses.get(i);
            List<InlineKeyboardButton> button = new ArrayList<>();
            if ((user_course != null) && (user_course.getId() == i)) {
                button.add(new InlineKeyboardButton().setText("\u2705 " + course).setCallbackData(String.valueOf(i)));
            } else {
                button.add(new InlineKeyboardButton().setText(course).setCallbackData(String.valueOf(i)));
            }
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
