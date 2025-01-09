package com.hetacz.mendtask.di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.hetacz.mendtask.driver.WebDriverProvider;
import org.openqa.selenium.WebDriver;

public class SeleniumModule extends AbstractModule {

    @Provides
    public static WebDriver provideWebDriver(WebDriverProvider provider) {
        return provider.getDriver();
    }
}
