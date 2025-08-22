package com.example.telegram_bot.handlers;

import org.telegram.telegrambots.meta.api.objects.message.Message;

public interface CommandHandler {
    String command();
    String handle(Message message);
    String description();
}
