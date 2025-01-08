package com.hetacz.mendtask.tests;

import com.google.inject.Inject;
import com.hetacz.mendtask.api.GitHubApi;
import com.hetacz.mendtask.constants.AUT;
import com.hetacz.mendtask.di.ApiModule;
import com.hetacz.mendtask.di.SeleniumModule;
import com.hetacz.mendtask.driver.WebDriverProvider;
import com.hetacz.mendtask.pages.github.GitHubLoginPage;
import com.hetacz.mendtask.responses.CodeAndResponse;
import com.hetacz.mendtask.service.AutConfigService;
import com.hetacz.mendtask.service.ConfigService;
import com.hetacz.mendtask.utils.Utils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Guice(modules = {ApiModule.class, SeleniumModule.class})
@RequiredArgsConstructor(onConstructor_ = @Inject)
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseTest {

    private static final Map<AUT, Set<Cookie>> COOKIES_MAP = new EnumMap<>(AUT.class);
    @Inject
    protected ConfigService cs;
    @Inject
    protected AutConfigService autConfig;
    @Inject
    WebDriverProvider driverProvider;
    @Inject
    GitHubApi gitHubApi;

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
        autConfig.loadProperties(cs.getAut());
    }

    @BeforeTest(dependsOnMethods = "beforeTest", groups = "github-ui")
    public void getLoginCookies() {
        GitHubLoginPage loginPage = new GitHubLoginPage(driver());
        Set<Cookie> cookies = loginPage.load()
                .fillEmail(autConfig.getProperty(cs.getAut(), "email"))
                .fillPassword(autConfig.getProperty(cs.getAut(), "password"))
                .submitLogin()
                .getLoginCookies();
        COOKIES_MAP.put(AUT.GITHUB, cookies);
        driverProvider.cleanupDriver();
    }

    @BeforeTest(groups = "github")
    public void cleanUpData() {
        Request getRepoList = gitHubApi.getRepoList();
        CodeAndResponse<List<String>> response = gitHubApi.apiHelper.sendRequestAndParseResponse(getRepoList, Utils.REPO_NAME_EXTRACTOR);
        Assertions.assertThat(response.code()).isEqualTo(200);
        List<String> reposToKeep = Utils.splitBySemicolon(autConfig.getProperty(cs.getAut(), "keep"));
        response.body().stream()
                .filter(repo -> !reposToKeep.contains(repo))
                .forEach(repo -> {
                    Request deleteRepo = gitHubApi.deleteRepo(repo);
                    int deleteRepoCode = gitHubApi.apiHelper.sendRequest(deleteRepo);
                    Assertions.assertThat(deleteRepoCode).isEqualTo(204);
                });
    }

    @BeforeMethod(groups = "github-ui")
    public void injectGitHubCookies() {
        driver().get(autConfig.getProperty(cs.getAut(), "base.url"));
        COOKIES_MAP.get(AUT.GITHUB).forEach(cookie -> driver().manage().addCookie(cookie));
        driver().navigate().refresh();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        driverProvider.cleanupDriver();
    }
}
