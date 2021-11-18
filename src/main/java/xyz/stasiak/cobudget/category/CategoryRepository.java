package xyz.stasiak.cobudget.category;

import io.vavr.collection.List;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import xyz.stasiak.cobudget.category.dto.CategoryReadModel;

@org.springframework.stereotype.Repository
interface CategoryRepository extends Repository<Category, Long> {

    Category save(Category category);

    @Query("select id, null, name from category where parent_id is null and user_id = :userId")
    List<CategoryReadModel> findCategories(String userId);

    @Query("select id, parent_id, name from category where parent_id is not null and user_id = :userId")
    List<CategoryReadModel> findSubcategories(String userId);

    @Query("select id, parent_id, name from category where parent_id = :parentId and user_id = :userId")
    List<CategoryReadModel> findSubcategories(String userId, Long parentId);

}
