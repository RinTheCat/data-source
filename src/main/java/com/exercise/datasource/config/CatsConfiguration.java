package com.exercise.datasource.config;

import com.exercise.datasource.domain.cats.Cat;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
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
        basePackageClasses = Cat.class,
        entityManagerFactoryRef = "catsEntityManagerFactory",
        transactionManagerRef = "catsTransactionManager"
)
public class CatsConfiguration {

    @Value("classpath:db-init/cats-schema.sql")
    private Resource schemaScript;

    @Value("classpath:db-init/cats-data.sql")
    private Resource dataScript;

    @Bean
    @ConfigurationProperties("spring.datasource.cats")
    public DataSourceProperties catsDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary // EntityManagerFactoryBuilder is declared in org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration
    // and this class needs a single data source injected
    public DataSource catsDataSource() {
        return catsDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    public DataSourceInitializer catsDataSourceInitializer(@Qualifier("catsDataSource") DataSource dataSource) {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(catsDatabasePopulator());
        return initializer;
    }

    private DatabasePopulator catsDatabasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(schemaScript);
        populator.addScript(dataScript);
        return populator;
    }

    @Bean
    public JdbcTemplate catsJdbcTemplate(@Qualifier("catsDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean catsEntityManagerFactory(
            @Qualifier("catsDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource)
                .packages(Cat.class)
                .build();
    }

    @Bean
    public PlatformTransactionManager catsTransactionManager(
            @Qualifier("catsEntityManagerFactory") LocalContainerEntityManagerFactoryBean catsEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(catsEntityManagerFactory.getObject()));
    }
}
