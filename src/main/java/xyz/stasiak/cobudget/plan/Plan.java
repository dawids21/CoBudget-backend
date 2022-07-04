package xyz.stasiak.cobudget.plan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.Map;

@Data
class Plan {

    @Id
    private Long id;

    @JsonIgnore
    private String userId;
    private String month;

    private Map<Long, PlannedCategory> categories = new HashMap<>();

    Plan(Long id, String userId, String month) {
        this.id = id;
        this.userId = userId;
        this.month = month;
    }
}
