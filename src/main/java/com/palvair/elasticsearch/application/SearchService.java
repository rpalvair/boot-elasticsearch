package com.palvair.elasticsearch.application;

import com.palvair.elasticsearch.presentation.SearchResult;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    public SearchResult search(final String value) {
        return new SearchResult(null);
    }
}
