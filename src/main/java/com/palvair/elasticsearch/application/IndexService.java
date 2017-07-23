package com.palvair.elasticsearch.application;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class IndexService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexService.class);
    private final Client client;

    @Autowired
    public IndexService(final Client client) {
        this.client = client;
    }

    public void createIndex(final IndexName indexName, final TypeName typeName) throws IOException, InterruptedException {
        final URI mappingsUri = new ClassPathResource("mappings.json").getURI();
        final String mapping = new String(Files.readAllBytes(Paths.get(mappingsUri)));
        final URI settingsUri = new ClassPathResource("settings.json").getURI();
        final String setting = new String(Files.readAllBytes(Paths.get(settingsUri)));
        final CreateIndexResponse createIndexResponse = client.admin()
                .indices()
                .prepareCreate(indexName.getName())
                .addMapping(typeName.getName(), mapping)
                .setSettings(setting)
                .get();

        LOGGER.debug("response = {}", createIndexResponse);
        LOGGER.debug("Indice {} created", indexName.getName());
    }

    public void deleteIndex(final IndexName indexName) throws InterruptedException {
        final DeleteIndexResponse deleteIndexResponse = client.admin()
                .indices()
                .prepareDelete(indexName.getName())
                .get();

        LOGGER.debug("response {}", deleteIndexResponse.getContext());

        LOGGER.debug("Indice {} deleted", indexName.getName());
    }

    public boolean indexExists(final IndexName indexName) {
        return client.admin()
                .indices()
                .exists(new IndicesExistsRequest(indexName.getName()))
                .actionGet()
                .isExists();
    }
}
