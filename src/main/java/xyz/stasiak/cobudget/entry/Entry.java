package xyz.stasiak.cobudget.entry;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

import java.time.LocalDate;

record Entry(
        @Id
        Long id,

        int amount,
        LocalDate date,

        @Embedded.Nullable(prefix = "category_")
        CategoryId category,

        String subcategory
) {

    static Entry of(EntryWriteModel dto) {
        return new Entry(null, dto.amount(), dto.date(), new CategoryId(dto.categoryId()), dto.subcategory());
    }

}
