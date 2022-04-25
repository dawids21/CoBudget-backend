package xyz.stasiak.cobudget.category.query;

import io.vavr.collection.List;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;

@org.springframework.stereotype.Repository
public interface CategoryQueryRepository extends Repository<CategoryReadModel, Long> {

    @Query("select id, null, name from category where parent_id is null and user_id = :userId and disabled = false")
    List<CategoryReadModel> findCategories(String userId);

    @Query("select id, parent_id, name from category where parent_id is not null and user_id = :userId and disabled = false")
    List<CategoryReadModel> findSubcategories(String userId);

    @Query("select id, parent_id, name from category where parent_id = :parentId and user_id = :userId and disabled = false")
    List<CategoryReadModel> findSubcategories(Long parentId, String userId);

}
