package com.hetacz.mendtask.tests.gitab;

import com.hetacz.mendtask.tests.BaseTest;
import org.testng.annotations.Test;

@Test(groups = "gitlab")
public class GitLabTest extends BaseTest {

    @Test
    public void test() throws InterruptedException {
        Thread.sleep(5000);
        driver().get(configService.getPlatformProperty("base.url"));
    }

    @Test
    public void test2() throws InterruptedException {
        Thread.sleep(5000);
        driver().get(configService.getPlatformProperty("base.url"));
    }

    @Test
    public void test3() throws InterruptedException {
        Thread.sleep(5000);
        driver().get(configService.getPlatformProperty("base.url"));
    }
}
