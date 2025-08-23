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
        "telegrambots.enabled=false",
        "telegrambots.botToken=test-token",
        "telegrambots.adminUsers=111",
        "spring.application.notifications.chat-id=0"
})
class HandlersWiringTest {

    public static final String ADVICE = "/advice";
    public static final String CHUCK_NORRIS = "/chuck_norris_fact";
    public static final String CURRENCY = "/currency";
    public static final String QUOTE = "/quote";
    public static final String STATS = "/stats";

    @Autowired
    private List<CommandHandler> handlerList;

    @Autowired
    private CommandDispatcher dispatcher;

    @Test
    void listAndMapHaveExpectedSizes() {
        assertThat(handlerList).isNotEmpty();
        @SuppressWarnings("unchecked")
        Map<String, CommandHandler> map =
                (Map<String, CommandHandler>) ReflectionTestUtils.getField(dispatcher, "handlers");

        assertThat(map).isNotNull();
        assertThat(map.size()).isLessThanOrEqualTo(handlerList.size());

        // как минимум три команды по задаче
        assertThat(map.keySet())
                .contains(ADVICE, CHUCK_NORRIS, CURRENCY, QUOTE, STATS);
    }
}
