package com.example.airankings.demo.web.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/*
* 获取参数的类
* */
@Getter
@Setter
public class KeywordRequest {
    // getters and setters
    @JsonProperty("keyword")
    private String keyword;

    @JsonProperty("tagId")
    private String tagId;

    @JsonProperty("displayCount")
    private int displayCount;
}
