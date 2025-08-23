package com.example.telegram_bot.handlers;

import com.example.telegram_bot.annotation.MonitorPerformance;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Component
@Lazy
//@Order(value = 1)
//@Primary
public class QuoteCommandHandler implements CommandHandler {

    private final Faker faker = new Faker();

    @PostConstruct
    public void init() {
        System.out.println(this.getClass().getCanonicalName() + "is initialized");
    }

    @Override
    public String command() {
        return "/quote";
    }

    @Override
    @MonitorPerformance()
    public String handle(Message message) {
        // нужно для теста работы @MonitorPerformance()
//        try {
//            Thread.sleep( 600);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        return faker.yoda().quote();
    }

    @Override
    public String description() {
        return "случайная цитата Магистра Йоды";
    }
}
