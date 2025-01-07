package com.hetacz.mendtask.api;

import com.google.inject.Inject;
import com.hetacz.mendtask.service.ConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class GitHubApi {

    private static final String HEADER_AUTH = "Authorization";
    private static final String HEADER_VERSION = "X-GitHub-Api-Version";
    public final ApiHelper apiHelper;
    private final ConfigService configService;

    public Request sampleRequest() {
        return new Request.Builder()
                .url(configService.getPlatformProperty("api.url") + "octocat")
                .header(HEADER_AUTH, "Bearer " + apiHelper.getApiToken())
                .header(HEADER_VERSION, apiHelper.getApiVersion())
                .get()
                .build();
    }
}
