package com.hetacz.mendtask.tests.github;

import com.google.inject.Inject;
import com.hetacz.mendtask.api.GitHubApi;
import com.hetacz.mendtask.pages.github.GitHubCreateNewRepoPage;
import com.hetacz.mendtask.pages.github.GitHubDashboardPage;
import com.hetacz.mendtask.responses.CodeAndResponse;
import com.hetacz.mendtask.tests.BaseTest;
import com.hetacz.mendtask.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.List;

@Slf4j
public class GitHubUiTest extends BaseTest {

    @Inject
    private GitHubApi gitHubApi;

    @Test
    public void validateCookieInjectionLogin() {
        GitHubDashboardPage dashboardPage = new GitHubDashboardPage(driver());
        String headerText = dashboardPage.load().getHeaderTitle();
        Assertions.assertThat(headerText).isEqualTo("Dashboard");
    }

    @Test
    public void createNewRepo() {
        GitHubCreateNewRepoPage createNewRepoPage = new GitHubCreateNewRepoPage(driver());
        String repoName = Utils.generateTestRepoName();
        String repoTitle = createNewRepoPage.load()
                .fillRepoName(repoName)
                .waitForRepoNameCheck()
                .clickCreateRepoButton()
                .getRepoTitle();
        Assertions.assertThat(repoTitle).contains(repoName);
    }

    @Test
    public void createNewRepoApi() {
        GitHubDashboardPage dashboardPage = new GitHubDashboardPage(driver());
        String repoName = Utils.generateTestRepoName();
        Request request = gitHubApi.createRepo(repoName);
        CodeAndResponse<List<String>> response = gitHubApi.apiHelper.sendRequestAndParseResponse(request, Utils.REPO_NAME_EXTRACTOR);
        Assertions.assertThat(response.code()).isEqualTo(201);
        Assertions.assertThat(response.body()).contains(repoName);
        List<String> topRepos = dashboardPage.load().getTopRepositoriesNames();
        Assertions.assertThat(topRepos).contains(repoName);
    }

    @Test
    public void deleteRepo() {
        GitHubDashboardPage dashboardPage = new GitHubDashboardPage(driver());
        String repoName = Utils.generateTestRepoName();
        Request request = gitHubApi.createRepo(repoName);
        CodeAndResponse<List<String>> response = gitHubApi.apiHelper.sendRequestAndParseResponse(request, Utils.REPO_NAME_EXTRACTOR);
        Assertions.assertThat(response.code()).isEqualTo(201);
        Assertions.assertThat(response.body()).contains(repoName);
        Request deleteRequest = gitHubApi.deleteRepo(repoName);
        int deleteCode = gitHubApi.apiHelper.sendRequest(deleteRequest);
        Assertions.assertThat(deleteCode).isEqualTo(204);
        List<String> topRepos = dashboardPage.load().getTopRepositoriesNames();
        Assertions.assertThat(topRepos).doesNotContain(repoName);
    }
}
