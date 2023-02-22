package xyz.stasiak.cobudget.receipt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import xyz.stasiak.cobudget.common.UserId;

import java.time.OffsetDateTime;

record ReceiptImage(
        @Id
        Long id,

        @JsonIgnore
        String userId,

        String key,

        OffsetDateTime submitDate
) {
//    ReceiptImage(UserId userId, String key, OffsetDateTime submitDate) {
//        this(null, userId.id(), key, submitDate);
//    }

    static ReceiptImage of(UserId userId, String key, OffsetDateTime submitDate) {
        return new ReceiptImage(null, userId.id(), key, submitDate);
    }
}
