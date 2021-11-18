package xyz.stasiak.cobudget.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import xyz.stasiak.cobudget.category.dto.CategoryWriteModel;

record Category(@Id Long id, @JsonIgnore String userId, Long parentId, String name) {

    static Category of(CategoryWriteModel dto, String userId) {
        return new Category(null, userId, dto.parentId(), dto.name());
    }

}
