package xyz.stasiak.cobudget.plan;


import io.vavr.collection.List;
import io.vavr.control.Option;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@org.springframework.stereotype.Repository
interface PlanRepository extends Repository<Plan, Long> {
    @Modifying
    Plan save(Plan plan);

    @Query("select * from plan where id = :id and user_id = :userId")
    Option<Plan> findByIdAndUserId(Long id, String userId);

    @Query("select * from plan where user_id = :userId and extract(year from year_and_month) = :year and  extract(month from year_and_month) = :month")
    Option<Plan> findByUserIdAndYearAndMonth(String userId, int year, int month);

    @Query("delete from plan where id = :id and user_id = :userId")
    @Modifying
    void deleteById(Long id, String userId);

    @Query(resultSetExtractorClass = PlanReadModelExtractor.class, value = "select p.id as id, p.year_and_month as date, c.name as subcategoryName, pc.amount as amount from plan p left join planned_category pc on p.id = pc.plan left join category c on pc.category_key = c.id where p.user_id = :userId and extract(year from p.year_and_month) = :year and  extract(month from p.year_and_month) = :month")
    Option<PlanReadModel> readPlanByUserIdAndYearAndMonth(String userId, int year, int month);
}

class PlanReadModelExtractor implements ResultSetExtractor<PlanReadModel> {

    private static final String ID_COLUMN = "id";
    private static final String DATE_COLUMN = "date";
    private static final String SUBCATEGORY_NAME_COLUMN = "subcategoryName";
    private static final String AMOUNT_COLUMN = "amount";

    @Override
    public PlanReadModel extractData(ResultSet rs) throws SQLException, DataAccessException {
        ArrayList<PlannedCategoryReadModel> plannedCategoryReadModels = new ArrayList<>();
        if (!rs.next()) {
            return null;
        }
        long id = rs.getLong(ID_COLUMN);
        Date date = rs.getDate(DATE_COLUMN);
        if (rs.getString(SUBCATEGORY_NAME_COLUMN) == null) {
            return new PlanReadModel(id, date.toLocalDate(), List.of());
        }
        String subcategoryName = rs.getString(SUBCATEGORY_NAME_COLUMN);
        int amount = rs.getInt(AMOUNT_COLUMN);
        plannedCategoryReadModels.add(new PlannedCategoryReadModel(subcategoryName, amount));
        while (rs.next()) {
            subcategoryName = rs.getString(SUBCATEGORY_NAME_COLUMN);
            amount = rs.getInt(AMOUNT_COLUMN);
            plannedCategoryReadModels.add(new PlannedCategoryReadModel(subcategoryName, amount));
        }
        return new PlanReadModel(id, date.toLocalDate(), List.ofAll(plannedCategoryReadModels));
    }
}
