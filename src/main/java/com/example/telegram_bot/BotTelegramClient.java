package com.example.telegram_bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;

@Component
public class BotTelegramClient extends OkHttpTelegramClient {
    public BotTelegramClient(@Value("${telegrambots.botToken}") String token) {
        super(token);
    }
}
