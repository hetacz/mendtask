package com.hetacz.mendtask.tests.gitlab;

import com.hetacz.mendtask.constants.AUT;
import com.hetacz.mendtask.tests.BaseTest;
import org.testng.annotations.Test;

@Test(groups = "gitlab")
public class GitLabTest extends BaseTest {

    @Test
    public void test() throws InterruptedException {
        Thread.sleep(15000);
        driver().get(autConfig.getProperty(AUT.GITLAB, "base.url"));
    }

    @Test
    public void test2() throws InterruptedException {
        Thread.sleep(10000);
        driver().get(autConfig.getProperty(AUT.GITLAB, "base.url"));
    }

    @Test
    public void test3() throws InterruptedException {
        Thread.sleep(5000);
        driver().get(autConfig.getProperty(AUT.GITLAB, "base.url"));
    }
}
