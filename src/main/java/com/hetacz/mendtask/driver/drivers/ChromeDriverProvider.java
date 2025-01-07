package com.hetacz.mendtask.driver.drivers;

import com.hetacz.mendtask.driver.DriverProvider;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Collections;

public class ChromeDriverProvider implements DriverProvider {

    @Override
    public WebDriver createDriver() {
        WebDriverManager.chromedriver().cachePath("drivers").setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        System.setProperty("webdriver.chrome.silentLogging", "true");
        System.setProperty("webdriver.chrome.silentOutput", "true");
        return driver;
    }

    @Override
    public WebDriver createDriverHeadless() {
        WebDriverManager.chromedriver().cachePath("drivers").setup();
        ChromeOptions options = new ChromeOptions();
        System.setProperty("webdriver.chrome.silentLogging", "true");
        System.setProperty("webdriver.chrome.silentOutput", "true");
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-logging"));
        options.addArguments(
                "--disable-gpu",
                "--window-size=1920,1080",
                "--ignore-certificate-errors",
                "--disable-extensions",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--log-level=3",
                "--headless=new"
        );
        return new ChromeDriver(options);
    }
}
