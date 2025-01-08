package com.hetacz.mendtask.pages.github;

import com.hetacz.mendtask.pages.BasePage;
import com.hetacz.mendtask.pages.Loadable;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Set;

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
        load("login");
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

    public GitHubLoginPage submitLogin() {
        getClickableElement(signInButton).click();
        wait.until(ExpectedConditions.urlContains(configService.getPlatformProperty("base.url")));
        return this;
    }

    public Set<Cookie> getLoginCookies() {
        Set<Cookie> cookies = driver.manage().getCookies();
        Cookie hostCookie = getHostCookie(cookies);
        Cookie sanitizedHostCookie = buildSanitizedHostCookie(hostCookie);
        cookies.remove(hostCookie);
        cookies.add(sanitizedHostCookie);
        return cookies;
    }

    private Cookie getHostCookie(Set<Cookie> cookies) {
        return cookies.stream()
                .filter(cookie -> cookie.getName().contains("__Host-"))
                .findAny()
                .orElseThrow();
    }

    private Cookie buildSanitizedHostCookie(Cookie hostCookie) {
        return new Cookie.Builder(hostCookie.getName(), hostCookie.getValue()).path("/")
                .isSecure(true)
                .build();
    }
}
