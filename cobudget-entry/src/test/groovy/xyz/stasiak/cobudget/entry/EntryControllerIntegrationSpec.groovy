package xyz.stasiak.cobudget.entry

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.mock.DetachedMockFactory

import java.time.LocalDate

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@WebMvcTest(EntryController)
@Import(WebIntegrationMockConfig)
class EntryControllerIntegrationSpec extends WebIntegrationSpec {

    @Autowired
    EntryRepository entryRepository

    def "post saves entry in repository"() {
        when:
        def result = mvc.perform(
                post("/api/entry")
                        .with(getJwt("userId"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(entryJson())
        )
                .andReturn()
                .response

        then:
        result.status == HttpStatus.OK.value()
        1 * repository.save(entry())
    }

    static def entryJson() {
        """
{
    "amount": -20,
    "date": "2021-11-21",
    "categoryId": 2
}
"""
    }

    static def entry() {
        def entryWriteModel = new EntryWriteModel(-20, LocalDate.of(2021, 11, 21), 2L)

        Entry.of(entryWriteModel, "userId")
    }

    @TestConfiguration
    static class MockConfig {
        def detachedMockFactory = new DetachedMockFactory()

        @Bean
        EntryRepository entryRepository() {
            detachedMockFactory.Mock(EntryRepository)
        }
    }
}