package xyz.stasiak.cobudget.receipt;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class Receipt {

    private String date;

    private List<LineItem> lineItems;

    private int total;
}
