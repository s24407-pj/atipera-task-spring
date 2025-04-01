package org.demo.atiperataskspring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Branch {
    private String name;
    private String lastCommitSha;

    @JsonProperty("commit")
    private void unpackNested(Map<String, Object> commit) {
        this.lastCommitSha = (String) commit.get("sha");
    }

    public String getName() {
        return name;
    }

    public String getLastCommitSha() {
        return lastCommitSha;
    }
}