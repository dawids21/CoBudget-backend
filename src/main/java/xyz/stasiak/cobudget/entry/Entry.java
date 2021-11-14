package xyz.stasiak.cobudget.entry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

import java.time.LocalDate;

record Entry(
        @Id
        Long id,

        @JsonIgnore
        String userId,

        int amount,
        LocalDate date,

        @Embedded.Nullable(prefix = "category_")
        CategoryId category
) {

    static Entry of(EntryWriteModel dto, String userId) {
        return new Entry(null, userId, dto.amount(), dto.date(), new CategoryId(dto.categoryId()));
    }

}
