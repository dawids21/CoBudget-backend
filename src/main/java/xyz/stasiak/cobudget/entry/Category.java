package xyz.stasiak.cobudget.entry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

record Category(@Id Long id, @JsonIgnore String user_id, String name) {

    static Category of(CategoryWriteModel dto, String userId) {
        return new Category(null, userId, dto.name());
    }

}
