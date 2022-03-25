package com.gitapp.model.branches;

import lombok.Data;

@Data
public class Branch {
    private String name;
    private Commit commit;
}
