package com.example.telegram_bot.service;

import com.example.telegram_bot.util.AppContextHolder;
import com.example.telegram_bot.handlers.CommandHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommandDispatcher {

    private Map<String, CommandHandler> handlers;

    private final ApplicationContext context;

    @Autowired
    private List<CommandHandler> handlerList;

    public CommandDispatcher(List<CommandHandler> handlerList, ApplicationContext context) {
        this.handlers = handlerList.stream()
                .collect(Collectors.toMap(CommandHandler::command, commandHandler -> commandHandler,
                        (oldValue, newValue) -> newValue));
        this.context = context;
    }

    public String processCommand(Message message) {
        CommandHandler handler = handlers.get(message.getText());
        if (handler == null) {
            String list = handlers.values().stream()
                    .sorted(Comparator.comparing(CommandHandler::command))
                    .map(h -> "- " + h.command() + " — " + h.description())
                    .collect(Collectors.joining("\n"));

            return "Unknown command. List of available commands:\n" + list;
        }
        return handler.handle(message);
    }


    // для дебага
    @PostConstruct
    private void init() {
//        ApplicationContext ctx = AppContextHolder.getContext();
        context.getBean(CommandHandler.class);
        System.out.println("Init method");
    }
}
