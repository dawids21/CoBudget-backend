package xyz.stasiak.cobudget.category


import io.vavr.collection.HashMap
import io.vavr.control.Option
import spock.lang.Specification

class CategoryApplicationServiceSpec extends Specification {

    private final CategoryRepository repository = new TestCategoryRepository()
    private final CategoryApplicationService categoryApplicationService = new CategoryApplicationService(repository)

    def "delete should mark category as disabled"() {
        when:
        categoryApplicationService.disable(2L)

        then:
        def category = repository.findById(2L).get()
        category.isDisabled()
    }
}

class TestCategoryRepository implements CategoryRepository {

    private io.vavr.collection.Map<Long, Category> categories
    private long nextId = 4

    TestCategoryRepository() {
        this.categories = HashMap.of(
                1L, new Category(1L, "user", null, "Food", false),
                2L, new Category(2L, "user", 1L, "Home", false),
                3L, new Category(3L, "user", 1L, "Work", false)
        )
    }

    Option<Category> findById(long id) {
        return categories.get(id)
    }

    @Override
    Category save(Category category) {
        if (category.getId() == null) {
            category.setId(nextId)
            nextId++
        }
        categories = categories.put(category.getId(), category)
        return category
    }
}
