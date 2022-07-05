package xyz.stasiak.cobudget.plan

import org.springframework.beans.factory.annotation.Autowired
import xyz.stasiak.cobudget.DataIntegrationSpec
import xyz.stasiak.cobudget.common.UserId
import xyz.stasiak.cobudget.plan.exception.PlanNotFound

import java.time.LocalDate
import java.time.Month

class PlanApplicationServiceDataIntegrationSpec extends DataIntegrationSpec {

    @Autowired
    private PlanRepository planRepository

    private PlanApplicationService planApplicationService

    private final UserId USER_ID = new UserId("userId")

    void setup() {
        planApplicationService = new PlanApplicationService(planRepository)
    }

    void cleanup() {
        jdbcTemplate.execute("delete from plan where user_id = 'userId'")
    }

    def "return plan for given user"() {
        given:
        aPlan()

        when:
        def plan = planApplicationService.getPlanFor(USER_ID, LocalDate.of(2022, Month.JULY, 5))

        then:
        !plan.isEmpty()
    }

    def "return amount planned for given user"() {
        given:
        def plan = aPlan()

        when:
        planApplicationService.planCategory(USER_ID, plan.getId(), 2, 300)

        then:
        planApplicationService.getAmountPlannedFor(USER_ID, plan.getId(), 2)
    }

    def "should return read model for plan"() {
        given:
        jdbcTemplate.execute(categoriesSql())
        and:
        def plan = aPlan()
        and:
        planApplicationService.planCategory(USER_ID, plan.getId(), 2, 300)

        when:
        def planReadModel = planApplicationService.readPlan(USER_ID, LocalDate.of(2022, Month.JULY, 5))

        then:
        planReadModel.id() == plan.getId()
        planReadModel.date().getYear() == 2022
        planReadModel.date().month == Month.JULY
        planReadModel.plannedCategories().size() == 1
        with(planReadModel.plannedCategories().get(0)) {
            subcategoryName() == "Home"
            amount() == 300
        }
    }

    def "should throw an exception when read model not found"() {
        when:
        planApplicationService.readPlan(USER_ID, LocalDate.of(2022, Month.JULY, 5))

        then:
        thrown(PlanNotFound)
    }

    private aPlan() {
        planApplicationService.createPlan(USER_ID, LocalDate.of(2022, Month.JULY, 1))
    }

    private static def categoriesSql() {
        // language=SQL
        """
insert into category(id, name, user_id, parent_id) values
(1, 'Food', 'userId', null),
(2, 'Home', 'userId', 1),
(3, 'Clothes', 'userId', null);
"""
    }
}
