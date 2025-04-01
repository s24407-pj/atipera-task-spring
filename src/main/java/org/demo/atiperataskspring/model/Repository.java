package org.demo.atiperataskspring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Repository {

    private String name;
    private boolean fork;
    private String ownerLogin;

    @JsonProperty("owner")
    private void unpackOwner(Map<String, Object> owner) {
        this.ownerLogin = (String) owner.get("login");
    }

    public String getName() {
        return name;
    }

    public boolean isFork() {
        return fork;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }
}
