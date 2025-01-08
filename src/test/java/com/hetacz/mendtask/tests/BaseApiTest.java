package com.hetacz.mendtask.tests;

import com.google.inject.Inject;
import com.hetacz.mendtask.api.GitHubApi;
import com.hetacz.mendtask.constants.AUT;
import com.hetacz.mendtask.constants.BrowserType;
import com.hetacz.mendtask.di.ApiModule;
import com.hetacz.mendtask.responses.CodeAndResponse;
import com.hetacz.mendtask.service.AutConfigService;
import com.hetacz.mendtask.service.ConfigService;
import com.hetacz.mendtask.utils.Utils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.assertj.core.api.Assertions;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Guice;
import org.testng.xml.XmlTest;

import java.util.List;

@Slf4j
@Guice(modules = ApiModule.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseApiTest {

    @Inject
    private ConfigService cs;
    @Inject
    GitHubApi gitHubApi;

    @BeforeTest(alwaysRun = true)
    public void beforeTest(ITestContext context) {
        System.out.println("Before test");
        XmlTest xml = context.getCurrentXmlTest();
        String xmlAut = xml.getParameter("aut").toUpperCase();
        String xmlBrowser = xml.getParameter("browser").toUpperCase();
        String xmlHeadless = xml.getParameter("headless").toUpperCase();
        AUT aut = AUT.valueOf(Utils.isNullOrBlank(xmlAut)
                ? cs.getProperty("aut", AUT.AZURE.toString()).toUpperCase()
                : xmlAut.toUpperCase());
        BrowserType browserType = BrowserType.valueOf(Utils.isNullOrBlank(xmlBrowser)
                ? cs.getProperty("browser", BrowserType.CHROME.toString()).toUpperCase()
                : xmlBrowser.toUpperCase());
        boolean headless = Boolean.parseBoolean(Utils.isNullOrBlank(xmlHeadless) ? cs.getProperty("headless", "false")
                .toUpperCase() : xmlHeadless.toUpperCase());
        System.out.println("AUT: " + aut);
        System.out.println("Browser: " + browserType);
        System.out.println("Headless: " + headless);
        System.setProperty("aut", aut.toString());
        System.setProperty("browser", browserType.toString());
        System.setProperty("headless", String.valueOf(headless));
    }

    @BeforeTest(groups = "github")
    public void cleanUpData() {
        System.out.println("cleanUpData");
        Request getRepoList = gitHubApi.getRepoList();
        CodeAndResponse<List<String>> response = gitHubApi.apiHelper.sendRequestAndParseResponse(getRepoList, Utils.REPO_NAME_EXTRACTOR);
        Assertions.assertThat(response.code()).isEqualTo(200);
        List<String> reposToKeep = Utils.splitBySemicolon(AutConfigService.getProperty(cs.getAut(), "keep"));
        response.body().stream()
                .filter(repo -> !reposToKeep.contains(repo))
                .forEach(repo -> {
                    Request deleteRepo = gitHubApi.deleteRepo(repo);
                    int deleteRepoCode = gitHubApi.apiHelper.sendRequest(deleteRepo);
                    Assertions.assertThat(deleteRepoCode).isEqualTo(204);
                });
    }
}
