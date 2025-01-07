package com.hetacz.mendtask.driver;

import com.hetacz.mendtask.constants.BrowserType;
import com.hetacz.mendtask.driver.drivers.ChromeDriverProvider;
import com.hetacz.mendtask.driver.drivers.EdgeDriverProvider;
import com.hetacz.mendtask.driver.drivers.FirefoxDriverProvider;
import lombok.experimental.UtilityClass;

@UtilityClass
public class WebDriverFactory {

    public DriverProvider getManager(BrowserType browserType) {
        return switch (browserType) {
            case CHROME -> new ChromeDriverProvider();
            case FIREFOX -> new FirefoxDriverProvider();
            case EDGE -> new EdgeDriverProvider();
        };
    }
}
