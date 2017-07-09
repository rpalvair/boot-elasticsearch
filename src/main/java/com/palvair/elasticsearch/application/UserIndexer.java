package com.palvair.elasticsearch.application;

import com.palvair.elasticsearch.domain.User;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Service
public class UserIndexer {

    private final Client client;

    @Autowired
    public UserIndexer(final Client client) {
        this.client = client;
    }

    public void addUser(final User user) throws IOException, InterruptedException {
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

    }
}
