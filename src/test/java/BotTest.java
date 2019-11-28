import model.Course;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import service.UserService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class BotTest {

    final long CHAT_ID = 1123;
    final long USER_ID = 123123123;

    static Stream<Arguments> createSubscriptions() {
        return Stream.of(
                Arguments.of(
                    new HashMap<Course, Boolean>()
                ),
                Arguments.of(
                        new HashMap<Course, Boolean>(){{
                            put(new Course("A", 1), true);
                            put(new Course("B", 2), false);
                        }}
                )
        );
    }
/*
    static Stream<Arguments> createDataForSubscription() {
        return Stream.of(
                Arguments.of(
                        true,false
                ),
                Arguments.of(
                        true, true
                )
        );
    }*/

    /**
     * Test that courses are shown when selecting courses for
     * use case "User (student) wants to register for an elective course in order to get notifications",
     * event 2
     * @param subscribedTo
     */
    @ParameterizedTest
    @MethodSource("createSubscriptions")
    public void testGetCourses(Map<Course, Boolean> subscribedTo) {
        Bot bot = new Bot();
        Parser parserMock = mock(Parser.class);
        when(parserMock.getCourses()).thenReturn(subscribedTo.keySet().stream().sorted((x, y) -> (int) (y.getId() - x.getId())).map(Course::getName).collect(Collectors.toList()));

        User chooseCoursesUser = mock(User.class);
        when(chooseCoursesUser.getId()).thenReturn((int) USER_ID);

        model.User userModel = mock(model.User.class);
        when(userModel.getId()).thenReturn(USER_ID);
        when(userModel.getChat_id()).thenReturn(CHAT_ID);

        UserService userServiceMock = mock(UserService.class);
        when(userServiceMock.findUser(USER_ID)).thenReturn(userModel);
        when(userServiceMock.findUser(CHAT_ID)).thenReturn(userModel);
        for (Map.Entry<Course, Boolean> kv : subscribedTo.entrySet()) {
            Course c = kv.getKey();
            Boolean subscribed = kv.getValue();
            if (subscribed) {
                when(userServiceMock.getCourseById(userModel, c.getId())).thenReturn(c);
            } else {
                when(userServiceMock.getCourseById(userModel, c.getId())).thenReturn(null);
            }
        }

        Bot.setUserService(userServiceMock);

        bot.setParser(parserMock);

        Message chooseCoursesMessage = mock(Message.class);
        Update chooseCoursesUpdate = mock(Update.class);
        when(chooseCoursesUpdate.hasMessage()).thenReturn(true);
        when(chooseCoursesUpdate.getMessage()).thenReturn(chooseCoursesMessage);
        when(chooseCoursesMessage.hasText()).thenReturn(true);
        when(chooseCoursesMessage.getText()).thenReturn("Choose course");
        when(chooseCoursesMessage.getChatId()).thenReturn(CHAT_ID);
        when(chooseCoursesMessage.getFrom()).thenReturn(chooseCoursesUser);

        SendMessage x = (SendMessage) bot.getMethod(chooseCoursesUpdate);
        InlineKeyboardMarkup mu = (InlineKeyboardMarkup) x.getReplyMarkup();
        List<String> buttonTexts = new ArrayList<>();
        for (List<InlineKeyboardButton> row : mu.getKeyboard()) {
            for (InlineKeyboardButton b : row) {
                buttonTexts.add(b.getText());
            }
        }

        for (Map.Entry<Course, Boolean> kv : subscribedTo.entrySet()) {
            String c = kv.getKey().getName();
            Boolean subscribed = kv.getValue();
            if (subscribed) {
                c = "\u2705 " + c;
            }

            assertThat(buttonTexts, hasItems(c));
        }
    }

    /**
     * Test for [Use case #1]
     * Tests menu which user would see when he enters app
     * @param userRegistered
     */
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void testStart(boolean userRegistered) {
        Bot bot = new Bot();


        User user = mock(User.class);
        when(user.getId()).thenReturn((int) USER_ID);

        model.User userModel = mock(model.User.class);
        when(userModel.getId()).thenReturn(USER_ID);
        when(userModel.getChat_id()).thenReturn(CHAT_ID);

        Message message = mock(Message.class);
        Update update = mock(Update.class);
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("/start");
        when(message.getChatId()).thenReturn(CHAT_ID);
        when(message.getFrom()).thenReturn(user);

        UserService userServiceMock = mock(UserService.class);
        if (userRegistered) {
            when(userServiceMock.findUser(USER_ID)).thenReturn(userModel);
        } else {
            when(userServiceMock.findUser(USER_ID)).thenReturn(null);
        }

        Bot.setUserService(userServiceMock);

        SendMessage x = (SendMessage) bot.getMethod(update);
        ReplyKeyboardMarkup mu = (ReplyKeyboardMarkup) x.getReplyMarkup();
        List<String> buttonTexts = new ArrayList<>();
        for (KeyboardRow row : mu.getKeyboard()) {
            for (KeyboardButton b : row) {
                buttonTexts.add(b.getText());
            }
        }

        assertThat(buttonTexts, containsInAnyOrder("Choose course", "All courses", "Unsubscribe"));
    }

    /**
     * Test for [Use case #3]
     * Tests subscription and unsubscribtion from course
     * @param isSubscribedForCourse
     */
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void testCourseSelection(boolean isSubscribedForCourse) {
        Set<Course> courses = new HashSet<Course>();
        final boolean subscribtionPerformed = false;
        boolean unsubscriptionPerformed = false;
        Bot bot = new Bot();


        User user = mock(User.class);
        when(user.getId()).thenReturn((int) USER_ID);

        model.User userModel = mock(model.User.class);
        when(userModel.getId()).thenReturn(USER_ID);
        when(userModel.getChat_id()).thenReturn(CHAT_ID);
        when(userModel.getCourses()).thenReturn(courses);

        Message message = mock(Message.class);
        Update update = mock(Update.class);
        CallbackQuery query = mock(CallbackQuery.class);

        when(update.hasMessage()).thenReturn(false);
        when(update.hasCallbackQuery()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        //update.getCallbackQuery().getData()
        when(update.getCallbackQuery()).thenReturn(query);
        when(query.getData()).thenReturn("1");

        when(message.getChatId()).thenReturn(CHAT_ID);
        when(message.getFrom()).thenReturn(user);

        UserService userServiceMock = mock(UserService.class);
        //if (userRegistered) {
        when(userServiceMock.findUser(USER_ID)).thenReturn(userModel);
        //} else {
        //    when(userServiceMock.findUser(USER_ID)).thenReturn(null);
        //}
        if (isSubscribedForCourse) {
            when(userServiceMock.getCourseById(userModel,1)).thenReturn(new Course("Name",1));
        } else {
            when(userServiceMock.getCourseById(userModel,1)).thenReturn(null);
        }
        ArgumentCaptor<Course> argument = ArgumentCaptor.forClass(Course.class);
        doAnswer((Answer) invocation -> {
            if(isSubscribedForCourse)
            {
                fail();
            }
            else
            {
                Course arg1 = invocation.getArgument(1);
                assertEquals(1, arg1.getId());
                assertEquals("course name", arg1.getName());
            }
            return null;
        }).when(userServiceMock).subscribeUser(anyObject(),argument.capture());

        ArgumentCaptor<Course> argument2 = ArgumentCaptor.forClass(Course.class);
        doAnswer((Answer) invocation -> {
            if(!isSubscribedForCourse)
            {
                fail();
            }
            else
            {
                Course arg1 = invocation.getArgument(1);
                assertEquals(1, arg1.getId());
                assertEquals("course name", arg1.getName());
            }
            return null;
        }).when(userServiceMock).unSubscribeUser(anyObject(),argument2.capture());

        Bot.setUserService(userServiceMock);

    }


    /**
     * Test for [Use case #5] alternative scope
     * Tests user unsubscription from bot in case when user wants to continue use of bot
     */
    @Test
    public void testUnsubscriptionFromBot_UserDisAgree() {
        Bot bot = new Bot();


        User user = mock(User.class);
        when(user.getId()).thenReturn((int) USER_ID);

        model.User userModel = mock(model.User.class);
        when(userModel.getId()).thenReturn(USER_ID);
        when(userModel.getChat_id()).thenReturn(CHAT_ID);

        Message message = mock(Message.class);
        Update update = mock(Update.class);
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("Unsubscribe");
        when(message.getChatId()).thenReturn(CHAT_ID);
        when(message.getFrom()).thenReturn(user);

        UserService userServiceMock = mock(UserService.class);

        when(userServiceMock.findUser(USER_ID)).thenReturn(userModel);
        when(userServiceMock.findUser(CHAT_ID)).thenReturn(userModel);

        Bot.setUserService(userServiceMock);

        SendMessage x = (SendMessage) bot.getMethod(update);
        ReplyKeyboardMarkup mu = (ReplyKeyboardMarkup) x.getReplyMarkup();
        List<String> buttonTexts = new ArrayList<>();
        for (KeyboardRow row : mu.getKeyboard()) {
            for (KeyboardButton b : row) {
                buttonTexts.add(b.getText());
            }
        }

        assertThat(buttonTexts, containsInAnyOrder("Yes", "No"));

        when(message.getText()).thenReturn("No");
        SendMessage x2 = (SendMessage) bot.getMethod(update);

        doAnswer(invocation -> {
            fail();
            return null;
        }).when(userServiceMock).deleteUser(isNotNull());
    }

    /**
     * Test for [Use case #5]
     * Tests user unsubscription from bot
     */
    @Test
    public void testUnsubscriptionFromBot_UserAgree() {
        Bot bot = new Bot();


        User user = mock(User.class);
        when(user.getId()).thenReturn((int) USER_ID);

        model.User userModel = mock(model.User.class);
        when(userModel.getId()).thenReturn(USER_ID);
        when(userModel.getChat_id()).thenReturn(CHAT_ID);

        Message message = mock(Message.class);
        Update update = mock(Update.class);
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("Unsubscribe");
        when(message.getChatId()).thenReturn(CHAT_ID);
        when(message.getFrom()).thenReturn(user);

        UserService userServiceMock = mock(UserService.class);
        when(userServiceMock.findUser(USER_ID)).thenReturn(userModel);
        when(userServiceMock.findUser(CHAT_ID)).thenReturn(userModel);
        Bot.setUserService(userServiceMock);

        SendMessage x = (SendMessage) bot.getMethod(update);
        ReplyKeyboardMarkup mu = (ReplyKeyboardMarkup) x.getReplyMarkup();
        List<String> buttonTexts = new ArrayList<>();
        for (KeyboardRow row : mu.getKeyboard()) {
            for (KeyboardButton b : row) {
                buttonTexts.add(b.getText());
            }
        }

        assertThat(buttonTexts, containsInAnyOrder("Yes", "No"));

        when(message.getText()).thenReturn("Yes");
        SendMessage x2 = (SendMessage) bot.getMethod(update);

        verify(userServiceMock, times(1)).deleteUser(userModel);
    }


    /**
     * Test for bot security
     * User wants to send smth for bot without subscription
     */
    @Test
    public void testUnauthorisedCommand() {
        Bot bot = new Bot();
        final String UNSUBSCRIBED_USER_ERROR_MSG = "You are unsubscribed for this bot /start to start";

        User user = mock(User.class);
        when(user.getId()).thenReturn((int) USER_ID);

        model.User userModel = mock(model.User.class);
        when(userModel.getId()).thenReturn(USER_ID);
        when(userModel.getChat_id()).thenReturn(CHAT_ID);

        Message message = mock(Message.class);
        Update update = mock(Update.class);
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("Unsubscribe");
        when(message.getChatId()).thenReturn(CHAT_ID);
        when(message.getFrom()).thenReturn(user);

        UserService userServiceMock = mock(UserService.class);
        when(userServiceMock.findUser(USER_ID)).thenReturn(null);
        when(userServiceMock.findUser(CHAT_ID)).thenReturn(null);
        Bot.setUserService(userServiceMock);

        SendMessage x = (SendMessage) bot.getMethod(update);
        assertEquals(x.getText(),UNSUBSCRIBED_USER_ERROR_MSG);
    }


    /**
     * Test for [Use case #2]
     * View entire elective course schedule
     */
    @Test
    public void testViewEntireElectiveCourseSchedule() {
        Bot bot = new Bot();
        final String SPREAD_SHEET = "https://docs.google.com/spreadsheets/d/15xpB2ckMNQLmgwTmJv0DsRjrvsjAYPAC0Im3QFWdqZM/edit#gid=516269660";

        User user = mock(User.class);
        when(user.getId()).thenReturn((int) USER_ID);

        model.User userModel = mock(model.User.class);
        when(userModel.getId()).thenReturn(USER_ID);
        when(userModel.getChat_id()).thenReturn(CHAT_ID);

        Message message = mock(Message.class);
        Update update = mock(Update.class);
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("All courses");
        when(message.getChatId()).thenReturn(CHAT_ID);
        when(message.getFrom()).thenReturn(user);

        UserService userServiceMock = mock(UserService.class);
        when(userServiceMock.findUser(USER_ID)).thenReturn(userModel);
        when(userServiceMock.findUser(CHAT_ID)).thenReturn(userModel);
        Bot.setUserService(userServiceMock);

        SendMessage x = (SendMessage) bot.getMethod(update);
        assertEquals(x.getText(),SPREAD_SHEET);
    }

}