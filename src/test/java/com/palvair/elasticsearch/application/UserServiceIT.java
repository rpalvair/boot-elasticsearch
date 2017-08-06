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
public class UserServiceIT {

    @Autowired
    private UserService userService;
    @Autowired
    private UserIndexer userIndexer;


    @Test
    public void should_find_user_by_name_in_index_and_retrieve_it() throws IOException, InterruptedException {
        userIndexer.indexUsers();
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
    public void should_find_user_by_prenom_in_index_and_retrieve_it() throws IOException, InterruptedException {
        userIndexer.indexUsers();
        final SearchResult<User> all = userService.getAll();
        assertThat(all)
                .isNotNull();
        assertThat(all.getList())
                .isNotEmpty()
                .hasSize(1);

        final String prenom = "Ruddy";
        final SearchResult<User> searchResult = userService.find(prenom);

        assertThat(searchResult).isNotNull();
        final List<User> users = searchResult.getList();
        assertThat(users).isNotEmpty()
                .extracting(User::getPrenom)
                .contains(prenom);

        assertThat(users).isNotEmpty()
                .extracting(User::getNom)
                .contains("Palvair");

    }

    @Test
    public void should_find_user_approximately_by_nom_in_index_and_retrieve_it() throws IOException, InterruptedException {
        userIndexer.indexUsers();

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

    @Test
    public void should_find_user_approximately_by_prenom_in_index_and_retrieve_it() throws IOException, InterruptedException {
        userIndexer.indexUsers();
        final SearchResult<User> all = userService.getAll();
        assertThat(all)
                .isNotNull();
        assertThat(all.getList())
                .isNotEmpty()
                .hasSize(1);

        final SearchResult<User> searchResult = userService.find("rud");

        assertThat(searchResult).isNotNull();
        final List<User> users = searchResult.getList();
        assertThat(users).isNotEmpty()
                .extracting(User::getNom)
                .contains("Palvair");

        assertThat(users).isNotEmpty()
                .extracting(User::getPrenom)
                .contains("Ruddy");
    }

}