package jp.takeda.springathena

import jp.takeda.springathena.dao.SomeDao
import org.springframework.boot.ExitCodeGenerator
import org.springframework.boot.SpringApplication
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.time.LocalDate


@SpringBootApplication
class SpringAthenaApplication(
        private val someDao: SomeDao
) {
    fun run(vararg args: String) {
        val persons = someDao.selectSomeByDate(LocalDate.of(2019, 4, 11))
        persons.forEach {
            println(it)
        }
    }
}

fun main(args: Array<String>) {
    val springApp = SpringApplication(SpringAthenaApplication::class.java)
    springApp.webApplicationType = WebApplicationType.NONE

    val exitCode = springApp.run().use {
        try {
            val app = it.getBean(SpringAthenaApplication::class.java)
            app.run(*args)
            SpringApplication.exit(it)
        } catch (e: Exception) {
            e.printStackTrace()
            SpringApplication.exit(it, ExitCodeGenerator { -1 })
        }
    }

    System.exit(exitCode)
}
