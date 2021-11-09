package xyz.stasiak.cobudget.entry;

import io.vavr.collection.Set;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;

@org.springframework.stereotype.Repository
interface CategoryRepository extends Repository<Category, Long> {

    Category save(Category category);

    @Query("select name from category where user_id = :userId")
    Set<CategoryReadModel> findAll(String userId);

}
