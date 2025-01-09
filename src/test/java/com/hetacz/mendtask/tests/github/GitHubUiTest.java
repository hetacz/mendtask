package com.hetacz.mendtask.tests.github;

import com.hetacz.mendtask.responses.CodeAndResponse;
import com.hetacz.mendtask.steps.UiGithubSteps;
import com.hetacz.mendtask.tests.BaseTest;
import com.hetacz.mendtask.utils.Utils;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.List;

@Test(groups = {"ui", "github"})
public class GitHubUiTest extends BaseTest {

    @Test
    public void validateCookieInjectionLogin() {
        String headerText = new UiGithubSteps(driver()).openDashboard().getLoginProof();
        Assertions.assertThat(headerText).isEqualTo("Dashboard");
    }

    @Test
    public void createNewRepo() {
        String repoName = Utils.generateTestRepoName();
        String repoTitle = new UiGithubSteps(driver()).openRepoCreator().createNewRepo(repoName);
        Assertions.assertThat(repoTitle).contains(repoName);
    }

    @Test
    public void createNewRepoApi() {
        String repoName = Utils.generateTestRepoName();
        CodeAndResponse<List<String>> response = apiHelper.createRepo(repoName);
        Assertions.assertThat(response.code()).isEqualTo(201);
        Assertions.assertThat(response.body()).contains(repoName);
        List<String> topRepos = new UiGithubSteps(driver()).openDashboard().getRepositories();
        Assertions.assertThat(topRepos).contains(repoName);
    }

    @Test
    public void deleteRepo() {
        String repoName = Utils.generateTestRepoName();
        CodeAndResponse<List<String>> response = apiHelper.createRepo(repoName);
        Assertions.assertThat(response.code()).isEqualTo(201);
        Assertions.assertThat(response.body()).contains(repoName);
        int deleteCode = apiHelper.deleteRepo(repoName);
        Assertions.assertThat(deleteCode).isEqualTo(204);
        List<String> topRepos = new UiGithubSteps(driver()).openDashboard().getRepositories();
        Assertions.assertThat(topRepos).doesNotContain(repoName);
    }
}
