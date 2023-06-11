package xyz.stasiak.cobudget.receipt;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LineItem {

    private String description;

    private int order;

    private int total;

}
