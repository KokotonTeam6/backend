package kokoton.sextet

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource("classpath:application.properties")
class Database {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    fun hikariDataSource(): HikariConfig {
        val hikariConfig = HikariConfig()
        return hikariConfig
    }

    @Bean
    fun dataSource(): HikariDataSource {
        val dataSource = HikariDataSource(hikariDataSource())

        return dataSource
    }
}