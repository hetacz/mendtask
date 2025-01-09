package com.hetacz.mendtask.tests.gitlab;

import com.hetacz.mendtask.constants.AUT;
import com.hetacz.mendtask.service.AutConfigService;
import com.hetacz.mendtask.tests.BaseTest;
import com.hetacz.mendtask.utils.Utils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

@Test(groups = {"ui", "gitlab"})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GitLabTest extends BaseTest {

    String baseUrl = AutConfigService.getProperty(AUT.GITLAB, "base.url");

    @Test
    public void test() throws InterruptedException {
        Thread.sleep(Utils.RANDOM.nextLong(5_000));
        driver().get(baseUrl);
        Thread.sleep(Utils.RANDOM.nextLong(5_000));
        Assertions.assertThat(driver().getCurrentUrl()).contains("about");
    }

    @Test
    public void test2() throws InterruptedException {
        Thread.sleep(Utils.RANDOM.nextLong(5_000));
        driver().get(baseUrl);
        Thread.sleep(Utils.RANDOM.nextLong(5_000));
        Assertions.assertThat(driver().getCurrentUrl()).contains("gitlab");
    }

    @Test
    public void test3() throws InterruptedException {
        Thread.sleep(Utils.RANDOM.nextLong(5_000));
        driver().get(baseUrl);
        Thread.sleep(Utils.RANDOM.nextLong(5_000));
        Assertions.assertThat(driver().getCurrentUrl()).contains("com");
    }
}
