package com.palvair.elasticsearch.application;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Service
public class IndexService {

    private static final String INDEX = "user";
    private static final String TYPE = "user";
    private final Client client;

    @Autowired
    public IndexService(final Client client) {
        this.client = client;
    }

    public void createIndex() throws IOException, InterruptedException {
        client.admin().indices().prepareCreate(INDEX)
                .addMapping(TYPE, new ClassPathResource("mappings.json").getPath())
                .setSettings(new ClassPathResource("settings.json").getPath());

        final XContentBuilder json = jsonBuilder()
                .startObject()
                .field("prenom", "ruddy")
                .field("nom", "palvair")
                .endObject();

        client.prepareIndex("user", "user")
                .setSource(json)
                .get();

        Thread.sleep(5000);
    }
}