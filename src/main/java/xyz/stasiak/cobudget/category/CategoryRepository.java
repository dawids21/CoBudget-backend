package xyz.stasiak.cobudget.category;

import io.vavr.collection.List;
import io.vavr.collection.Set;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import xyz.stasiak.cobudget.category.dto.CategoryReadModel;

@org.springframework.stereotype.Repository
interface CategoryRepository extends Repository<Category, Long> {

    Category save(Category category);

    @Query("select id, null, name from category where parent_id is null and user_id = :userId")
    Set<CategoryReadModel> findCategories(String userId);

    @Query("select id, parent_id, name from category where parent_id = :parentId and user_id = :userId")
    Set<CategoryReadModel> findSubcategories(String userId, Long parentId);

    @Query("""
            select category.id as category_id, category.name as category, subcategory.id as subcategory_id, subcategory.name as subcategory
            from category
            inner join category subcategory on subcategory.parent_id = category.id
            where category.user_id = :userId
            """)
    List<CategorySubcategoryProjection> findAllCategories(String userId);

}
