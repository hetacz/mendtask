package com.hetacz.mendtask.driver;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hetacz.mendtask.constants.BrowserType;
import com.hetacz.mendtask.service.ConfigService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.WebDriver;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebDriverProvider {

    ConfigService cs;
    ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public WebDriver getDriver() {
        if (driver.get() == null) {
            driver.set(createWebDriver());
        }
        return driver.get();
    }

    public void cleanupDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }

    private WebDriver createWebDriver() {
        BrowserType browserType = BrowserType.valueOf(cs.getProperty("browser", BrowserType.CHROME.toString()));
        boolean headless = Boolean.parseBoolean(cs.getProperty("headless", "false"));
        return headless
                ? WebDriverFactory.getManager(browserType).createDriverHeadless()
                : WebDriverFactory.getManager(browserType).createDriver();
    }
}
