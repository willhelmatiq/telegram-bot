package com.example.telegram_bot.handlers;

import com.example.telegram_bot.annotation.AdminOnly;
import com.example.telegram_bot.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.stream.Collectors;

@Component
public class StatisticCommandHandler implements CommandHandler{

    @Autowired
    private StatisticService statisticService;

    @Override
    public String command() {
        return "/stats";
    }

    @AdminOnly
    @Override
    public String handle(Message message) {
        String statistic = statisticService.getStatistics(message.getFrom().getId()).stream()
                .map(StatisticService.HistoryItem::toString)
                .collect(Collectors.joining("\n"));
        if (statistic.isBlank()) {
            return "There are no statistics yet";
        }
        return statistic;
    }

    @Override
    public String description() {
        return "показать статистику пользователей";
    }
}
