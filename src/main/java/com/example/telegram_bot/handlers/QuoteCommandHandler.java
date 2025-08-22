package com.example.telegram_bot.handlers;

import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Component
@Lazy
//@Order(value = 1)
@Primary
public class QuoteCommandHandler implements CommandHandler {

    private final Faker faker = new Faker();

    @PostConstruct
    public void init() {
        System.out.println(this.getClass().getCanonicalName() + "is initialized");
//        int attempt = 10;
//        Set<String> quotes = new HashSet<>();
//        while (true){
//            String quote = faker.yoda().quote();
//            while (quotes.contains(quote) && attempt > 0) {
//                quote = faker.yoda().quote();
//                attempt--;
//            }
//            if (attempt > 0) {
//                quotes.add(quote);
//                attempt = 10;
//            } else {
//                break;
//            }
//        }
//        String result = String.join("\n", quotes);
    }

    @Override
    public String command() {
        return "/quote";
    }

    @Override
    public String handle(Message message) {
        return faker.yoda().quote();
    }

    @Override
    public String description() {
        return "случайная цитата Магистра Йоды";
    }
}
