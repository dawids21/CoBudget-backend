package xyz.stasiak.cobudget.category


import io.vavr.collection.HashMap
import io.vavr.collection.Set
import io.vavr.control.Option
import spock.lang.Specification
import xyz.stasiak.cobudget.category.exception.CategoryIdNotFound

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

    def "disable throw exception when category not found"() {
        given:
        def notExistingId = 6L

        when:
        categoryApplicationService.disable(notExistingId)

        then:
        thrown(CategoryIdNotFound)
    }

    def "disable all subcategories when disabling main category"() {
        when:
        categoryApplicationService.disable(1L)

        then:
        repository.findById(1L).get().isDisabled()
        repository.findById(2L).get().isDisabled()
        repository.findById(3L).get().isDisabled()
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

    @Override
    Category save(Category category) {
        if (category.getId() == null) {
            category.setId(nextId)
            nextId++
        }
        categories = categories.put(category.getId(), category)
        return category
    }

    Option<Category> findById(long id) {
        return categories.get(id)
    }

    @Override
    Set<Category> findAllByParentId(long parentId) {
        return categories.values().filter(category -> category.getParentId() == parentId).toSet()
    }
}
