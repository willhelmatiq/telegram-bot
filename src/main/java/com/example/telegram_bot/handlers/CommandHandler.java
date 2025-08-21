package com.example.telegram_bot.handlers;

import com.example.telegram_bot.model.Message;

public interface CommandHandler {
    String command();
    String handle(Message message);
}
