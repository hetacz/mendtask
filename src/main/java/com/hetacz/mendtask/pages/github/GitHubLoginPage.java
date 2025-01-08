package com.hetacz.mendtask.pages.github;

import com.hetacz.mendtask.constants.AUT;
import com.hetacz.mendtask.pages.BasePage;
import com.hetacz.mendtask.pages.Loadable;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GitHubLoginPage extends BasePage implements Loadable<GitHubLoginPage> {

    By emailField = By.id("login_field");
    By passwordField = By.id("password");
    By signInButton = By.name("commit");

    public GitHubLoginPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public GitHubLoginPage load() {
        load(AUT.GITHUB, "login");
        return this;
    }

    public GitHubLoginPage fillEmail(String email) {
        getVisibleElement(emailField).sendKeys(email);
        return this;
    }

    public GitHubLoginPage fillPassword(String password) {
        getVisibleElement(passwordField).sendKeys(password);
        return this;
    }

    public GitHubDashboardPage submitLogin() {
        getClickableElement(signInButton).click();
        wait.until(ExpectedConditions.urlContains(autConfig.getProperty(AUT.GITHUB, "base.url")));
        return new GitHubDashboardPage(driver);
    }
}
