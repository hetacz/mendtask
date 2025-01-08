package com.hetacz.mendtask.pages.github;

import com.hetacz.mendtask.pages.BasePage;
import com.hetacz.mendtask.pages.Loadable;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GitHubCreateNewRepoPage extends BasePage implements Loadable<GitHubCreateNewRepoPage> {

    By repoNameField = By.cssSelector("[data-testid='repository-name-input']");
    By repoCheckSuccess = By.id("RepoNameInput-is-available");
    By createRepoButton = By.cssSelector("form button[type='submit']");

    public GitHubCreateNewRepoPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public GitHubCreateNewRepoPage load() {
        load("new");
        return this;
    }

    public GitHubCreateNewRepoPage fillRepoName(String repoName) {
        getClickableElement(repoNameField).sendKeys(repoName);
        return this;
    }

    public GitHubCreateNewRepoPage waitForRepoNameCheck() {
        getVisibleElement(repoCheckSuccess);
        return this;
    }

    public GitHubCreatedRepoPage clickCreateRepoButton() {
        getClickableElement(createRepoButton).click();
        return new GitHubCreatedRepoPage(driver);
    }
}
