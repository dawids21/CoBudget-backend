package xyz.stasiak.cobudget.category;

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

    @PostMapping
    ResponseEntity<Category> addCategory(@RequestBody CategoryWriteModel dto, @AuthenticationPrincipal Jwt jwt) {

        var userId = UserId.get(jwt).getOrElseThrow(() -> new UserIdNotFound(jwt.getSubject()));

        var category = Category.of(dto, userId.id());

        return ResponseEntity.ok(categoryRepository.save(category));
    }

}
