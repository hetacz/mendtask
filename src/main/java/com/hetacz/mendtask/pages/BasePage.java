package com.hetacz.mendtask.pages;

import com.google.inject.Inject;
import com.hetacz.mendtask.constants.AUT;
import com.hetacz.mendtask.service.AutConfigService;
import com.hetacz.mendtask.service.ConfigService;
import com.hetacz.mendtask.utils.InjectorHolder;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

@Slf4j
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class BasePage {

    WebDriverWait wait;
    WebDriver driver;
    ConfigService cs;
    AutConfigService autConfig;

    @Inject
    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.cs = InjectorHolder.getConfigService();
        this.autConfig = InjectorHolder.getAutConfigService();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(cs.getProperty("wait"))));
    }

    protected void load(AUT aut, String url) {
        driver.get(autConfig.getProperty(aut, "base.url") + url);
    }

    protected WebElement getClickableElement(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected WebElement getVisibleElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected List<WebElement> getVisibleElements(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }
}
