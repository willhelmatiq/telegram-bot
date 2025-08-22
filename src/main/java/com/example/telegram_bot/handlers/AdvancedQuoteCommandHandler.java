package com.example.telegram_bot.handlers;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
//@Primary
public class AdvancedQuoteCommandHandler implements CommandHandler{

    private final Random random = new Random();

    @PostConstruct
    public void init() {
        System.out.println(this.getClass().getCanonicalName() + "is initialized");
    }

    @Override
    public String command() {
        return "/quote";
    }

    @Override
    public String handle(Message message) {
        ResourceReader rr = new ResourceReader();
        List<String> lines = null;
        try {
            lines = rr.readAllLines("advancedQuotes.txt");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return lines.get(random.nextInt(lines.size()));
    }

    @Override
    public String description() {
        return "случайная цитата Магистра Йоды";
    }

    static class ResourceReader {
        public List<String> readAllLines(String fileName) throws Exception {
            try (var inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
                 var reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                return reader.lines().collect(Collectors.toList());
            }
        }
    }
}
