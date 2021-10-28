package xyz.stasiak.cobudget.entry;

import java.time.LocalDate;

record EntryReadModel(long id, int amount, LocalDate date, String category, String subcategory) {
}
