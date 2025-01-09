package com.hetacz.mendtask.tests;

import com.google.inject.Inject;
import com.hetacz.mendtask.api.ApiHelper;
import com.hetacz.mendtask.constants.AUT;
import com.hetacz.mendtask.constants.BrowserType;
import com.hetacz.mendtask.di.ApiModule;
import com.hetacz.mendtask.di.SeleniumModule;
import com.hetacz.mendtask.driver.WebDriverProvider;
import com.hetacz.mendtask.pages.LoginActions;
import com.hetacz.mendtask.responses.CodeAndResponse;
import com.hetacz.mendtask.service.AutConfigService;
import com.hetacz.mendtask.service.ConfigService;
import com.hetacz.mendtask.steps.UiGithubSteps;
import com.hetacz.mendtask.utils.Utils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Guice;
import org.testng.xml.XmlTest;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Guice(modules = {ApiModule.class, SeleniumModule.class})
public abstract class BaseTest {

    private static final Map<AUT, Set<Cookie>> COOKIES_MAP = new EnumMap<>(AUT.class);
    @Inject
    protected ApiHelper apiHelper;
    @Inject
    WebDriverProvider driverProvider;
    @Inject
    ConfigService cs;

    @BeforeTest(alwaysRun = true)
    public void beforeTest(ITestContext context) {
        XmlTest xml = context.getCurrentXmlTest();
        String xmlAut = xml.getParameter("aut").toUpperCase();
        String xmlBrowser = xml.getParameter("browser").toUpperCase();
        String xmlHeadless = xml.getParameter("headless").toUpperCase();
        AUT aut = AUT.valueOf(Utils.isNullOrBlank(xmlAut)
                ? cs.getProperty("aut", AUT.AZURE.toString()).toUpperCase()
                : xmlAut.toUpperCase());
        BrowserType browserType = BrowserType.valueOf(Utils.isNullOrBlank(xmlBrowser) ? cs.getProperty(
                "browser",
                BrowserType.CHROME.toString()
        ).toUpperCase() : xmlBrowser.toUpperCase());
        boolean headless = Boolean.parseBoolean(Utils.isNullOrBlank(xmlHeadless) ? cs.getProperty("headless", "false")
                .toUpperCase() : xmlHeadless.toUpperCase());
        System.setProperty("aut", aut.toString());
        System.setProperty("browser", browserType.toString());
        System.setProperty("headless", String.valueOf(headless));
    }

    @BeforeTest(groups = {"api", "github"})
    public void cleanUpData() {
        CodeAndResponse<List<String>> response = apiHelper.getRepoList();
        Assertions.assertThat(response.code()).isEqualTo(200);
        List<String> reposToKeep = Utils.splitBySemicolon(AutConfigService.getProperty(cs.getAut(), "keep"));
        response.body()
                .stream()
                .filter(repo -> !reposToKeep.contains(repo))
                .forEach(repo -> Assertions.assertThat(apiHelper.deleteRepo(repo)).isEqualTo(204));
    }

    @BeforeTest(groups = {"ui", "github"})
    public void getLoginCookies() {
        LoginActions uiGithubSteps = new UiGithubSteps(driver());
        Set<Cookie> cookies = uiGithubSteps.openLogin().performLogin().extractCookies();
        COOKIES_MAP.put(AUT.GITHUB, cookies);
        driverProvider.cleanupDriver();
    }

    @BeforeMethod(groups = {"ui", "github"})
    public void injectGitHubCookies() {
        LoginActions uiGithubSteps = new UiGithubSteps(driver());
        uiGithubSteps.injectCookies(COOKIES_MAP.get(AUT.GITHUB));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        driverProvider.cleanupDriver();
    }

    protected WebDriver driver() {
        return driverProvider.getDriver();
    }
}
