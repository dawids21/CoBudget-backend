package xyz.stasiak.cobudget.entry.query;

import io.vavr.collection.Set;
import lombok.RequiredArgsConstructor;
import xyz.stasiak.cobudget.common.UserId;

import java.time.LocalDate;

@RequiredArgsConstructor
class EntryQueryService {

    private final EntryQueryRepository repository;

    Set<EntryReadModel> getByDate(LocalDate from, LocalDate to, UserId userId) {
        return repository.findByDateBetween(from, to, userId.id());
    }

}
