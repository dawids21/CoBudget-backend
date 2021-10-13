package xyz.stasiak.cobudget.expense;

import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
interface Expenses extends Repository<Expense, Long> {
}
