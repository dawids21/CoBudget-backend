package xyz.stasiak.cobudget.entry;

import org.springframework.data.repository.Repository;

@org.springframework.stereotype.Repository
public interface EntryRepository extends Repository<Entry, Long> {

    Entry save(Entry expense);

}
