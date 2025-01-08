package com.hetacz.mendtask.tests.gitlab;

import com.hetacz.mendtask.constants.AUT;
import com.hetacz.mendtask.service.AutConfigService;
import com.hetacz.mendtask.tests.BaseTest;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.Random;

public class GitLabTest extends BaseTest {

    @Test
    public void test() throws InterruptedException {
        Thread.sleep(new Random().nextInt(5_000));
        driver().get(AutConfigService.getProperty(AUT.GITLAB, "base.url"));
        Thread.sleep(new Random().nextInt(5_000));
        Assertions.assertThat(driver().getCurrentUrl()).contains("about");
    }

    @Test
    public void test2() throws InterruptedException {
        Thread.sleep(new Random().nextInt(5_000));
        driver().get(AutConfigService.getProperty(AUT.GITLAB, "base.url"));
        Thread.sleep(new Random().nextInt(5_000));
        Assertions.assertThat(driver().getCurrentUrl()).contains("gitlab");
    }

    @Test
    public void test3() throws InterruptedException {
        Thread.sleep(new Random().nextInt(5_000));
        driver().get(AutConfigService.getProperty(AUT.GITLAB, "base.url"));
        Thread.sleep(new Random().nextInt(5_000));
        Assertions.assertThat(driver().getCurrentUrl()).contains("com");
    }
}
