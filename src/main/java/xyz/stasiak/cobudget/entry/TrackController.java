package xyz.stasiak.cobudget.entry;

import io.vavr.collection.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
@CrossOrigin(origins = "${security.cors.origins}")
class TrackController {

    private final EntryRepository entryRepository;
    private final CategoryRepository categoryRepository;

    @PostMapping("/entry")
    Entry addEntry(@RequestBody EntryWriteModel dto) {
        return entryRepository.save(Entry.of(dto));
    }

    @GetMapping("/entry")
    Set<EntryReadModel> getEntriesByDate(@RequestParam String from, @RequestParam String to) {
        return entryRepository.findByDateBetween(LocalDate.parse(from), LocalDate.parse(to));
    }


    @PostMapping("/category")
    Category addCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @GetMapping("/category/{id}")
    ResponseEntity<Category> getCategory(@PathVariable long id) {
        return categoryRepository
                .findById(id)
                .map(ResponseEntity::ok)
                .getOrElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}