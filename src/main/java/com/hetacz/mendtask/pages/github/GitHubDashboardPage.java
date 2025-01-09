package com.hetacz.mendtask.pages.github;

import com.hetacz.mendtask.constants.AUT;
import com.hetacz.mendtask.pages.BasePage;
import com.hetacz.mendtask.pages.Loadable;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GitHubDashboardPage extends BasePage implements Loadable<GitHubDashboardPage> {

    By headerTitle = By.cssSelector("nav[role='navigation']");
    By topRepositoriesList = By.cssSelector("aside li a:last-child");

    public GitHubDashboardPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public GitHubDashboardPage load() {
        load(AUT.GITHUB, "");
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

    public Set<Cookie> getLoginCookies() {
        Set<Cookie> cookies = driver.manage().getCookies();
        Cookie hostCookie = getHostCookie(cookies);
        Cookie sanitizedHostCookie = buildSanitizedHostCookie(hostCookie);
        cookies.remove(hostCookie);
        cookies.add(sanitizedHostCookie);
        return cookies;
    }

    private Cookie getHostCookie(Set<Cookie> cookies) {
        return cookies.stream().filter(cookie -> cookie.getName().contains("__Host-")).findAny().orElseThrow();
    }

    private Cookie buildSanitizedHostCookie(Cookie hostCookie) {
        return new Cookie.Builder(hostCookie.getName(), hostCookie.getValue()).path("/").isSecure(true).build();
    }
}
