package xyz.stasiak.cobudget.entry;

import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

import java.time.LocalDate;

record Entry(@Id Long id, int amount, LocalDate date, AggregateReference<Category, Long> category, String subcategory) {

}
