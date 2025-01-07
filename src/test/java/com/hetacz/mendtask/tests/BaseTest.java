package com.hetacz.mendtask.tests;

import com.google.inject.Inject;
import com.hetacz.mendtask.constants.AUT;
import com.hetacz.mendtask.di.ApiModule;
import com.hetacz.mendtask.di.SeleniumModule;
import com.hetacz.mendtask.driver.WebDriverProvider;
import com.hetacz.mendtask.pages.github.GitHubLoginPage;
import com.hetacz.mendtask.service.ConfigService;
import com.hetacz.mendtask.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Guice(modules = {ApiModule.class, SeleniumModule.class})
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class BaseTest {

    private static final Map<AUT, Set<Cookie>> COOKIES_MAP = new EnumMap<>(AUT.class);
    @Inject
    protected ConfigService configService;
    @Inject
    private WebDriverProvider driverProvider;

    protected WebDriver driver() {
        return driverProvider.getDriver();
    }

    @Parameters({"browser", "headless", "aut"})
    @BeforeTest(alwaysRun = true)
    public void beforeTest(@Optional String xmlBrowser, @Optional boolean xmlHeadless, @Optional String xmlAut) {
        if (!Utils.isNullOrBlank(xmlBrowser)) {
            System.setProperty("browser", xmlBrowser.toUpperCase());
        }
        if (!Utils.isNullOrBlank(xmlAut)) {
            System.setProperty("aut", xmlAut.toUpperCase());
        }
        System.setProperty("headless", String.valueOf(xmlHeadless));
    }

    @BeforeTest(dependsOnMethods = "beforeTest")
    public void getLoginCookies() {
        GitHubLoginPage loginPage = new GitHubLoginPage(driver());
        Set<Cookie> cookies = loginPage.load()
                .typeEmail(configService.getPlatformProperty("username"))
                .typePassword(configService.getPlatformProperty("password"))
                .submitLogin()
                .getLoginCookies();

        COOKIES_MAP.put(AUT.GITHUB, cookies);
        driverProvider.cleanupDriver();
    }

    @BeforeMethod(groups = "github-ui")
    public void injectGitHubCookies() {
        driver().get(configService.getPlatformProperty("base.url"));
        COOKIES_MAP.get(AUT.GITHUB).forEach(cookie -> driver().manage().addCookie(cookie));
        driver().navigate().refresh();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        driverProvider.cleanupDriver();
    }
}
