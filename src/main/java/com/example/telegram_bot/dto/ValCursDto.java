package com.example.telegram_bot.dto;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

import java.util.List;

@XmlRootElement(name = "ValCurs")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ValCursDto {

    @XmlAttribute(name = "Date")
    private String date;

    @XmlElement(name = "Valute")
    private List<ValuteDto> valutes;
}
