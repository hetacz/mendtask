package com.hetacz.mendtask.tests.github;

import com.google.inject.Inject;
import com.hetacz.mendtask.api.GitHubApi;
import com.hetacz.mendtask.responses.BillingPackages;
import com.hetacz.mendtask.responses.CodeAndResponse;
import com.hetacz.mendtask.tests.BaseApiTest;
import com.hetacz.mendtask.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

@Slf4j
public class GitHubApiTest extends BaseApiTest {

    @Inject
    private GitHubApi gitHubApi;

    @Test
    public void apiAuth() {
        Request request = gitHubApi.getOctocat();
        int code = gitHubApi.apiHelper.sendRequest(request);
        Assertions.assertThat(code).isEqualTo(200);
    }

    @Test
    public void apiCheckPackageBilling() {
        Request request = gitHubApi.getPackageBilling();
        CodeAndResponse<BillingPackages> response = gitHubApi.apiHelper.sendRequestAndSerializeResponse(request, BillingPackages.class);
        Assertions.assertThat(response.code()).isEqualTo(200);
        Utils.validate(response.body());
    }
}
