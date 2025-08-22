package com.example.telegram_bot;

import com.example.telegram_bot.handlers.CommandHandler;
import com.example.telegram_bot.service.CommandDispatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "telegrambots.enabled=false" // чтобы стартер бота не стартовал
})
class HandlersWiringTest {

    @Autowired
    private List<CommandHandler> handlerList;

    @Autowired
    private CommandDispatcher dispatcher;

    @Test
    void listAndMapHaveExpectedSizes() {
        assertThat(handlerList).isNotEmpty();
        // вытащим приватную Map
        @SuppressWarnings("unchecked")
        Map<String, CommandHandler> map =
                (Map<String, CommandHandler>) ReflectionTestUtils.getField(dispatcher, "handlers");

        assertThat(map).isNotNull();
        assertThat(map.size()).isLessThanOrEqualTo(handlerList.size());

        // как минимум три команды по задаче
        assertThat(map.keySet()).contains("/quote", "/currency", "/advice", "/chuck_norris_fact");
    }
}
