package com.example.telegram_bot;

import com.example.telegram_bot.service.CommandDispatcher;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {
        "telegrambots.enabled=false"
})
class CommandResponsesTest {

    @Autowired
    private CommandDispatcher dispatcher;

    private Message msg(String text) {
        Message mock = Mockito.mock(Message.class);
        when(mock.getText()).thenReturn(text);
        return mock;
    }

    @Test
    void botRespondsToKnownCommands() {
        assertThat(dispatcher.processCommand(msg("/currency"))).isNotBlank();
        assertThat(dispatcher.processCommand(msg("/advice"))).isNotBlank();
        assertThat(dispatcher.processCommand(msg("/quote"))).isNotBlank();
        assertThat(dispatcher.processCommand(msg("/chuck_norris_fact"))).isNotBlank();
    }

    @Test
    void unknownCommandPrintsAvailableList() {
        String resp = dispatcher.processCommand(msg("/unknown"));
        assertThat(resp).startsWith("Unknown command. List of available commands:");
        assertThat(resp).contains("/quote").contains("/currency").contains("/advice").contains("/chuck_norris_fact");
    }
}
