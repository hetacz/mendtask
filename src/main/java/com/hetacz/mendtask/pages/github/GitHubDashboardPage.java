package com.hetacz.mendtask.pages.github;

import com.hetacz.mendtask.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class GitHubDashboardPage extends BasePage {

    private final By headerTitle = By.cssSelector("nav[role='navigation']");

    public GitHubDashboardPage(WebDriver driver) {
        super(driver);
    }

    public GitHubDashboardPage load() {
        load("");
        return this;
    }

    public String getHeaderTitle() {
        return getVisibleElement(headerTitle).getText();
    }
}
