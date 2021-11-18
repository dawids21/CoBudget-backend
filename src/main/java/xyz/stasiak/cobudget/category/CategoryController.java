package xyz.stasiak.cobudget.category;

import io.vavr.collection.Seq;
import io.vavr.collection.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import xyz.stasiak.cobudget.category.dto.CategoryReadModel;
import xyz.stasiak.cobudget.category.dto.CategorySubcategoryReadModel;
import xyz.stasiak.cobudget.category.dto.CategoryWriteModel;
import xyz.stasiak.cobudget.common.UserId;
import xyz.stasiak.cobudget.common.UserIdNotFound;

@RequiredArgsConstructor
@RequestMapping("/api/category")
@RestController
@CrossOrigin(origins = "${security.cors.origins}")
class CategoryController {

    private final CategoryRepository categoryRepository;

    @PostMapping
    ResponseEntity<Category> addCategory(@RequestBody CategoryWriteModel dto, @AuthenticationPrincipal Jwt jwt) {

        var userId = UserId.get(jwt).getOrElseThrow(() -> new UserIdNotFound(jwt.getSubject()));

        var category = Category.of(dto, userId.id());

        return ResponseEntity.ok(categoryRepository.save(category));
    }

    @GetMapping
    ResponseEntity<Set<CategoryReadModel>> getCategories(@AuthenticationPrincipal Jwt jwt) {

        var userId = UserId.get(jwt).getOrElseThrow(() -> new UserIdNotFound(jwt.getSubject()));

        return ResponseEntity.ok(categoryRepository.findCategories(userId.id()));
    }

    @GetMapping("/{categoryId}/subcategory")
    ResponseEntity<Set<CategoryReadModel>> getSubcategories(@PathVariable long categoryId, @AuthenticationPrincipal Jwt jwt) {

        var userId = UserId.get(jwt).getOrElseThrow(() -> new UserIdNotFound(jwt.getSubject()));

        return ResponseEntity.ok(categoryRepository.findSubcategories(userId.id(), categoryId));
    }

    @GetMapping("/all")
    ResponseEntity<Seq<CategorySubcategoryReadModel>> getAllCategories(@AuthenticationPrincipal Jwt jwt) {

        var userId = UserId.get(jwt).getOrElseThrow(() -> new UserIdNotFound(jwt.getSubject()));

        var categories = categoryRepository.findAllCategories(userId.id())
                .groupBy(CategorySubcategoryProjection::categoryId)
                .values()
                .map(CategorySubcategoryProjection::toCategorySubcategoryReadModel);

        return ResponseEntity.ok(categories);
    }
}
