package xyz.stasiak.cobudget.category;

import io.vavr.control.Option;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;

@org.springframework.stereotype.Repository
interface CategoryRepository extends Repository<Category, Long> {

    Category save(Category category);

    @Query("select * from category where id = :id")
    Option<Category> findById(long id);

}
