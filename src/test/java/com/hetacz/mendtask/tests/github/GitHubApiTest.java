package com.hetacz.mendtask.tests.github;

import com.hetacz.mendtask.responses.BillingPackages;
import com.hetacz.mendtask.responses.CodeAndResponse;
import com.hetacz.mendtask.tests.BaseTest;
import com.hetacz.mendtask.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

@Slf4j
@Test(groups = {"api", "github"})
public class GitHubApiTest extends BaseTest {

    @Test
    public void apiAuth() {
        int code = apiHelper.authenticate();
        Assertions.assertThat(code).isEqualTo(200);
    }

    @Test
    public void apiCheckPackageBilling() {
        CodeAndResponse<BillingPackages> response = apiHelper.getBillingInfo();
        Assertions.assertThat(response.code()).isEqualTo(200);
        Utils.validate(response.body());
    }
}
