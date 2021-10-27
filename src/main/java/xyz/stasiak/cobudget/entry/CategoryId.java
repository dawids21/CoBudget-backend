package xyz.stasiak.cobudget.entry;

import org.springframework.data.relational.core.mapping.Column;

record CategoryId(@Column("category") Long id) {
}