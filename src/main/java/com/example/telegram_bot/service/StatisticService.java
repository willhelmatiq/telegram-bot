package com.example.telegram_bot.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class StatisticService {

    private final Map<Long, List<HistoryItem>> statistics = new HashMap<>();

    public void addStatistic(Message message) {
        Long userId = message.getFrom().getId();
        statistics.putIfAbsent(userId, new ArrayList<>());
        statistics.get(userId).add(new HistoryItem(userId, message.getText(), LocalDateTime.now()));

    }
    public List<HistoryItem> getStatistics(Long userId) {
        return statistics.get(userId) == null ? Collections.EMPTY_LIST : statistics.get(userId);
    }

    @Data
    @AllArgsConstructor
    public static class HistoryItem {
        private long userId;
        private String command;
        private LocalDateTime dateTime;
    }
}
