package jp.takeda.springathena.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import java.sql.Driver
import java.util.*

@Configuration
class AthenaConfig {
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    fun dataSourceProperties() = DataSourceProperties()

    @Bean("dataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    fun dataSource(): SimpleDriverDataSource {
        val settings = dataSourceProperties()
        val dataSource = SimpleDriverDataSource()

        dataSource.apply {
            @Suppress("UNCHECKED_CAST")
            setDriverClass(Class.forName(settings.determineDriverClassName()) as Class<out Driver>)
            url = settings.determineUrl()

            // 認証設定を追加する
            val connectionProperties = Properties()
            connectionProperties.setProperty(
                    "aws_credentials_provider_class",
                    "com.amazonaws.auth.profile.ProfileCredentialsProvider"
            )
            setConnectionProperties(connectionProperties)
        }

        return dataSource
    }

    @Bean("jdbcTemplate")
    fun jdbcTemplate(
            @Autowired
            @Qualifier("dataSource")
            dataSource: SimpleDriverDataSource
    ): JdbcTemplate = JdbcTemplate(dataSource)
}