package xyz.stasiak.cobudget.expense;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.util.Set;

@RepositoryRestResource
interface ExpenseRepository extends Repository<Expense, Long> {

    Expense save(Expense expense);

    @Query("select * from expense where date >= :start and date <= :end")
    Set<Expense> findByDateBetween(LocalDate start, LocalDate end);
}
