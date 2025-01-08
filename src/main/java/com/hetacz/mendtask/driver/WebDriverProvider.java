package com.hetacz.mendtask.driver;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hetacz.mendtask.constants.BrowserType;
import com.hetacz.mendtask.service.ConfigService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebDriverProvider {

    ConfigService configService;
    ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public WebDriver getDriver() {
        if (driver.get() == null) {
            driver.set(createWebDriver());
            log.info("Inside create driver if {} {}", driver.get().hashCode(), Thread.currentThread().getName());
        }
        log.info("Getting driver in thread: {}", Thread.currentThread().getName());
        return driver.get();
    }

    public void cleanupDriver() {
        try {
            if (driver.get() != null) {
                driver.get().quit();
                driver.remove();
            } else {
                log.warn("Driver is null");
            }
        } catch (Exception e) {
            log.warn("Driver in illegal state.", e);
        }
    }

    private WebDriver createWebDriver() {
        BrowserType browserType = BrowserType.valueOf(configService.getProperty("browser", BrowserType.CHROME.toString()));
        boolean headless = Boolean.parseBoolean(configService.getProperty("headless", "false"));
        return headless
                ? WebDriverFactory.getManager(browserType).createDriverHeadless()
                : WebDriverFactory.getManager(browserType).createDriver();
    }
}
