package xyz.stasiak.cobudget.entry;

import io.vavr.collection.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/entry")
@RequiredArgsConstructor
class EntryController {

    private final EntryRepository entryRepository;

    @PostMapping
    Entry add(@RequestBody Entry entry) {
        return entryRepository.save(entry);
    }

    @GetMapping
    Set<Entry> getEntriesByDate(@RequestParam String from, @RequestParam String to) {
        return entryRepository.findByDateBetween(LocalDate.parse(from), LocalDate.parse(to));
    }
}
