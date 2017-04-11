package com.palvair.elasticsearch.presentation;


import java.util.Collections;
import java.util.List;

public class SearchResult<T> {

    private final List<T> list;


    public SearchResult() {
        //for jackson
        this.list = Collections.emptyList();
    }

    public SearchResult(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "list=" + list +
                '}';
    }
}
