package com.palvair.elasticsearch.application;

import com.palvair.elasticsearch.domain.User;
import com.palvair.elasticsearch.infrastructure.Application;
import com.palvair.elasticsearch.presentation.SearchResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class}, webEnvironment = DEFINED_PORT)
@ActiveProfiles("test")
public class UserSearchServiceIT {

    @Autowired
    private IndexService indexService;

    @Autowired
    private UserSearchService userSearchService;

    @Before
    public void before() throws IOException, InterruptedException {
        indexService.createIndex();
    }

    @Test
    public void should_user_in_index_and_retrieve_it() throws IOException, InterruptedException {
        final String nom = "palvair";
        indexService.addUser(new User(nom, "ruddy"));
        final SearchResult<User> searchResult = userSearchService.searchExactly(nom);
        assertThat(searchResult).isNotNull();
        final List<User> users = searchResult.getList();
        assertThat(users).isNotEmpty()
                .extracting(User::getNom)
                .contains(nom);
    }


}