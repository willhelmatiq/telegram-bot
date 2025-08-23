package com.example.telegram_bot.handlers;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;
import java.util.Random;

@Component
public class AdviceCommandHandler implements CommandHandler {

    private final Random random = new Random();

    private final List<String> advices = List.of(
            "Не откладывай дела на завтра.",
            "Учись на ошибках, но не зацикливайся на них.",
            "Береги время – это самый ценный ресурс.",
            "Слушай больше, чем говоришь.",
            "Заботься о здоровье каждый день."
    );

    @PostConstruct
    public void init() {
        System.out.println(this.getClass().getCanonicalName() + "is initialized");
    }

    @Override
    public String command() {
        return "/advice";
    }

    @Override
    public String handle(Message message) {
//        throw new RuntimeException("Test");   //для проверки нотификаций
        return advices.get(random.nextInt(advices.size()));
    }

    @Override
    public String description() {
        return "случайный совет дня";
    }
}
