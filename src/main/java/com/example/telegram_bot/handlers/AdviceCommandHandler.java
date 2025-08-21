package com.example.telegram_bot.handlers;

import com.example.telegram_bot.model.Message;
import org.springframework.stereotype.Component;

@Component
public class AdviceCommandHandler implements CommandHandler {
    @Override
    public String command() {
        return "";
    }

    @Override
    public String handle(Message message) {
        return "";
    }
}
