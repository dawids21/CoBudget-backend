package xyz.stasiak.cobudget.entry;

import io.vavr.control.Option;
import org.springframework.data.repository.Repository;

@org.springframework.stereotype.Repository
interface CategoryRepository extends Repository<Category, Long> {

    Category save(Category category);

    Option<Category> findById(Long id);

}
