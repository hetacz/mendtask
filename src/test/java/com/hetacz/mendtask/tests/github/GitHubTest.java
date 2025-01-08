package com.hetacz.mendtask.tests.github;

import com.google.inject.Inject;
import com.hetacz.mendtask.api.GitHubApi;
import com.hetacz.mendtask.pages.github.GitHubCreateNewRepoPage;
import com.hetacz.mendtask.pages.github.GitHubDashboardPage;
import com.hetacz.mendtask.responses.BillingPackages;
import com.hetacz.mendtask.responses.CodeAndResponse;
import com.hetacz.mendtask.tests.BaseTest;
import com.hetacz.mendtask.utils.Utils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;

@Slf4j
public class GitHubTest extends BaseTest {

    @Inject
    private GitHubApi gitHubApi;

    @Test
    public void apiAuth() {
        Request request = gitHubApi.getOctocat();
        int code = gitHubApi.apiHelper.sendRequest(request);
        Assertions.assertThat(code).isEqualTo(200);
    }

    @Test(groups = "github-ui")
    public void validateCookieInjectionLogin() {
        GitHubDashboardPage dashboardPage = new GitHubDashboardPage(driver());
        String headerText = dashboardPage.load().getHeaderTitle();
        Assertions.assertThat(headerText).isEqualTo("Dashboard");
    }

    @Test
    public void apiCheckPackageBilling() {
        Request request = gitHubApi.getPackageBilling();
        CodeAndResponse<BillingPackages> response = gitHubApi.apiHelper.sendRequestAndSerializeResponse(request, BillingPackages.class);
        Assertions.assertThat(response.code()).isEqualTo(200);
        validate(response.body());
        System.out.println(response.body());
    }

    @Test(groups = "github-ui")
    public void createNewRepo() {
        String repoName = Utils.generateTestRepoName();
        GitHubCreateNewRepoPage createNewRepoPage = new GitHubCreateNewRepoPage(driver());
        String repoTitle = createNewRepoPage.load()
                .fillRepoName(repoName)
                .waitForRepoNameCheck()
                .clickCreateRepoButton()
                .getRepoTitle();
        Assertions.assertThat(repoTitle).contains(repoName);
    }

    @Test(groups = "github-ui")
    public void createNewRepoApi() {
        String repoName = Utils.generateTestRepoName();
        Request request = gitHubApi.createRepo(repoName);
        CodeAndResponse<List<String>> response = gitHubApi.apiHelper.sendRequestAndParseResponse(request, Utils.REPO_NAME_EXTRACTOR);
        Assertions.assertThat(response.code()).isEqualTo(201);
        Assertions.assertThat(response.body()).contains(repoName);
        GitHubDashboardPage dashboardPage = new GitHubDashboardPage(driver());
        List<String> topRepos = dashboardPage.load().getTopRepositoriesNames();
        Assertions.assertThat(topRepos).contains(repoName);
    }

    @Test(groups = "github-ui")
    public void deleteRepo() {
        String repoName = Utils.generateTestRepoName();
        Request request = gitHubApi.createRepo(repoName);
        CodeAndResponse<List<String>> response = gitHubApi.apiHelper.sendRequestAndParseResponse(request, Utils.REPO_NAME_EXTRACTOR);
        Assertions.assertThat(response.code()).isEqualTo(201);
        Assertions.assertThat(response.body()).contains(repoName);
        Request deleteRequest = gitHubApi.deleteRepo(repoName);
        int deleteCode = gitHubApi.apiHelper.sendRequest(deleteRequest);
        Assertions.assertThat(deleteCode).isEqualTo(204);
        GitHubDashboardPage dashboardPage = new GitHubDashboardPage(driver());
        List<String> topRepos = dashboardPage.load().getTopRepositoriesNames();
        Assertions.assertThat(topRepos).doesNotContain(repoName);
    }

    private <T> void validate(T data) {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<T>> violations = validator.validate(data);
            violations.stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .forEach(log::warn);
        }
    }
}
