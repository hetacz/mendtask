package com.hetacz.mendtask.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.hetacz.mendtask.exceptions.ResponseProcessingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.random.RandomGenerator;

@Slf4j
@UtilityClass
public class Utils {

    public final RandomGenerator RANDOM = RandomGenerator.getDefault();

    private final JsonFactory JSON_FACTORY = new JsonFactory();

    public final BiFunction<Reader, String, List<String>> RESPONSE_EXTRACTOR = (reader, fieldName) -> {
        List<String> fieldValues = new ArrayList<>();
        try (JsonParser jsonParser = JSON_FACTORY.createParser(reader)) {
            while (!jsonParser.isClosed()) {
                JsonToken token = jsonParser.nextToken();
                if (token == null) {
                    break;
                }
                if (token == JsonToken.FIELD_NAME && jsonParser.currentName().equals(fieldName)) {
                    jsonParser.nextToken();
                    String value = jsonParser.getValueAsString();
                    if (value != null) {
                        fieldValues.add(value);
                    }
                }
            }
        } catch (IOException e) {
            throw new ResponseProcessingException(e.getMessage(), e.getCause());
        }
        return fieldValues;
    };

    public String generateTestRepoName() {
        return "TestRepo-" + System.currentTimeMillis() + "-" + RANDOM.nextInt(1_000);
    }

    public boolean isNullOrBlank(String s) {
        return s == null || s.isBlank();
    }

    public List<String> splitBySemicolon(String s) {
        return List.of(s.split(";"));
    }

    public <T> void validate(T data) {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<T>> violations = validator.validate(data);
            violations.stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .forEach(log::warn);
        }
    }
}
