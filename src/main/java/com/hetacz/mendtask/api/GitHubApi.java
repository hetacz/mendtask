package com.hetacz.mendtask.api;

import com.google.inject.Inject;
import com.hetacz.mendtask.constants.AUT;
import com.hetacz.mendtask.service.AutConfigService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GitHubApi {

    private static final MediaType MEDIA_TYPE_JSON_UTF8 = MediaType.get("application/json;charset=utf-8");
    private static final String HEADER_ACCEPT = "Accept";
    private static final String HEADER_AUTH = "Authorization";
    private static final String HEADER_VERSION = "X-GitHub-Api-Version";
    private static final String OCTOCAT = "octocat";
    private static final String REPOS = "user/repos";
    private static final String DELETE_REPO = "repos/%s/%s";
    private static final String PACKAGE_BILLING = "users/%s/settings/billing/packages";
    private static final String USERNAME = AutConfigService.getProperty(AUT.GITHUB, "username");
    ApiService apiService;
    Request.Builder requestBuilder;

    public Request getOctocat() {
        return requestBuilder.url(apiService.getApiUrl() + OCTOCAT).headers(generateHeaders()).get().build();
    }

    public Request getRepoList() {
        return requestBuilder.url(apiService.getApiUrl() + REPOS).headers(generateHeaders()).get().build();
    }

    public Request createRepo(String repoName) {
        return requestBuilder.url(apiService.getApiUrl() + REPOS)
                .headers(generateHeaders())
                .post(RequestBody.create("{\"name\":\"" + repoName + "\"}", MEDIA_TYPE_JSON_UTF8))
                .build();
    }

    public Request deleteRepo(String repoName) {
        return requestBuilder.url(apiService.getApiUrl() + DELETE_REPO.formatted(USERNAME, repoName))
                .headers(generateHeaders())
                .delete()
                .build();
    }

    public Request getPackageBilling() {
        return requestBuilder.url(apiService.getApiUrl() + PACKAGE_BILLING.formatted(USERNAME))
                .headers(generateHeaders())
                .get()
                .build();
    }

    private Headers generateHeaders() {
        return new Headers.Builder().add(HEADER_AUTH, "Bearer " + apiService.getApiToken())
                .add(HEADER_VERSION, apiService.getApiVersion())
                .add(HEADER_ACCEPT, apiService.getAcceptHeader())
                .build();
    }
}
