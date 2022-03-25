package com.gitapp.model.listing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GitRepository {

    @JsonProperty("name")
    private String repositoryName;
    private Owner owner;
    private boolean fork;
}
