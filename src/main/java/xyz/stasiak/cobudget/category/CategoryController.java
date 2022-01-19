package xyz.stasiak.cobudget.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import xyz.stasiak.cobudget.category.exception.CategoryIdNotFound;
import xyz.stasiak.cobudget.common.ErrorMessage;
import xyz.stasiak.cobudget.common.UserId;
import xyz.stasiak.cobudget.common.UserIdNotFound;

@RequiredArgsConstructor
@RequestMapping("/api/category")
@RestController
@CrossOrigin(origins = "${security.cors.origins}")
@Slf4j
class CategoryController {

    private final CategoryApplicationService categoryApplicationService;

    @PostMapping
    ResponseEntity<Category> addCategory(@RequestBody CategoryWriteModel dto, @AuthenticationPrincipal Jwt jwt) {
        var userId = UserId.get(jwt).getOrElseThrow(() -> new UserIdNotFound(jwt.getSubject()));
        var category = Category.of(dto, userId.id());
        return ResponseEntity.ok(categoryApplicationService.add(category));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable long id) {
        log.debug("Delete category with id {}", id);
        categoryApplicationService.disable(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CategoryIdNotFound.class)
    ResponseEntity<ErrorMessage> handleCategoryNotFound(CategoryIdNotFound exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.badRequest().body(ErrorMessage.of(exception));
    }
}
