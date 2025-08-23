package com.example.telegram_bot.handlers;

import com.example.telegram_bot.annotation.LogCommand;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Component
@LogCommand
public class ChuckNorrisFactHandler implements CommandHandler{

    private final Faker faker = new Faker();

    @Override
    public String command() {
        return "/chuck_norris_fact";
    }

    @Override
    public String handle(Message message) {
        return faker.chuckNorris().fact();
    }

    @Override
    public String description() {
        return "забавные факты о Чаке Норрисе";
    }
}
