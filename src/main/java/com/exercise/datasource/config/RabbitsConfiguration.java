package com.exercise.datasource.config;

import com.exercise.datasource.domain.rabbits.Rabbit;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        basePackageClasses = Rabbit.class,
        entityManagerFactoryRef = "rabbitsEntityManagerFactory",
        transactionManagerRef = "rabbitsTransactionManager"
)
public class RabbitsConfiguration {

    @Value("classpath:db-init/rabbits-schema.sql")
    private Resource schemaScript;

    @Value("classpath:db-init/rabbits-data.sql")
    private Resource dataScript;

    @Bean
    @ConfigurationProperties("spring.datasource.rabbits")
    public DataSourceProperties rabbitsDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource rabbitsDataSource() {
        return rabbitsDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    public DataSourceInitializer rabbitsDataSourceInitializer(@Qualifier("rabbitsDataSource") DataSource dataSource) {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(rabbitsDatabasePopulator());
        return initializer;
    }

    private DatabasePopulator rabbitsDatabasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(schemaScript);
        populator.addScript(dataScript);
        return populator;
    }

    @Bean
    public JdbcTemplate rabbitsJdbcTemplate(@Qualifier("rabbitsDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean rabbitsEntityManagerFactory(
            @Qualifier("rabbitsDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource)
                .packages(Rabbit.class)
                .build();
    }

    @Bean
    public PlatformTransactionManager rabbitsTransactionManager(
            @Qualifier("rabbitsEntityManagerFactory") LocalContainerEntityManagerFactoryBean rabbitsEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(rabbitsEntityManagerFactory.getObject()));
    }
}
