package xyz.stasiak.cobudget.entry;

import io.vavr.collection.Seq;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
@CrossOrigin(origins = "${security.cors.origins}")
@Slf4j
class TrackController {

    private final EntryRepository entryRepository;
    private final CategoryRepository categoryRepository;

    @PostMapping("/entry")
    ResponseEntity<Entry> addEntry(@RequestBody EntryWriteModel dto, @AuthenticationPrincipal Jwt jwt) {

        return getUserId(jwt)
                .map(userId -> Entry.of(dto, userId))
                .map(entryRepository::save)
                .map(ResponseEntity::ok)
                .getOrElseThrow(() -> new UserIdNotFound(jwt.getSubject()));
    }

    @GetMapping("/entry")
    ResponseEntity<Set<EntryReadModel>> getEntriesByDate(@RequestParam String from, @RequestParam String to, @AuthenticationPrincipal Jwt jwt) {
        return getUserId(jwt)
                .map(userId -> entryRepository.findByDateBetween(LocalDate.parse(from), LocalDate.parse(to), userId))
                .map(ResponseEntity::ok)
                .getOrElseThrow(() -> new UserIdNotFound(jwt.getSubject()));
    }


    @PostMapping("/category")
    ResponseEntity<Category> addCategory(@RequestBody CategoryWriteModel dto, @AuthenticationPrincipal Jwt jwt) {
        return getUserId(jwt)
                .map(userId -> Category.of(dto, userId))
                .map(categoryRepository::save)
                .map(ResponseEntity::ok)
                .getOrElseThrow(() -> new UserIdNotFound(jwt.getSubject()));
    }

    @GetMapping("/category")
    ResponseEntity<Set<CategoryReadModel>> getCategories(@AuthenticationPrincipal Jwt jwt) {
        return getUserId(jwt)
                .map(categoryRepository::findCategories)
                .map(ResponseEntity::ok)
                .getOrElseThrow(() -> new UserIdNotFound(jwt.getSubject()));
    }

    @GetMapping("/category/{categoryId}/subcategory")
    ResponseEntity<Set<CategoryReadModel>> getSubcategories(@PathVariable long categoryId, @AuthenticationPrincipal Jwt jwt) {
        return getUserId(jwt)
                .map(userId -> categoryRepository.findSubcategories(userId, categoryId))
                .map(ResponseEntity::ok)
                .getOrElseThrow(() -> new UserIdNotFound(jwt.getSubject()));
    }

    @GetMapping("/category/all")
    ResponseEntity<Seq<CategoryWithSubcategoriesReadModel>> getAllCategories(@AuthenticationPrincipal Jwt jwt) {

        String userId = getUserId(jwt).getOrElseThrow(() -> new UserIdNotFound(jwt.getSubject()));

        var map = categoryRepository.findAllCategories(userId)
                .groupBy(CategorySubcategoryReadModel::categoryId)
                .values()
                .map(category -> new CategoryWithSubcategoriesReadModel(category.get().categoryId(), category.map(subcategory -> new CategoryReadModel(subcategory.subcategoryId(), subcategory.categoryId(), subcategory.subcategory()))));

        return ResponseEntity.ok(map);
    }

    private Option<String> getUserId(Jwt jwt) {
        return Option.of(jwt.getClaim("uid"));
    }

    @ExceptionHandler(UserIdNotFound.class)
    private ResponseEntity<?> handleNoUserId(UserIdNotFound exception) {
        log.error("User id was not in jwt token for subject: {}", exception.getSubject());
        return ResponseEntity.badRequest().build();
    }
}