package com.katerynamykh.taskprofitsoft.backend.config;

import org.testcontainers.containers.PostgreSQLContainer;

public class BasePostgreContainerConfig extends PostgreSQLContainer<BasePostgreContainerConfig> {
    private static final String DB_IMAGE = "postgres";
    private static BasePostgreContainerConfig postgreContainer;

    public BasePostgreContainerConfig() {
        super(DB_IMAGE);
    }

    public static synchronized BasePostgreContainerConfig getInstance() {
        if (postgreContainer == null) {
            postgreContainer = new BasePostgreContainerConfig();
        }
        return postgreContainer;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("TEST_DB_URL", postgreContainer.getJdbcUrl());
        System.setProperty("TEST_DB_USERNAME", postgreContainer.getUsername());
        System.setProperty("TEST_DB_PASSWORD", postgreContainer.getPassword());
    }

    @Override
    public void stop() {
    }
}
