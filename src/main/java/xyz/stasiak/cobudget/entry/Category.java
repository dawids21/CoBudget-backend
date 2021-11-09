package xyz.stasiak.cobudget.entry;

import org.springframework.data.annotation.Id;

record Category(@Id Long id, String user_id, String name) {
}
