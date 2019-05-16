package jp.takeda.springathena.dao

import jp.takeda.springathena.entity.Person
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class SomeDao(
        @Qualifier("jdbcTemplate")
        private val jdbcTemplate: JdbcTemplate,
        @Value("\${athena.database}")
        private val database: String
) {
    fun selectSomeByDate(date: LocalDate): List<Person> {
        val sql = """
            SELECT
             name,
             age
            FROM
             $database.some
            WHERE
             some.year = '${date.year}'
             AND some.month = '${date.monthValue.toString().padStart(2, '0')}'
             AND some.day = '${date.dayOfMonth.toString().padStart(2, '0')}'
            ;
        """.trimIndent()

        return jdbcTemplate.query(sql) { resultSet, _ ->
            Person(
                    name = resultSet.getString("name"),
                    age = resultSet.getInt("age")
            )
        }
    }
}
