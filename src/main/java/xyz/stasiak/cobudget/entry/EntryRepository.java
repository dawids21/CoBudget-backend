package xyz.stasiak.cobudget.entry;

import io.vavr.collection.Set;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;

import java.time.LocalDate;

@org.springframework.stereotype.Repository
interface EntryRepository extends Repository<Entry, Long> {

    Entry save(Entry expense);

    @Query("""
            select entry.id, amount, date, category.name as category, subcategory.name as subcategory
            from entry
            left join category subcategory on subcategory.id = entry.category_id
            left join category on category.id = subcategory.parent_id
            where entry.user_id like :userId and date >= :start and date <= :end
            """)
    Set<EntryReadModel> findByDateBetween(LocalDate start, LocalDate end, String userId);
}
