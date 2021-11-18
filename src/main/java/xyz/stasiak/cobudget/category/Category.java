package xyz.stasiak.cobudget.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

record Category(@Id Long id, @JsonIgnore String userId, Long parentId, String name) {

    static Category of(CategoryWriteModel dto, String userId) {
        return new Category(null, userId, dto.parentId(), dto.name());
    }

}
