package com.hetacz.mendtask.pages.github;

import com.hetacz.mendtask.pages.BasePage;
import com.hetacz.mendtask.pages.Loadable;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GitHubDashboardPage extends BasePage implements Loadable<GitHubDashboardPage> {

    By headerTitle = By.cssSelector("nav[role='navigation']");
    By topRepositoriesList = By.cssSelector("aside li a:last-child");

    public GitHubDashboardPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public GitHubDashboardPage load() {
        load("");
        return this;
    }

    public String getHeaderTitle() {
        return getVisibleElement(headerTitle).getText();
    }

    public List<String> getTopRepositoriesNames() {
        return getVisibleElements(topRepositoriesList).stream()
                .map(WebElement::getText)
                .map(s -> s.split("/")[1])
                .map(String::strip)
                .toList();
    }
}
