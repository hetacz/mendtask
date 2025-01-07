package com.hetacz.mendtask.driver.drivers;

import com.hetacz.mendtask.driver.DriverProvider;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxDriverProvider implements DriverProvider {

    @Override
    public WebDriver createDriver() {
        WebDriverManager.firefoxdriver().cachePath("drivers").setup();
        WebDriver driver = new FirefoxDriver();
        driver.manage().window().maximize();
        return driver;
    }

    @Override
    public WebDriver createDriverHeadless() {
        WebDriverManager.firefoxdriver().cachePath("drivers").setup();
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--window-size=1920,1080", "-headless");
        return new FirefoxDriver(options);
    }
}
