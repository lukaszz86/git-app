package com.gitapp.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GitRepositorySummary {
    private String repositoryName;
    private String ownerName;
    private List<BranchSummary> branches;
}
