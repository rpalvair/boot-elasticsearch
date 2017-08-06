package com.palvair.elasticsearch.application;

import com.palvair.elasticsearch.domain.User;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Service
public class UserIndexer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserIndexer.class);
    private final Client client;
    private final JdbcTemplate jdbcTemplate;
    private final IndexService indexService;

    @Autowired
    public UserIndexer(final Client client,
                       final JdbcTemplate jdbcTemplate,
                       final IndexService indexService) {
        this.client = client;
        this.jdbcTemplate = jdbcTemplate;
        this.indexService = indexService;
    }


    public void indexUsers() {
        try {
            if (indexService.indexExists(IndexName.USER)) {
                indexService.deleteIndex(IndexName.USER);
            }
            indexService.createIndex(IndexName.USER, TypeName.USER);
        } catch (final IOException | InterruptedException ex) {
            LOGGER.error(ex.getMessage());
        }
        final String sql = "SELECT users.nom," +
                " users.prenom " +
                " FROM users";

        jdbcTemplate.query(sql, this::mapUser)
                .forEach(this::addUser);
    }

    private User mapUser(final ResultSet resultSet, final int i) throws SQLException {
        return new User(resultSet.getString("nom"),
                resultSet.getString("prenom"));
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

        } catch (final IOException ex) {
            LOGGER.error(ex.getMessage());
        }

    }
}
