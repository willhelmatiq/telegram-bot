package com.example.telegram_bot;

import com.example.telegram_bot.service.CommandDispatcher;
import com.example.telegram_bot.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {
        "telegrambots.enabled=false",
        "telegrambots.botToken=test-token",
        "telegrambots.adminUsers=111",
        "spring.application.notifications.enabled=false",
        "spring.application.notifications.chat-id=0"
})
class CommandResponsesTest {

    public static final String ADVICE = "/advice";
    public static final String CHUCK_NORRIS = "/chuck_norris_fact";
    public static final String CURRENCY = "/currency";
    public static final String QUOTE = "/quote";
    public static final String STATS = "/stats";
    public static final String UNKNOWN = "/unknown";
    private static final String USD_RATE = "97.12";



    @TestConfiguration
    static class Mocks {
        @Bean
        @Primary
        CurrencyService currencyService() {
            return Mockito.mock(CurrencyService.class);
        }
    }

    @Autowired
    private CommandDispatcher dispatcher;

    @Autowired
    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        when(currencyService.getUsdRate()).thenReturn(USD_RATE);
    }


    private Message msg(String text) {
        Message mock = Mockito.mock(Message.class);
        User user = Mockito.mock(User.class);
        when(user.getId()).thenReturn(111L);
        when(mock.getFrom()).thenReturn(user);
        when(mock.getText()).thenReturn(text);
        return mock;
    }

    @Test
    void botRespondsToKnownCommands() {
        assertThat(dispatcher.processCommand(msg(ADVICE))).isNotBlank();
        assertThat(dispatcher.processCommand(msg(CHUCK_NORRIS))).isNotBlank();
        assertThat(dispatcher.processCommand(msg(CURRENCY))).isNotBlank();
        assertThat(dispatcher.processCommand(msg(QUOTE))).isNotBlank();
        assertThat(dispatcher.processCommand(msg(STATS))).isNotBlank();
    }

    @Test
    void unknownCommandPrintsAvailableList() {
        String resp = dispatcher.processCommand(msg(UNKNOWN));
        assertThat(resp).startsWith("Unknown command. List of available commands:");
        assertThat(resp)
                .contains(ADVICE)
                .contains(CHUCK_NORRIS)
                .contains(CURRENCY)
                .contains(QUOTE)
                .contains(STATS);
    }
}
