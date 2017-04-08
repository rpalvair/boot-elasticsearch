package com.palvair.elasticsearch.presentation;


public class SearchResult {

    private String content;


    public SearchResult(final String content) {
        this.content = content;
    }

    public SearchResult() {
        //for jackson
    }

    public String getContent() {
        return content;
    }

}
