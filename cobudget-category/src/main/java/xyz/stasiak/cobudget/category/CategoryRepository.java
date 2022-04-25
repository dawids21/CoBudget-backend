package xyz.stasiak.cobudget.category;

import io.vavr.collection.Set;
import io.vavr.control.Option;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;

@org.springframework.stereotype.Repository
public interface CategoryRepository extends Repository<Category, Long> {

    Category save(Category category);

    @Query("select * from category where id = :id")
    Option<Category> findById(long id);

    @Query("select * from category where parent_id = :parentId")
    Set<Category> findAllByParentId(long parentId);

    @Query("select * from category where parent_id = :parentId and name = :name and user_id = :userId")
    Option<Category> findByParentIdAndName(Long parentId, String name, String userId);
}
