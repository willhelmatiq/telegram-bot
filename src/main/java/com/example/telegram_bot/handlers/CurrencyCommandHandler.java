package com.example.telegram_bot.handlers;

import com.example.telegram_bot.service.CurrencyService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Component
@RequiredArgsConstructor
public class CurrencyCommandHandler implements CommandHandler {

    private final CurrencyService currencyService;

    @PostConstruct
    public void init() {
        System.out.println(this.getClass().getCanonicalName() + "is initialized");
    }

    @Override
    public String command() {
        return "/currency";
    }

    @Override
    public String handle(Message message) {
        return currencyService.getUsdRate();
    }

    @Override
    public String description() {
        return "актуальный курс доллара";
    }
}
