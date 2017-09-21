package com.palvair.elasticsearch.application;

import com.palvair.elasticsearch.domain.User;
import com.palvair.elasticsearch.domain.UserRepository;
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
    private final IndexService indexService;
    private final UserRepository userRepository;

    @Autowired
    public UserIndexer(final IndexService indexService,
                       final UserRepository jdbcUserRepository) {
        this.indexService = indexService;
        this.userRepository = jdbcUserRepository;
    }


    //TODO : retourner index et alias
    public String refreshIndex() {
        final String alias = "user";
        final String oldIndex = indexService.getFirstIndexFromAlias(alias);
        final String newIndex = computeNewIndexName(oldIndex, alias);
        indexService.createIndex(newIndex, "user");
        userRepository.findAll()
                .forEach(user -> addUser(user, newIndex));
        if (oldIndex != null) {
            indexService.removeAlias(oldIndex, alias);
            indexService.deleteIndex(oldIndex);
        }
        indexService.addAlias(newIndex, alias);
        return newIndex;
    }


    private String computeNewIndexName(final String oldIndex, final String index) {
        if (oldIndex == null) {
            return index + "_1";
        }
        final String indexWithoutVersion = oldIndex.substring(0, oldIndex.length() - 2);
        if (oldIndex.endsWith("_1")) {
            return indexWithoutVersion + "_2";
        }
        return indexWithoutVersion + "_1";
    }

    private void addUser(final User user, final String indexName) {
        try {
            final XContentBuilder json = jsonBuilder()
                    .startObject()
                    .field("prenom", user.getPrenom())
                    .field("nom", user.getNom())
                    .endObject();
            indexService.addContent(indexName, "user", json);
        } catch (final IOException exception) {
            LOGGER.error("Error while adding user {}", user, exception);
        }

    }
}
