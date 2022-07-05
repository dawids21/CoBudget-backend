package xyz.stasiak.cobudget.plan;


import io.vavr.collection.List;
import io.vavr.control.Option;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@org.springframework.stereotype.Repository
interface PlanRepository extends Repository<Plan, Long> {
    Plan save(Plan plan);

    @Query("select * from plan where id = :id")
    Option<Plan> findById(Long id);

    @Query("select * from plan where user_id = :userId and extract(year from year_and_month) = :year and  extract(month from year_and_month) = :month")
    Option<Plan> findByUserIdAndYearAndMonth(String userId, int year, int month);

    @Query("delete from plan where id = :id")
    void deleteById(Long id);

    @Query(resultSetExtractorClass = PlanReadModelExtractor.class, value = "select p.id as id, p.year_and_month as date, c.name as subcategoryName, pc.amount as amount from plan p left join planned_category pc on p.id = pc.plan inner join category c on pc.category_key = c.id where p.user_id = :userId and extract(year from p.year_and_month) = :year and  extract(month from p.year_and_month) = :month")
    Option<PlanReadModel> readPlanByUserIdAndYearAndMonth(String userId, int year, int month);
}

class PlanReadModelExtractor implements ResultSetExtractor<PlanReadModel> {

    @Override
    public PlanReadModel extractData(ResultSet rs) throws SQLException, DataAccessException {
        ArrayList<PlannedCategoryReadModel> plannedCategoryReadModels = new ArrayList<>();
        if (!rs.next()) {
            return null;
        }
        long id = rs.getLong("id");
        Date date = rs.getDate("date");
        String subcategoryName = rs.getString("subcategoryName");
        int amount = rs.getInt("amount");
        plannedCategoryReadModels.add(new PlannedCategoryReadModel(subcategoryName, amount));
        while (rs.next()) {
            subcategoryName = rs.getString("subcategoryName");
            amount = rs.getInt("amount");
            plannedCategoryReadModels.add(new PlannedCategoryReadModel(subcategoryName, amount));
        }
        return new PlanReadModel(id, date.toLocalDate(), List.ofAll(plannedCategoryReadModels));
    }
}
