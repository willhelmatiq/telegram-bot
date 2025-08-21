package com.example.telegram_bot.handlers;

import com.example.telegram_bot.model.Message;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class QuoteCommandHandler implements CommandHandler {

    @PostConstruct
    public void init() {
        System.out.println(this.getClass().getCanonicalName() + "is initialized");
    }

    @Override
    public String command() {
        return "";
    }

    @Override
    public String handle(Message message) {
        return "";
    }
}
