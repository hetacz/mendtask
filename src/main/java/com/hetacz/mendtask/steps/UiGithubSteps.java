package com.hetacz.mendtask.steps;

import com.hetacz.mendtask.constants.AUT;
import com.hetacz.mendtask.pages.AuthorizedActions;
import com.hetacz.mendtask.pages.CreateRepoActions;
import com.hetacz.mendtask.pages.LoginActions;
import com.hetacz.mendtask.pages.github.GitHubCreateNewRepoPage;
import com.hetacz.mendtask.pages.github.GitHubDashboardPage;
import com.hetacz.mendtask.pages.github.GitHubLoginPage;
import com.hetacz.mendtask.service.AutConfigService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Set;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UiGithubSteps implements AuthorizedActions, CreateRepoActions, LoginActions {

    WebDriver driver;
    GitHubLoginPage loginPage;
    GitHubDashboardPage dashboardPage;
    GitHubCreateNewRepoPage createNewRepoPage;

    public UiGithubSteps(WebDriver driver) {
        this.driver = driver;
        this.loginPage = new GitHubLoginPage(driver);
        this.dashboardPage = new GitHubDashboardPage(driver);
        this.createNewRepoPage = new GitHubCreateNewRepoPage(driver);
    }

    @Override
    public AuthorizedActions openDashboard() {
        dashboardPage.load();
        return this;
    }

    @Override
    public String getLoginProof() {
        return dashboardPage.getHeaderTitle();
    }

    @Override
    public List<String> getRepositories() {
        return dashboardPage.getTopRepositoriesNames();
    }

    @Override
    public Set<Cookie> extractCookies() {
        return dashboardPage.getLoginCookies();
    }

    @Override
    public CreateRepoActions openRepoCreator() {
        createNewRepoPage.load();
        return this;
    }

    @Override
    public String createNewRepo(String repoName) {
        return createNewRepoPage.fillRepoName(repoName).waitForRepoNameCheck().clickCreateRepoButton().getRepoTitle();
    }

    @Override
    public LoginActions openLogin() {
        loginPage.load();
        return this;
    }

    @Override
    public AuthorizedActions performLogin() {
        loginPage.fillEmail(getProps("email")).fillPassword(getProps("password")).submitLogin();
        return this;
    }

    @Override
    public AuthorizedActions injectCookies(Set<Cookie> cookies) {
        dashboardPage.load();
        cookies.forEach(driver.manage()::addCookie);
        driver.navigate().refresh();
        return this;
    }

    private String getProps(String key) {
        return AutConfigService.getProperty(AUT.GITHUB, key);
    }
}
