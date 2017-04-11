package com.palvair.elasticsearch.application;

import com.palvair.elasticsearch.domain.User;
import com.palvair.elasticsearch.infrastructure.Application;
import com.palvair.elasticsearch.presentation.SearchResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class}, webEnvironment = DEFINED_PORT)
@ActiveProfiles("test")
public class IndexServiceIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexServiceIT.class);
    @Autowired
    private IndexService indexService;

    @Autowired
    private SearchService searchService;


    @Before
    public void before() throws IOException, InterruptedException {
        indexService.createIndex();
    }

    @Test
    public void shoud() throws IOException, InterruptedException {
        final SearchResult<User> searchResult = searchService.searchExactly("palvair");
        LOGGER.debug("searchResult = {}", searchResult);
    }


}