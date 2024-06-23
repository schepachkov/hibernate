package ru.schepachkov.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@AllArgsConstructor
public class Birthday {

    private LocalDate birthday;


    public long getAge() {
        return ChronoUnit.YEARS.between(birthday, LocalDate.now());
    }
}
