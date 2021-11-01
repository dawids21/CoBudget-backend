package xyz.stasiak.cobudget.entry;

import io.vavr.collection.Set;
import org.springframework.data.repository.Repository;

@org.springframework.stereotype.Repository
interface CategoryRepository extends Repository<Category, Long> {

    Category save(Category category);

    Set<Category> findAll();

}
