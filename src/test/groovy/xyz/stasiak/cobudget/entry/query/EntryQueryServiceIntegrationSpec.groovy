package xyz.stasiak.cobudget.entry.query

import org.springframework.beans.factory.annotation.Autowired
import xyz.stasiak.cobudget.DataIntegrationSpec
import xyz.stasiak.cobudget.common.UserId

import java.time.LocalDate

class EntryQueryServiceIntegrationSpec extends DataIntegrationSpec {

    @Autowired
    EntryQueryRepository queryRepository

    private EntryQueryService queryService

    void setup() {
        queryService = new EntryQueryService(queryRepository)
    }

    def "return entries in given time range"() {
        given:
        jdbcTemplate.execute("insert into entry(amount, date, category_id, user_id) values (-20, '2021-11-21', 2, 'userId'), (-20, '2021-11-18', 2, 'userId')")

        when:
        def results = queryService.getByDate(LocalDate.of(2021, 11, 20), LocalDate.of(2021, 11, 21), new UserId("userId"))

        then:
        results.size() == 1
    }
}