package com.palvair.elasticsearch.application;

import com.palvair.elasticsearch.domain.User;
import com.palvair.elasticsearch.domain.UserRepository;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Service
public class UserIndexer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserIndexer.class);
    private final Client client;
    private final IndexService indexService;
    private final UserRepository userRepository;

    @Autowired
    public UserIndexer(final Client client,
                       final IndexService indexService,
                       final UserRepository jdbcUserRepository) {
        this.client = client;
        this.indexService = indexService;
        this.userRepository = jdbcUserRepository;
    }


    public void indexUsers() {
        if (indexService.indexExists(IndexName.USER)) {
            indexService.deleteIndex(IndexName.USER);
        }
        indexService.createIndex(IndexName.USER, TypeName.USER);
        userRepository.findAll()
                .forEach(this::addUser);
    }

    private void addUser(final User user) {
        try {
            final XContentBuilder json = jsonBuilder()
                    .startObject()
                    .field("prenom", user.getPrenom())
                    .field("nom", user.getNom())
                    .endObject();

            client.prepareIndex(IndexName.USER.getName(), TypeName.USER.getName())
                    .setSource(json)
                    .get();

            client.admin()
                    .indices()
                    .prepareRefresh()
                    .get();

        } catch (final IOException exception) {
            LOGGER.error("Error while adding user {}", user, exception);
        }

    }
}
