package xyz.stasiak.cobudget.entry;

import io.vavr.collection.List;
import io.vavr.collection.Set;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;

@org.springframework.stereotype.Repository
interface CategoryRepository extends Repository<Category, Long> {

    Category save(Category category);

    @Query("select id, name from category where parent_id is null and user_id = :userId")
    Set<CategoryReadModel> findCategories(String userId);

    @Query("select id, name from category where parent_id = :parentId and user_id = :userId")
    Set<CategoryReadModel> findSubcategories(String userId, Long parentId);

    @Query("""
            select category.id, category.name, subcategory.id, subcategory.name
            from category
            left join category subcategory on subcategory.parent_id = category.id
            where category.user_id = :userId
            """)
    List<SubcategoryReadModel> findAllCategories(String userId);

}
