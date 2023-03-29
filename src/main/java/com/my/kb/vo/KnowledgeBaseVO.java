package com.my.kb.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KnowledgeBaseVO {

    private String title;
    //private String type;
    private String description;
    private String splunkQuery;
    private String tag;
    //private String reference;
    private String createOn;
    //private String application;
    private Long mostSearched;
    private String knowledgeId;

    // For Search String
    private String search;
    private Boolean filter;
    private Integer count;

    public String toString() {
        return "Knowledge Base Details :: " + title + ", " + description + ", " + splunkQuery + ", " + knowledgeId;
    }
}
