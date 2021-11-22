package xyz.stasiak.cobudget.category.query;

import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import xyz.stasiak.cobudget.common.UserId;
import xyz.stasiak.cobudget.common.UserIdNotFound;

@RequiredArgsConstructor
@RequestMapping("/api/category")
@RestController
@CrossOrigin(origins = "${security.cors.origins}")
class CategoryQueryController {

    private final CategoryQueryService queryService;

    @GetMapping
    ResponseEntity<List<CategoryReadModel>> getCategories(@AuthenticationPrincipal Jwt jwt) {

        var userId = UserId.get(jwt).getOrElseThrow(() -> new UserIdNotFound(jwt.getSubject()));

        return ResponseEntity.ok(queryService.getCategories(userId));
    }

    @GetMapping("/{categoryId}/subcategory")
    ResponseEntity<List<CategoryReadModel>> getSubcategories(@PathVariable long categoryId, @AuthenticationPrincipal Jwt jwt) {

        var userId = UserId.get(jwt).getOrElseThrow(() -> new UserIdNotFound(jwt.getSubject()));

        return ResponseEntity.ok(queryService.getSubcategories(categoryId, userId));
    }

    @GetMapping("/all")
    ResponseEntity<List<CategorySubcategoryReadModel>> getAllCategories(@AuthenticationPrincipal Jwt jwt) {

        var userId = UserId.get(jwt).getOrElseThrow(() -> new UserIdNotFound(jwt.getSubject()));

        return ResponseEntity.ok(queryService.getAllCategories(userId));
    }
}
