package com.example.telegram_bot;

import com.example.telegram_bot.service.CommandDispatcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest(properties = {
        "telegrambots.enabled=false"
})
class LazyInitializationTest {

    @Autowired
    private CommandDispatcher dispatcher;

    private Message msg(String text) {
        Message mock = Mockito.mock(Message.class);
        when(mock.getText()).thenReturn(text);
        return mock;
    }

    @Test
    void quoteHandlerInitializedOnlyOnFirstCall(CapturedOutput output) {
        // На старте приложения в логе не должно быть сообщения от ленивого бина
        assertThat(output.getOut())
                .doesNotContain("QuoteCommandHandler initialized");

        // Первый вызов /quote — бин создаётся, @PostConstruct печатает сообщение
        dispatcher.processCommand(msg("/quote"));
        assertThat(output.getOut())
                .contains("QuoteCommandHandler initialized");

        // Повторный вызов — сообщение больше не печатается
        String before = output.getOut();
        dispatcher.processCommand(msg("/quote"));
        assertThat(output.getOut())
                .isEqualTo(before);
    }
}
