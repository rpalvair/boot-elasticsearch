package com.palvair.elasticsearch.application.scheduler;

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

    private final UserIndexer userIndexer;

    @Autowired
    public Scheduler(final UserIndexer userIndexer) {
        this.userIndexer = userIndexer;
    }

    @Scheduled(fixedDelay = 1000 * 60 * 60)
    public void index() {
        LOGGER.info("Start indexing {}", LocalDateTime.now());
        try {
            userIndexer.indexUsers();
        } catch (final Exception e) {
            LOGGER.error(e.getMessage());
        }
        LOGGER.info("Indexing terminated {}", LocalDateTime.now());
    }
}