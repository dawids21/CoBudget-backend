package xyz.stasiak.cobudget.entry;

import io.vavr.collection.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

class TrackController {

    @RequestMapping("/api/entry")
    @RequiredArgsConstructor
    @RestController
    static class EntryController {

        private final EntryRepository entryRepository;

        @PostMapping
        Entry add(@RequestBody EntryWriteModel dto) {
            return entryRepository.save(Entry.of(dto));
        }

        @GetMapping
        Set<EntryReadModel> getEntriesByDate(@RequestParam String from, @RequestParam String to) {
            return entryRepository.findByDateBetween(LocalDate.parse(from), LocalDate.parse(to));
        }
    }

    @RestController
    @RequestMapping("/api/category")
    @RequiredArgsConstructor
    static class CategoryController {

        private final CategoryRepository categoryRepository;

        @PostMapping
        Category add(@RequestBody Category category) {
            return categoryRepository.save(category);
        }

        @GetMapping("/{id}")
        ResponseEntity<Category> getEntriesByDate(@PathVariable long id) {
            return categoryRepository
                    .findById(id)
                    .map(ResponseEntity::ok)
                    .getOrElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    }
}