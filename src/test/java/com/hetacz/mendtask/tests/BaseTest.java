package com.hetacz.mendtask.tests;

import com.google.inject.Inject;
import com.hetacz.mendtask.constants.AUT;
import com.hetacz.mendtask.di.ApiModule;
import com.hetacz.mendtask.di.SeleniumModule;
import com.hetacz.mendtask.driver.WebDriverProvider;
import com.hetacz.mendtask.pages.github.GitHubLoginPage;
import com.hetacz.mendtask.service.AutConfigService;
import com.hetacz.mendtask.service.ConfigService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Guice(modules = {ApiModule.class, SeleniumModule.class})
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseTest {

    private static final Map<AUT, Set<Cookie>> COOKIES_MAP = new EnumMap<>(AUT.class);
    @Inject
    private ConfigService cs;
    @Inject
    WebDriverProvider driverProvider;

    protected WebDriver driver() {
        return driverProvider.getDriver();
    }

    @BeforeMethod(groups = {"github", "ui"})
    public void getLoginCookies() {
        System.out.println("getLoginCookies");
        GitHubLoginPage loginPage = new GitHubLoginPage(driver());
        Set<Cookie> cookies = loginPage.load()
                .fillEmail(AutConfigService.getProperty(cs.getAut(), "email"))
                .fillPassword(AutConfigService.getProperty(cs.getAut(), "password"))
                .submitLogin()
                .getLoginCookies();
        COOKIES_MAP.put(AUT.GITHUB, cookies);
        driverProvider.cleanupDriver();
    }

    @BeforeMethod(groups = {"github", "ui"})
    public void injectGitHubCookies() {
        System.out.println("injectGitHubCookies");
        driver().get(AutConfigService.getProperty(cs.getAut(), "base.url"));
        COOKIES_MAP.get(AUT.GITHUB).forEach(cookie -> driver().manage().addCookie(cookie));
        driver().navigate().refresh();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        System.out.println("tearDown");
        driverProvider.cleanupDriver();
    }
}
