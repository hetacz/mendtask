package com.hetacz.mendtask.api;

import com.google.inject.Inject;
import com.hetacz.mendtask.constants.AUT;
import com.hetacz.mendtask.service.AutConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class GitHubApi {

    private static final String HEADER_ACCEPT = "Accept";
    private static final String HEADER_AUTH = "Authorization";
    private static final String HEADER_VERSION = "X-GitHub-Api-Version";
    private static final MediaType MEDIA_TYPE_JSON_UTF8 = MediaType.get("application/json;charset=utf-8");
    public final ApiHelper apiHelper;
    private final AutConfigService autConfig;

    public Request getOctocat() {
        return new Request.Builder().url(apiHelper.getApiUrl() + "octocat")
                .headers(generateHeaders())
                .get()
                .build();
    }

    public Request getRepoList() {
        return new Request.Builder().url(apiHelper.getApiUrl() + "user/repos")
                .headers(generateHeaders())
                .get()
                .build();
    }

    public Request createRepo(String repoName) {
        return new Request.Builder().url(apiHelper.getApiUrl() + "user/repos")
                .headers(generateHeaders())
                .post(RequestBody.create("{\"name\":\"" + repoName + "\"}", MEDIA_TYPE_JSON_UTF8))
                .build();
    }

    public Request deleteRepo(String repoName) {
        return new Request.Builder().url(apiHelper.getApiUrl() + "repos/" + autConfig.getProperty(AUT.GITHUB, "username") + "/" + repoName)
                .headers(generateHeaders())
                .delete()
                .build();
    }

    public Request getPackageBilling() {
        return new Request.Builder().url(apiHelper.getApiUrl() + "users/" + autConfig.getProperty(AUT.GITHUB, "username") + "/settings/billing/packages")
                .headers(generateHeaders())
                .get()
                .build();
    }

    private Headers generateHeaders() {
        return new Headers.Builder().add(HEADER_AUTH, "Bearer " + apiHelper.getApiToken())
                .add(HEADER_VERSION, apiHelper.getApiVersion())
                .add(HEADER_ACCEPT, apiHelper.getAcceptHeader())
                .build();
    }
}
