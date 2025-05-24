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
    basePackages = ["org.example.framework.repository.mariadb"],
    entityManagerFactoryRef = "mariadbEntityManagerFactory",
    transactionManagerRef = "mariadbTransactionManager"
)
@EnableTransactionManagement
class MariadbConfig {

    @Qualifier("mariadb")
    @Bean(defaultCandidate = false)
    @ConfigurationProperties("app.mariadb")
    fun mariadb(): DruidDataSource {
        return DruidDataSourceBuilder.create().build()
    }

    @Qualifier("mariadb")
    @Bean(defaultCandidate = false)
    fun mariadbEntityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        @Qualifier("mariadb") dataSource: DruidDataSource
    ): LocalContainerEntityManagerFactoryBean {
        return builder
            .dataSource(dataSource)
            .packages("org.example.framework.entity.mariadb")
            .persistenceUnit("mariadb")
            .build()
    }

    @Qualifier("mariadb")
    @Bean(defaultCandidate = false)
    fun mariadbTransactionManager(
        @Qualifier("mariadbEntityManagerFactory") entityManagerFactory: EntityManagerFactory
    ): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }
}