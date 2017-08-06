package com.palvair.elasticsearch.application;

import com.palvair.elasticsearch.domain.User;
import com.palvair.elasticsearch.infrastructure.Application;
import com.palvair.elasticsearch.presentation.SearchResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class}, webEnvironment = NONE)
@ActiveProfiles("test")
@Transactional
public class UserServiceIT {

    @Autowired
    private IndexService indexService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserIndexer userIndexer;


    @Test
    public void should_find_user_in_index_and_retrieve_it() throws IOException, InterruptedException {
        initIndex();
        userIndexer.fillIndex();

        final SearchResult<User> all = userService.getAll();
        assertThat(all)
                .isNotNull();
        assertThat(all.getList())
                .isNotEmpty()
                .hasSize(1);

        final String nom = "Palvair";
        final SearchResult<User> searchResult = userService.find(nom);

        assertThat(searchResult).isNotNull();
        final List<User> users = searchResult.getList();
        assertThat(users).isNotEmpty()
                .extracting(User::getNom)
                .contains(nom);
    }

    @Test
    public void should_find_user_approximately_by_nom_in_index_and_retrieve_it() throws IOException, InterruptedException {
        initIndex();
        userIndexer.fillIndex();

        final SearchResult<User> all = userService.getAll();
        assertThat(all)
                .isNotNull();
        assertThat(all.getList())
                .isNotEmpty()
                .hasSize(1);

        final SearchResult<User> searchResult = userService.find("pal");

        assertThat(searchResult).isNotNull();
        final List<User> users = searchResult.getList();
        assertThat(users).isNotEmpty()
                .extracting(User::getNom)
                .contains("Palvair");
    }

    private void initIndex() throws InterruptedException, IOException {
        if (indexService.indexExists(IndexName.USER)) {
            indexService.deleteIndex(IndexName.USER);
        }
        indexService.createIndex(IndexName.USER, TypeName.USER);
    }

}