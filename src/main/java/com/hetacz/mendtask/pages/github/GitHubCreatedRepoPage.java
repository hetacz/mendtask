package com.hetacz.mendtask.pages.github;

import com.hetacz.mendtask.pages.BasePage;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GitHubCreatedRepoPage extends BasePage {

    By repoTitle = By.id("repo-title-component");

    public GitHubCreatedRepoPage(WebDriver driver) {
        super(driver);
    }

    public String getRepoTitle() {
        return getVisibleElement(repoTitle).getText();
    }
}
