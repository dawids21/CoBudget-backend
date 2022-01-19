package xyz.stasiak.cobudget.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
class Category {
    @Id
    private Long id;
    @JsonIgnore
    private String userId;
    private Long parentId;
    private String name;
    private boolean disabled;

    Category(Long id, String userId, Long parentId, String name, boolean disabled) {
        this.id = id;
        this.userId = userId;
        this.parentId = parentId;
        this.name = name;
        this.disabled = disabled;
    }

    static Category of(CategoryWriteModel dto, String userId) {
        return new Category(null, userId, dto.parentId(), dto.name(), false);
    }

}
