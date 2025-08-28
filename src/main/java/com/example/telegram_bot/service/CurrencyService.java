package com.example.telegram_bot.service;

import com.example.telegram_bot.dto.ValCursDto;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class CurrencyService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final static String USD_CURRENCY = "USD";

    public String getUsdRate() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String url = "http://www.cbr.ru/scripts/XML_daily.asp?date_req=%s".formatted(today);

        String xml = restTemplate.getForObject(url, String.class);
        try {
            final var valCurs = getValCursDto(xml);

            return valCurs.getValutes().stream()
                    .filter(v -> USD_CURRENCY.equals(v.getCharCode()))
                    .findFirst()
                    .map(v -> String.format("Курс доллара: %s руб. за %d %s", v.getValue(), v.getNominal(), v.getCharCode()))
                    .orElse("Курс USD не найден");
        } catch (Exception e) {
            return "Ошибка при получении курса: " + e.getMessage();
        }
    }

    private static ValCursDto getValCursDto(String xml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ValCursDto.class);
        var unmarshaller = context.createUnmarshaller();
        ValCursDto valCurs = (ValCursDto) unmarshaller.unmarshal(new StringReader(xml));
        return valCurs;
    }
}
