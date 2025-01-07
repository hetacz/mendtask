package com.hetacz.mendtask.pages;

import com.google.inject.Inject;
import com.hetacz.mendtask.service.ConfigService;
import com.hetacz.mendtask.utils.InjectorHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
public abstract class BasePage {

    protected final WebDriverWait wait;
    protected final WebDriver driver;
    protected final ConfigService configService;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.configService = InjectorHolder.getConfigService();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(configService.getProperty("defaultWait"))));
        log.info("Driver hash in test: {}", driver.hashCode());
    }

    protected void load(String url) {
        driver.get(configService.getPlatformProperty("base.url") + url);
    }

    protected WebElement getClickableElement(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected WebElement getVisibleElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
