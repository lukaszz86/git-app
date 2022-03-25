package com.gitapp.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BranchSummary {
    private String branchName;
    private String commitSha;
}
