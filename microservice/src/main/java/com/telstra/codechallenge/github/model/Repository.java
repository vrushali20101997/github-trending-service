package com.telstra.codechallenge.github.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Repository {

    @JsonProperty("html_url")
    private String htmlUrl;

    @JsonProperty("watchers_count")
    private Integer watchersCount;

    private String language;
    private String description;
    private String name;
}