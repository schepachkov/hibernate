package ru.schepachkov.converter;

import ru.schepachkov.entity.Birthday;

import javax.persistence.AttributeConverter;
import java.sql.Date;
import java.util.Optional;

public class BirthdayConverter implements AttributeConverter<Birthday, Date> {



    @Override
    public Date convertToDatabaseColumn(Birthday attribute) {
        return Optional.ofNullable(attribute)
            .map(Birthday::getBirthday)
            .map(Date::valueOf)
            .orElse(null);
    }

    @Override
    public Birthday convertToEntityAttribute(Date dbData) {
        return Optional.ofNullable(dbData)
            .map(Date::toLocalDate)
            .map(Birthday::new)
            .orElse(null);
    }
}
