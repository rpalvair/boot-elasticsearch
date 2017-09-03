package com.palvair.elasticsearch.application;

import com.palvair.elasticsearch.domain.UserRepository;
import org.elasticsearch.client.Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserIndexerTest {


    @Mock
    private Client client;
    @Mock
    private IndexService indexService;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserIndexer userIndexer;

    @Test
    public void should_refresh_index_and_switch_aliases() {
        when(indexService.getFirstIndexFromAlias("user")).thenReturn("user_1");

        final String newIndex = userIndexer.refreshIndex();

        assertThat(newIndex).isNotNull()
                .isEqualTo("user_2");
    }
}