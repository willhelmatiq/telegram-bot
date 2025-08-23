package com.example.telegram_bot;

import com.example.telegram_bot.exception.AccessDeniedException;
import com.example.telegram_bot.service.CommandDispatcher;
import com.example.telegram_bot.service.StatisticService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final String token;
    private final BotTelegramClient telegramClient;
    private final CommandDispatcher commandDispatcher;
    private final StatisticService statisticService;

    public TBot(@Value("${telegrambots.botToken}") String token, BotTelegramClient telegramClient, CommandDispatcher commandDispatcher, StatisticService statisticService) {
        this.token = token;
        this.telegramClient = telegramClient;
        this.commandDispatcher = commandDispatcher;
        this.statisticService = statisticService;
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
            String result;
            try {
                result = commandDispatcher.processCommand(update.getMessage());
                statisticService.addStatistic(update.getMessage());
                var msg = SendMessage.builder().chatId(chatId).text(result).build();
                telegramClient.execute(msg);
            } catch (AccessDeniedException e) {
                var msg = SendMessage.builder().chatId(chatId).text(e.getMessage()).build();
                try {
                    telegramClient.execute(msg);
                } catch (TelegramApiException ex) {
                    throw new RuntimeException(ex);
                }
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
