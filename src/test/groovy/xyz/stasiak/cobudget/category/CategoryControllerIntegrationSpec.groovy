package xyz.stasiak.cobudget.category

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import spock.mock.DetachedMockFactory
import xyz.stasiak.cobudget.WebIntegrationSpec

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@WebMvcTest(CategoryController)
@Import(WebIntegrationMockConfig)
// https://github.com/spockframework/spock/issues/1539
@ContextConfiguration
class CategoryControllerIntegrationSpec extends WebIntegrationSpec {

    @Autowired
    CategoryApplicationService categoryApplicationService

    def "posts add new category using service"() {

        when:
        def response = mvc.perform(
                post("/api/category")
                        .with(getJwt("userId"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson())
        )
                .andReturn()
                .response

        then:
        response.status == HttpStatus.OK.value()
        1 * categoryApplicationService.add(category())

    }

    private static def categoryJson() {
        """
{
    "name": "Food",
    "parentId": 1
}
"""
    }

    private static def category() {
        def category = new CategoryWriteModel(1, "Food")

        Category.of(category, "userId")
    }

    @TestConfiguration
    static class MockConfig {
        def detachedMockFactory = new DetachedMockFactory()

        @Bean
        CategoryApplicationService categoryApplicationService() {
            detachedMockFactory.Mock(CategoryApplicationService)
        }
    }
}
