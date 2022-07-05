package xyz.stasiak.cobudget.plan;


import io.vavr.control.Option;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;

@org.springframework.stereotype.Repository
interface PlanRepository extends Repository<Plan, Long> {
    Plan save(Plan plan);

    @Query("select * from plan where id = :id")
    Option<Plan> findById(Long id);

    @Query("select * from plan where user_id = :userId and extract(year from year_and_month) = :year and  extract(month from year_and_month) = :month")
    Option<Plan> findByUserIdAndYearAndMonth(String userId, int year, int month);

    @Query("delete from plan where id = :id")
    void deleteById(Long id);
}
