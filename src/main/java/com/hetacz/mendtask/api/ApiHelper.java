package com.hetacz.mendtask.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.hetacz.mendtask.exceptions.ApiException;
import com.hetacz.mendtask.exceptions.ResponseProcessingException;
import com.hetacz.mendtask.responses.CodeAndResponse;
import com.hetacz.mendtask.service.AutConfigService;
import com.hetacz.mendtask.service.ConfigService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.Reader;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApiHelper {

    ConfigService cs;
    ObjectMapper objectMapper;
    OkHttpClient client;

    public String getApiUrl() {
        return AutConfigService.getProperty(cs.getAut(), "api.url");
    }

    public String getApiToken() {
        return AutConfigService.getProperty(cs.getAut(), "api.token");
    }

    public String getApiVersion() {
        return AutConfigService.getProperty(cs.getAut(), "api.version");
    }

    public String getAcceptHeader() {
        return AutConfigService.getProperty(cs.getAut(), "api.acceptHeader");
    }

    public <T> CodeAndResponse<T> sendRequestAndSerializeResponse(Request request, Class<T> responseClass) {
        try (Response response = client.newCall(request).execute()) {
            int responseCode = response.code();
            T serializedResponse = serialize(response.body().charStream(), responseClass);
            return new CodeAndResponse<>(responseCode, serializedResponse);
        } catch (IOException e) {
            throw new ApiException(e.getMessage(), e.getCause());
        }
    }

    public <T> CodeAndResponse<T> sendRequestAndParseResponse(Request request, Function<Reader, T> parser) {
        try (Response response = client.newCall(request).execute()) {
            int responseCode = response.code();
            T parsedResponse = parser.apply(response.body().charStream());
            return new CodeAndResponse<>(responseCode, parsedResponse);
        } catch (IOException e) {
            throw new ApiException(e.getMessage(), e.getCause());
        }
    }

    public int sendRequest(Request request) {
        try (Response response = client.newCall(request).execute()) {
            return response.code();
        } catch (IOException e) {
            throw new ApiException(e.getMessage(), e.getCause());
        }
    }

    private <T> T serialize(Reader stream, Class<T> clazz) {
        try {
            return objectMapper.readValue(stream, clazz);
        } catch (IOException e) {
            throw new ResponseProcessingException(e.getMessage(), e.getCause());
        }
    }
}
