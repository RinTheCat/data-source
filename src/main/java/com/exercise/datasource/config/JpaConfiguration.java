package com.exercise.datasource.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.exercise.datasource.repository",
        entityManagerFactoryRef = "mainEntityManagerFactory",
        transactionManagerRef = "mainTransactionManager"
)
public class JpaConfiguration {

    @Value("classpath:db-init/schema.sql")
    private Resource schemaScript;

    @Value("classpath:db-init/first-data.sql")
    private Resource firstDataScript;

    @Value("classpath:db-init/second-data.sql")
    private Resource secondDataScript;

    @Bean
    public LocalContainerEntityManagerFactoryBean mainEntityManagerFactory(
            @Qualifier("dynamicDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource)
                .packages("com.exercise.datasource.domain")
                .build();
    }

    @Bean
    public PlatformTransactionManager mainTransactionManager(
            @Qualifier("mainEntityManagerFactory") LocalContainerEntityManagerFactoryBean catsEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(catsEntityManagerFactory.getObject()));
    }

    // FIRST
    @Bean
    public DataSourceInitializer firstDataSourceInitializer(@Qualifier("firstDataSource") DataSource dataSource) {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(firstDatabasePopulator());
        return initializer;
    }

    @Bean
    public DatabasePopulator firstDatabasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(schemaScript);
        populator.addScript(firstDataScript);
        return populator;
    }

    // SECOND
    @Bean
    public DataSourceInitializer secondDataSourceInitializer(@Qualifier("secondDataSource") DataSource dataSource) {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(secondDatabasePopulator());
        return initializer;
    }

    @Bean
    public DatabasePopulator secondDatabasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(schemaScript);
        populator.addScript(secondDataScript);
        return populator;
    }
}
