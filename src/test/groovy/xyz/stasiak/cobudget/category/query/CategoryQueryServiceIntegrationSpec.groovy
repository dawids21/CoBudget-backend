package xyz.stasiak.cobudget.category.query

import io.vavr.collection.List
import org.springframework.beans.factory.annotation.Autowired
import xyz.stasiak.cobudget.DataIntegrationSpec
import xyz.stasiak.cobudget.common.UserId

class CategoryQueryServiceIntegrationSpec extends DataIntegrationSpec {

    @Autowired
    CategoryQueryRepository queryRepository

    private CategoryQueryService queryService

    void setup() {
        queryService = new CategoryQueryService(queryRepository)
    }

    def "return categories or subcategories for given user"() {
        given:
        jdbcTemplate.execute(categoriesSql())

        when:
        def categories = queryService.getCategories(new UserId("userId"))
        then:
        categories.size() == 2

        when:
        def subcategories = queryService.getSubcategories(1L, new UserId("userId"))
        then:
        subcategories.size() == 1
    }

    def "return categories with list of subcategories"() {
        given:
        jdbcTemplate.execute(categoriesSql())

        when:
        def categories = queryService.getAllCategories(new UserId("userId"))

        then:
        categories.size() == 2
        assertCategorySize(categories, 'Food', 1)
        assertCategorySize(categories, 'Clothes', 0)
    }

    private static void assertCategorySize(List<CategorySubcategoryReadModel> categories, String name, int size) {
        assert !findCategory(categories, name).isEmpty()
        assert findCategory(categories, name).get().subcategories().size() == size
    }

    private static def findCategory(List<CategorySubcategoryReadModel> categories, String name) {
        categories.filter(category -> category.name() == name)
    }

    private static def categoriesSql() {
        // language=SQL
        """
insert into category(id, name, user_id, parent_id) values
(1, 'Food', 'userId', null),
(2, 'Home', 'userId', 1),
(3, 'Clothes', 'userId', null);
"""
    }
}
