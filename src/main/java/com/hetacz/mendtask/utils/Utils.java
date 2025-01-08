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
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

@Slf4j
@UtilityClass
public class Utils {

    private final Random RANDOM = new Random();
    private final JsonFactory JSON_FACTORY = new JsonFactory();

    public final Function<Reader, List<String>> REPO_NAME_EXTRACTOR = (reader) -> {
        List<String> repoNames = new ArrayList<>();
        try (JsonParser jsonParser = JSON_FACTORY.createParser(reader)) {
            while (!jsonParser.isClosed()) {
                JsonToken token = jsonParser.nextToken();
                if (token == null) {
                    break;
                }
                if (token == JsonToken.FIELD_NAME && jsonParser.currentName().equals("name")) {
                    jsonParser.nextToken();
                    repoNames.add(jsonParser.getValueAsString());
                }
            }
        } catch (IOException e) {
            throw new ResponseProcessingException(e.getMessage(), e.getCause());
        }
        return repoNames;
    };

    public String generateTestRepoName() {
        return "TestRepo-" + System.currentTimeMillis() + "-" + new Random().nextInt(1_000);
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
