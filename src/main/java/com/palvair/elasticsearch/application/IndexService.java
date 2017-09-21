package com.palvair.elasticsearch.application;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.AliasOrIndex;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.SortedMap;

@Service
public class IndexService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexService.class);
    private final Client client;

    @Autowired
    public IndexService(final Client client) {
        this.client = client;
    }

    public String getFirstIndexFromAlias(final String alias) {
        final SortedMap<String, AliasOrIndex> aliasAndIndexLookup = client.admin()
                .cluster()
                .prepareState()
                .execute()
                .actionGet()
                .getState()
                .getMetaData()
                .getAliasAndIndexLookup();
        if (aliasAndIndexLookup.containsKey(alias)) {
            return aliasAndIndexLookup.get(alias).getIndices().get(0).getIndex();
        }
        return null;
    }

    public boolean addContent(final String index, final String type, final XContentBuilder json) {
        client.prepareIndex(index, type)
                .setSource(json)
                .get();

        client.admin()
                .indices()
                .prepareRefresh()
                .get();

        return true;
    }

    public boolean createIndex(final String indexName, final String typeName) {
        try {
            final String mapping = getContent("mappings.json");
            final String setting = getContent("settings.json");
            final CreateIndexResponse createIndexResponse = client.admin()
                    .indices()
                    .prepareCreate(indexName)
                    .addMapping(typeName, mapping)
                    .setSettings(setting)
                    .execute()
                    .actionGet();
            LOGGER.debug("Index {} created", indexName);
            return true;
        } catch (final Exception exception) {
            LOGGER.error("Error while creating index", exception);
            return false;
        }
    }

    public boolean addAlias(final String indexName, final String alias) {
        return client.admin()
                .indices()
                .prepareAliases()
                .addAlias(indexName, alias)
                .execute()
                .actionGet().isAcknowledged();
    }

    public boolean removeAlias(final String index, final String alias) {
        return client.admin()
                .indices()
                .prepareAliases()
                .removeAlias(index, alias)
                .execute()
                .actionGet().isAcknowledged();
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

    public boolean deleteIndex(final String indexName) {
        try {
            client.admin()
                    .indices()
                    .prepareDelete(indexName)
                    .execute()
                    .actionGet();
            LOGGER.debug("Index {} deleted", indexName);
            return true;
        } catch (final Exception exception) {
            LOGGER.error("Error while deleting index", exception);
            return false;
        }


    }

}
