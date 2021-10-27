package xyz.stasiak.cobudget.entry;

import io.vavr.control.Option;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
interface CategoryRepository extends Repository<Category, Long> {

    Category save(Category category);

    Option<Category> findById(Long id);

}
