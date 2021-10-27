package xyz.stasiak.cobudget.entry;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
class CategoryController {

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
