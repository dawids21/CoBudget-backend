package xyz.stasiak.cobudget.plan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.vavr.collection.Map;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
class Plan {

    @Id
    private Long id;

    @JsonIgnore
    private String userId;

    private String month;
    
    private Map<Long, PlannedCategory> categories;
}
