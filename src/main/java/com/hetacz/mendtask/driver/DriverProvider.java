package com.hetacz.mendtask.driver;

import org.openqa.selenium.WebDriver;

public interface DriverProvider {

    WebDriver createDriver();

    WebDriver createDriverHeadless();
}
