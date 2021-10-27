package xyz.stasiak.cobudget.entry;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.util.Set;

@RepositoryRestResource
interface EntryRepository extends Repository<Entry, Long> {

    Entry save(Entry expense);

    @Query("select * from expense where date >= :start and date <= :end")
    Set<Entry> findByDateBetween(LocalDate start, LocalDate end);
}
