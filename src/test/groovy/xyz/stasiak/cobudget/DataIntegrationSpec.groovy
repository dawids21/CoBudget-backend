package xyz.stasiak.cobudget

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.spock.Testcontainers
import spock.lang.Specification


@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Testcontainers
// https://github.com/spockframework/spock/issues/1539
@ContextConfiguration
abstract class DataIntegrationSpec extends Specification {

    @Autowired
    protected JdbcTemplate jdbcTemplate

}