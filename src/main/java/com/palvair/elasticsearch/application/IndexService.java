package com.palvair.elasticsearch.application;

import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class IndexService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexService.class);
    private final Client client;

    @Autowired
    public IndexService(final Client client) {
        this.client = client;
    }

    public void createIndex(final IndexName indexName, final TypeName typeName) {
        try {
            final String mapping = getContent("mappings.json");
            final String setting = getContent("settings.json");
            client.admin()
                    .indices()
                    .prepareCreate(indexName.getName())
                    .addMapping(typeName.getName(), mapping)
                    .setSettings(setting)
                    .execute();
            LOGGER.debug("Index {} created", indexName.getName());
        } catch (final Exception exception) {
            LOGGER.error("Error while creating index", exception);
        }
    }

    private String getContent(final String fileName) {
        final ClassPathResource classPathResource = new ClassPathResource(fileName);
        try {
            byte[] byteArray = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
            return new String(byteArray, StandardCharsets.UTF_8);
        } catch (final IOException exception) {
            LOGGER.warn("Error while reading file content {}", fileName, exception);
            return null;
        }
    }

    public void deleteIndex(final IndexName indexName) {
        try {
            client.admin()
                    .indices()
                    .prepareDelete(indexName.getName())
                    .execute();
            LOGGER.debug("Index {} deleted", indexName.getName());
        } catch (final Exception exception) {
            LOGGER.error("Error while deleting index", exception);
        }

    }

    public boolean indexExists(final IndexName indexName) {
        return client.admin()
                .indices()
                .exists(new IndicesExistsRequest(indexName.getName()))
                .actionGet()
                .isExists();
    }
}
