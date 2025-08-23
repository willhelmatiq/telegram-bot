package com.example.telegram_bot.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AdminConfig {

    private final List<Long> adminIds;

    public AdminConfig(@Value("${telegrambots.adminUsers}") String adminUsers) {
        this.adminIds = Stream.of(adminUsers.split(","))
                .map(String::trim)
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    public boolean isAdmin(Long userId) {
        return adminIds.contains(userId);
    }
}
