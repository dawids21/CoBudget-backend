package xyz.stasiak.cobudget.category

import io.vavr.collection.HashMap
import io.vavr.collection.Set
import io.vavr.control.Option
import spock.lang.Specification
import xyz.stasiak.cobudget.category.exception.CategoryIdNotFound
import xyz.stasiak.cobudget.category.exception.MainCategoryNotFound

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

    def "enable disabled category when the name and parent id is the same"() {
        given:
        categoryApplicationService.disable(2L)
        def categoryWriteModel = new CategoryWriteModel(parentId, name)

        when:
        categoryApplicationService.add(Category.of(categoryWriteModel, userId))

        then:
        repository.findById(2L).get().isDisabled() == isDisabled
        repository.findById(4L).isEmpty() == isEmpty

        where:
        parentId | name   | userId       || isDisabled | isEmpty
        1L       | "Home" | "user"       || false      | true
        2L       | "Home" | "user"       || true       | false
        1L       | "City" | "user"       || true       | false
        1L       | "Home" | "other user" || true       | false
    }

    def "enable only category not its subcategories"() {
        given:
        categoryApplicationService.disable(1L)
        def categoryWriteModel = new CategoryWriteModel(null, "Food")

        when:
        categoryApplicationService.add(Category.of(categoryWriteModel, "user"))

        then:
        !repository.findById(1L).get().isDisabled()
        repository.findById(2L).get().isDisabled()
        repository.findById(3L).get().isDisabled()
    }

    def "enable also category when enabling subcategories"() {
        given:
        categoryApplicationService.disable(1L)
        def categoryWriteModel = new CategoryWriteModel(1, "Home")

        when:
        categoryApplicationService.add(Category.of(categoryWriteModel, "user"))

        then:
        !repository.findById(1L).get().isDisabled()
        !repository.findById(2L).get().isDisabled()
        repository.findById(3L).get().isDisabled()
    }

    def "throw exception when main category for subcategory not found when enabling"() {
        given:
        def categoryWriteModel = new CategoryWriteModel(6, "new sub")
        categoryApplicationService.add(Category.of(categoryWriteModel, "user"))
        categoryApplicationService.disable(4L)

        when:
        categoryApplicationService.add(Category.of(categoryWriteModel, "user"))

        then:
        thrown(MainCategoryNotFound)
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

    @Override
    Option<Category> findByParentIdAndName(Long parentId, String name, String userId) {
        return Option.of(categories.values()
                .filter(category -> category.getParentId() == parentId)
                .filter(category -> category.getUserId() == userId)
                .find(category -> category.getName() == name) as Category)
    }
}
