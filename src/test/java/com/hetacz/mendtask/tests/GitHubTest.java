package com.hetacz.mendtask.tests;

import com.google.inject.Inject;
import com.hetacz.mendtask.api.GitHubApi;
import com.hetacz.mendtask.pages.github.GitHubDashboardPage;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

@Slf4j
public class GitHubTest extends BaseTest {

    @Inject
    GitHubApi gitHubApi;

    @Test(groups = "github-ui")
    public void validateCookieInjectionLogin() {
        GitHubDashboardPage dashboardPage = new GitHubDashboardPage(driver());
        String headerText = dashboardPage.load().getHeaderTitle();
        Assertions.assertThat(headerText).isEqualTo("Dashboard");
    }

    @Test
    public void apiLogin() {
        Request request = gitHubApi.sampleRequest();
        int code = gitHubApi.apiHelper.sendRequest(request);
        Assertions.assertThat(code).isEqualTo(200);
    }

    @Test(groups = "github-ui")
    public void createNewRepo() {
        GitHubDashboardPage dashboardPage = new GitHubDashboardPage(driver());
    }
}
