package com.example.telegram_bot.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@ConfigurationProperties(prefix = "spring.application.notifications")
@Data
@Component
public class NotificationConfig {
    private boolean enabled = true;
    private Long chatId;
    private boolean includeStacktrace = true;
    private int stacktraceMaxLines = 20;
    private Set<String> skipExceptions = Set.of(
            "com.example.telegram_bot.exception.AccessDeniedException"
    );
}
