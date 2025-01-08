package com.hetacz.mendtask.steps;

import com.hetacz.mendtask.constants.AUT;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommonSteps {

    WebDriver driver;
    AUT aut;

    public CommonSteps(WebDriver driver, AUT aut) {
        this.driver = driver;
        this.aut = aut;
    }
}
