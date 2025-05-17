package org.example.framework.config

import com.alibaba.druid.pool.DruidDataSource
import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceBuilder
import jakarta.persistence.EntityManagerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration(proxyBeanMethods = false)
@EnableJpaRepositories(
    basePackages = ["org.example.framework.repository.postgres"],
    entityManagerFactoryRef = "postgresEntityManagerFactory",
    transactionManagerRef = "postgresTransactionManager"
)
@EnableTransactionManagement
class PostgresConfig {

    @Bean
    @ConfigurationProperties("app.postgres")
    fun postgres(): DruidDataSource {
        return DruidDataSourceBuilder.create().build()
    }

    @Bean
    fun postgresEntityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        @Qualifier("postgres") dataSource: DruidDataSource
    ): LocalContainerEntityManagerFactoryBean {
        return builder
            .dataSource(dataSource)
            .packages("org.example.framework.entity.postgres")
            .persistenceUnit("postgres")
            .build()
    }

    @Bean
    fun postgresTransactionManager(
        @Qualifier("postgresEntityManagerFactory") entityManagerFactory: EntityManagerFactory
    ): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }
}