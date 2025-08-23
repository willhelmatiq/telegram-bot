package com.example.telegram_bot.aspect;

import com.example.telegram_bot.configuration.AdminConfig;
import com.example.telegram_bot.exception.AccessDeniedException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Aspect
@Component
public class SecurityAspect {
    private static final Logger log = LoggerFactory.getLogger(SecurityAspect.class);

    @Autowired
    private AdminConfig adminConfig;

    @Before("@annotation(com.example.telegram_bot.annotation.AdminOnly) && args(message,..)")
    public void checkAccess(Message message) {
        Long userId = message.getFrom().getId();
        if (!adminConfig.isAdmin(userId)) {
            log.error("User {} not allowed", userId);
            throw new AccessDeniedException("User " + userId + " not allowed");
        }
    }
}
