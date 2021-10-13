package xyz.stasiak.cobudget.expense;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;

record Expense(@Id Long id, int amount, LocalDate date, String category, String subcategory) {

}
