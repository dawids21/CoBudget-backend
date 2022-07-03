package xyz.stasiak.cobudget.plan;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface PlanRepository extends CrudRepository<Plan, Long> {
}
