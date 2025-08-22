package com.example.telegram_bot;

import com.example.telegram_bot.handlers.CommandHandler;
import com.example.telegram_bot.service.CommandDispatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(properties = {
        "telegrambots.enabled=false"
})
public class LastHandlerInMapTest {
    @Autowired
    private List<CommandHandler> handlerList;

    @Autowired
    private CommandDispatcher dispatcher;

    @Test
    void lastRegisteredQuoteHandlerWins() {
        // возьмём всех обработчиков с командой /quote
        List<CommandHandler> quotes = handlerList.stream()
                .filter(h -> "/quote".equals(h.command()))
                .toList();

        // если в проекте действительно 2+ реализаций /quote — проверяем правило
        if (quotes.size() >= 2) {
            CommandHandler expectedLast = quotes.getLast();

            @SuppressWarnings("unchecked")
            Map<String, CommandHandler> map =
                    (Map<String, CommandHandler>) ReflectionTestUtils.getField(dispatcher, "handlers");

            CommandHandler actualInMap = map.get("/quote");
            assertThat(actualInMap)
                    .as("В Map по ключу /quote должен быть последний зарегистрированный бин")
                    .isSameAs(expectedLast);
        } else {
            // если всего одна реализация /quote — тест всё равно валиден
            assertThat(quotes.size()).isEqualTo(1);
        }
    }
}
