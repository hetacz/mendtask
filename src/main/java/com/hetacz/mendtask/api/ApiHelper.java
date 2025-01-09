package com.hetacz.mendtask.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.hetacz.mendtask.exceptions.ApiException;
import com.hetacz.mendtask.exceptions.ResponseProcessingException;
import com.hetacz.mendtask.responses.BillingPackages;
import com.hetacz.mendtask.responses.CodeAndResponse;
import com.hetacz.mendtask.utils.Utils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.function.BiFunction;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApiHelper implements ApiActions {

    private static final String RESPONSE_KEY = "name";
    OkHttpClient client;
    ObjectMapper objectMapper;
    GitHubApi gitHubApi;
    BiFunction<Reader, String, List<String>> responseParser = Utils.RESPONSE_EXTRACTOR;

    @Override
    public int authenticate() {
        return sendRequest(gitHubApi.getOctocat());
    }

    @Override
    public CodeAndResponse<List<String>> getRepoList() {
        return sendRequestAndParseResponse(gitHubApi.getRepoList(), RESPONSE_KEY, responseParser);
    }

    @Override
    public CodeAndResponse<List<String>> createRepo(String repoName) {
        return sendRequestAndParseResponse(gitHubApi.createRepo(repoName), RESPONSE_KEY, responseParser);
    }

    @Override
    public int deleteRepo(String repoName) {
        return sendRequest(gitHubApi.deleteRepo(repoName));
    }

    @Override
    public CodeAndResponse<BillingPackages> getBillingInfo() {
        return sendRequestAndSerializeResponse(gitHubApi.getPackageBilling(), BillingPackages.class);
    }

    private <T> CodeAndResponse<T> sendRequestAndSerializeResponse(Request request, Class<T> responseClass) {
        try (Response response = client.newCall(request).execute()) {
            int responseCode = response.code();
            T serializedResponse = serialize(response.body().charStream(), responseClass);
            return new CodeAndResponse<>(responseCode, serializedResponse);
        } catch (IOException e) {
            throw new ApiException(e.getMessage(), e.getCause());
        }
    }

    private <T> CodeAndResponse<T> sendRequestAndParseResponse(
            Request request,
            String lookupKey,
            BiFunction<Reader, String, T> parser
    ) {
        try (Response response = client.newCall(request).execute()) {
            int responseCode = response.code();
            T parsedResponse = parser.apply(response.body().charStream(), lookupKey);
            return new CodeAndResponse<>(responseCode, parsedResponse);
        } catch (IOException e) {
            throw new ApiException(e.getMessage(), e.getCause());
        }
    }

    private int sendRequest(Request request) {
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
