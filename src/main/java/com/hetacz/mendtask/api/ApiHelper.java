package com.hetacz.mendtask.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.hetacz.mendtask.exceptions.ApiException;
import com.hetacz.mendtask.responses.CodeAndResponse;
import com.hetacz.mendtask.service.ConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.io.Reader;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ApiHelper {

    private final ConfigService configService;
    private final ObjectMapper objectMapper;
    private final OkHttpClient client;

    public String getApiUrl() {
        return configService.getPlatformProperty("api.url");
    }

    public String getApiToken() {
        return configService.getPlatformProperty("api.token");
    }

    public String getApiVersion() {
        return configService.getPlatformProperty("api.version");
    }

    public <T> CodeAndResponse<T> sendRequestAndStoreResponse(Request request, Class<T> responseClass) {
        try (Response response = client.newCall(request).execute()) {
            int responseCode = response.code();
            ResponseBody responseBody = response.body();
            Reader stream = responseBody.charStream();
            if (responseBody == null) {
                throw new ApiException("Response body is null");
            }
            T serializedResponse = serialize(stream, responseClass);
            return new CodeAndResponse<>(responseCode, serializedResponse);
        } catch (IOException e) {
            throw new ApiException(e.getMessage());
        }
    }

    public int sendRequest(Request request) {
        try (Response response = client.newCall(request).execute()) {
            return response.code();
        } catch (IOException e) {
            throw new ApiException(e.getMessage());
        }
    }

    public CodeAndResponse<String> sendRequestAndStoreResponse(Request request) {
        try (Response response = client.newCall(request).execute()) {
            int responseCode = response.code();
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new ApiException("Response body is null");
            }
            return new CodeAndResponse<>(responseCode, responseBody.string());
        } catch (IOException e) {
            throw new ApiException(e.getMessage());
        }
    }

    private <T> T serialize(Reader stream, Class<T> clazz) {
        try {
            return objectMapper.readValue(stream, clazz);
        } catch (IOException e) {
            throw new ApiException(e.getMessage());
        }
    }
}
