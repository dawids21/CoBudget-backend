package xyz.stasiak.cobudget.entry;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;

record Entry(@Id Long id, int amount, LocalDate date, String category, String subcategory) {

}
