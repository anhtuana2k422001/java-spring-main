package com.hoanhtuan.main_app_security.common;

import com.hoanhtuan.main_app_security.common.datasource.DBType;
import com.hoanhtuan.main_app_security.common.datasource.DataSourceSingleton;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class CheckerStartupApplication implements ApplicationListener<ApplicationReadyEvent> {


    @Autowired
    private final Environment environment;
    // Sử dụng dependency injection để inject Environment

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        checkActiveProfiles();
        DataSourceSingleton.intDataSource(DBType.ORACLE, "SYSTEM");
        DataSourceSingleton.intDataSource(DBType.POSTGRESQL, "POSTGRES");
        LOG.info("Startup app event getTimeTaken = {}", event.getTimeTaken());
    }


    public void checkActiveProfiles() {
        String[] activeProfiles = environment.getActiveProfiles();
        for (String profile : activeProfiles) {
            LOG.info("Active profile = {}", profile);
        }
    }
}
