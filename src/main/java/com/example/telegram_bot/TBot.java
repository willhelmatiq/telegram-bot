package com.example.telegram_bot;

import com.example.telegram_bot.service.CommandDispatcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class TBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final String token;
    private final TelegramClient telegramClient;
    private final CommandDispatcher commandDispatcher;

    public TBot(@Value("${telegrambots.botToken}") String token, CommandDispatcher commandDispatcher) {
        this.token = token;
        this.telegramClient = new OkHttpTelegramClient(token);
        this.commandDispatcher = commandDispatcher;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var chatId = update.getMessage().getChatId();
            String result = commandDispatcher.processCommand(update.getMessage());
            var msg = SendMessage.builder().chatId(chatId).text(result).build();
            try {
                telegramClient.execute(msg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        System.out.println("Registered bot running state is: " + botSession.isRunning());
    }

}
