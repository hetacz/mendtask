package com.hetacz.mendtask.di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.hetacz.mendtask.driver.WebDriverProvider;
import com.hetacz.mendtask.service.AutConfigService;
import com.hetacz.mendtask.service.ConfigService;
import org.openqa.selenium.WebDriver;

public class SeleniumModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ConfigService.class).in(Singleton.class);
        bind(AutConfigService.class).in(Singleton.class);
    }

    @Provides
    public WebDriver provideWebDriver(WebDriverProvider provider) {
        return provider.getDriver();
    }
}
