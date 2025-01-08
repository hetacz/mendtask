package com.hetacz.mendtask.api;

import com.hetacz.mendtask.responses.CodeAndResponse;

import java.util.List;

public interface ApiActions {

    int authenticate();

    CodeAndResponse<List<String>> getRepoList();

    CodeAndResponse<List<String>> createRepo(String repoName);

    CodeAndResponse<List<String>> deleteRepo(String repoName);

    CodeAndResponse<?> getBillingInfo();
}
