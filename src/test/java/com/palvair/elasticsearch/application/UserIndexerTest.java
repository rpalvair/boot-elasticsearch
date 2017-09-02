package com.palvair.elasticsearch.application;

import org.elasticsearch.client.Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class UserIndexerTest {


    @Mock
    private Client client;
    @InjectMocks
    private UserIndexer userIndexer;

    @Test
    public void should_refresh_index_and_switch_aliases() {
        final boolean refreshIndex = userIndexer.refreshIndex();

        assertThat(refreshIndex).isTrue();
    }
}