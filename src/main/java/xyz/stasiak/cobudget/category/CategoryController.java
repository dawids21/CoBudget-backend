package xyz.stasiak.cobudget.category;

import io.vavr.collection.Seq;
import io.vavr.collection.Set;
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
class CategoryController {

    private final CategoryRepository categoryRepository;

    @PostMapping("/category")
    ResponseEntity<Category> addCategory(@RequestBody CategoryWriteModel dto, @AuthenticationPrincipal Jwt jwt) {
        return UserId.get(jwt)
                .map(userId -> Category.of(dto, userId.id()))
                .map(categoryRepository::save)
                .map(ResponseEntity::ok)
                .getOrElseThrow(() -> new UserIdNotFound(jwt.getSubject()));
    }

    @GetMapping("/category")
    ResponseEntity<Set<CategoryReadModel>> getCategories(@AuthenticationPrincipal Jwt jwt) {
        return UserId.get(jwt)
                .map(userId -> categoryRepository.findCategories(userId.id()))
                .map(ResponseEntity::ok)
                .getOrElseThrow(() -> new UserIdNotFound(jwt.getSubject()));
    }

    @GetMapping("/category/{categoryId}/subcategory")
    ResponseEntity<Set<CategoryReadModel>> getSubcategories(@PathVariable long categoryId, @AuthenticationPrincipal Jwt jwt) {
        return UserId.get(jwt)
                .map(userId -> categoryRepository.findSubcategories(userId.id(), categoryId))
                .map(ResponseEntity::ok)
                .getOrElseThrow(() -> new UserIdNotFound(jwt.getSubject()));
    }

    @GetMapping("/category/all")
    ResponseEntity<Seq<CategoryWithSubcategoriesReadModel>> getAllCategories(@AuthenticationPrincipal Jwt jwt) {

        UserId userId = UserId.get(jwt).getOrElseThrow(() -> new UserIdNotFound(jwt.getSubject()));

        var map = categoryRepository.findAllCategories(userId.id())
                .groupBy(CategorySubcategoryReadModel::categoryId)
                .values()
                .map(category -> new CategoryWithSubcategoriesReadModel(category.get().categoryId(), category.map(subcategory -> new CategoryReadModel(subcategory.subcategoryId(), subcategory.categoryId(), subcategory.subcategory()))));

        return ResponseEntity.ok(map);
    }
}
