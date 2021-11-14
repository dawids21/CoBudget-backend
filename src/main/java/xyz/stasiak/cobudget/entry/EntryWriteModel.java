package xyz.stasiak.cobudget.entry;

import java.time.LocalDate;

record EntryWriteModel(int amount, LocalDate date, Long categoryId) {
}
