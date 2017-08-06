package com.palvair.elasticsearch.application.scheduler;

import com.palvair.elasticsearch.application.IndexName;
import com.palvair.elasticsearch.application.IndexService;
import com.palvair.elasticsearch.application.TypeName;
import com.palvair.elasticsearch.application.UserIndexer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class Scheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);

    private final IndexService indexService;
    private final UserIndexer userIndexer;

    @Autowired
    public Scheduler(final IndexService indexService,
                     final UserIndexer userIndexer) {
        this.indexService = indexService;
        this.userIndexer = userIndexer;
    }

    @Scheduled(fixedDelay = 1000 * 60 * 60)
    public void index() {
        LOGGER.info("Start indexing {}", LocalDateTime.now());
        try {
            if (indexService.indexExists(IndexName.USER)) {
                indexService.deleteIndex(IndexName.USER);
            }
            indexService.createIndex(IndexName.USER, TypeName.USER);
            userIndexer.fillIndex();
        } catch (final Exception e) {
            LOGGER.error(e.getMessage());
        }
        LOGGER.info("Indexing terminated {}", LocalDateTime.now());
    }
}