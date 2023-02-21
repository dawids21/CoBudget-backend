package xyz.stasiak.cobudget.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.stasiak.cobudget.category.exception.CategoryIdNotFound;
import xyz.stasiak.cobudget.category.exception.MainCategoryNotFound;
import xyz.stasiak.cobudget.common.ErrorMessage;
import xyz.stasiak.cobudget.common.UserId;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/api/category")
@RestController
@CrossOrigin(origins = "${security.cors.origins}")
@Slf4j
class CategoryController {

    private final CategoryApplicationService categoryApplicationService;

    @PostMapping
    ResponseEntity<Category> addCategory(@RequestBody CategoryWriteModel dto, Principal principal) {
        var userId = new UserId(principal.getName());
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

    @ExceptionHandler(MainCategoryNotFound.class)
    ResponseEntity<ErrorMessage> handleMainCategoryNotFound(MainCategoryNotFound exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.badRequest().body(ErrorMessage.of(exception));
    }
}
