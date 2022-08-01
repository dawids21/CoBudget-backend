package xyz.stasiak.cobudget.plan;


import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    @Query(resultSetExtractorClass = PlanReadModelExtractor.class, value = """
            select
                p.id as id, p.year_and_month as date, c.id as categoryId, c.name as categoryName,
                sc.id as subcategoryId, sc.name as subcategoryName, pc.amount as amount
            from plan p
            left join planned_category pc on p.id = pc.plan
            left join category sc on pc.category_key = sc.id
            left join category c on sc.parent_id = c.id
            where p.user_id = :userId and extract(year from p.year_and_month) = :year and  extract(month from p.year_and_month) = :month
            """)
    Option<PlanReadModel> readPlanByUserIdAndYearAndMonth(String userId, int year, int month);
}

class PlanReadModelExtractor implements ResultSetExtractor<PlanReadModel> {

    private static final String ID_COLUMN = "id";
    private static final String DATE_COLUMN = "date";
    private static final String CATEGORY_ID_COLUMN = "categoryId";
    private static final String CATEGORY_NAME_COLUMN = "categoryName";
    private static final String SUBCATEGORY_ID_COLUMN = "subcategoryId";
    private static final String SUBCATEGORY_NAME_COLUMN = "subcategoryName";
    private static final String AMOUNT_COLUMN = "amount";

    @Override
    public PlanReadModel extractData(ResultSet rs) throws SQLException, DataAccessException {
        if (!rs.next()) {
            return null;
        }
        long id = rs.getLong(ID_COLUMN);
        Date date = rs.getDate(DATE_COLUMN);
        if (rs.getString(AMOUNT_COLUMN) == null) {
            return new PlanReadModel(id, date.toLocalDate(), HashSet.empty());
        }
        return new PlanReadModel(id, date.toLocalDate(), mapCategories(rs));
    }

    private Set<PlannedCategoryReadModel> mapCategories(ResultSet rs) throws SQLException {
        record SqlCategory(long id, String name) {
        }
        record SqlSubcategory(long id, String name, int amount) {
        }
        long categoryId = rs.getLong(CATEGORY_ID_COLUMN);
        String categoryName = rs.getString(CATEGORY_NAME_COLUMN);
        long subcategoryId = rs.getLong(SUBCATEGORY_ID_COLUMN);
        String subcategoryName = rs.getString(SUBCATEGORY_NAME_COLUMN);
        int amount = rs.getInt(AMOUNT_COLUMN);
        Map<SqlCategory, Set<SqlSubcategory>> results = HashMap.of(
                new SqlCategory(categoryId, categoryName),
                HashSet.of(new SqlSubcategory(subcategoryId, subcategoryName, amount))
        );
        while (rs.next()) {
            categoryId = rs.getLong(CATEGORY_ID_COLUMN);
            categoryName = rs.getString(CATEGORY_NAME_COLUMN);
            subcategoryId = rs.getLong(SUBCATEGORY_ID_COLUMN);
            subcategoryName = rs.getString(SUBCATEGORY_NAME_COLUMN);
            amount = rs.getInt(AMOUNT_COLUMN);
            SqlCategory sqlCategory = new SqlCategory(categoryId, categoryName);
            results = results.put(
                    sqlCategory,
                    results.get(sqlCategory).getOrElse(HashSet.empty()).add(new SqlSubcategory(subcategoryId, subcategoryName, amount))
            );
        }
        return results.map(tuple -> {
            SqlCategory sqlCategory = tuple._1();
            Set<SqlSubcategory> sqlSubcategory = tuple._2();
            return new PlannedCategoryReadModel(sqlCategory.id(), sqlCategory.name(), sqlSubcategory.map(subcategory -> new PlannedSubcategoryReadModel(subcategory.id(), subcategory.name(), subcategory.amount())));
        }).toSet();
    }
}
