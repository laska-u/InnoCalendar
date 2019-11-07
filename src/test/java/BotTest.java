import model.Course;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import service.UserService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

class BotTest {
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
        when(parserMock.getCourses()).thenReturn(subscribedTo.keySet().stream().map(Course::getName).collect(Collectors.toList()));

        final long chatId = 1123;
        final long userId = 123123123;

        User chooseCoursesUser = mock(User.class);
        when(chooseCoursesUser.getId()).thenReturn((int) userId);

        model.User userModel = mock(model.User.class);
        when(userModel.getId()).thenReturn(userId);
        when(userModel.getChat_id()).thenReturn(chatId);

        UserService userServiceMock = mock(UserService.class);
        when(userServiceMock.findUser(chatId)).thenReturn(userModel);
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
        when(chooseCoursesMessage.getChatId()).thenReturn(chatId);
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
}