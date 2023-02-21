package xyz.stasiak.cobudget.category.query;

import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.stasiak.cobudget.common.UserId;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/api/category")
@RestController
@CrossOrigin(origins = "${security.cors.origins}")
class CategoryQueryController {

    private final CategoryQueryService queryService;

    @GetMapping
    ResponseEntity<List<CategoryReadModel>> getCategories(Principal principal) {

        var userId = new UserId(principal.getName());

        return ResponseEntity.ok(queryService.getCategories(userId));
    }

    @GetMapping("/{categoryId}/subcategory")
    ResponseEntity<List<CategoryReadModel>> getSubcategories(@PathVariable long categoryId, Principal principal) {

        var userId = new UserId(principal.getName());

        return ResponseEntity.ok(queryService.getSubcategories(categoryId, userId));
    }

    @GetMapping("/all")
    ResponseEntity<List<CategorySubcategoryReadModel>> getAllCategories(Principal principal) {

        var userId = new UserId(principal.getName());

        return ResponseEntity.ok(queryService.getAllCategories(userId));
    }
}
