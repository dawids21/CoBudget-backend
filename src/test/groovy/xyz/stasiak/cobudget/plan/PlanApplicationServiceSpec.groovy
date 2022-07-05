package xyz.stasiak.cobudget.plan

import io.vavr.control.Option
import spock.lang.Specification
import xyz.stasiak.cobudget.common.UserId
import xyz.stasiak.cobudget.plan.exception.PlanNotFound

import java.time.LocalDate
import java.time.Month
import java.util.concurrent.atomic.AtomicLong

class PlanApplicationServiceSpec extends Specification {

    final def planApplicationService = new PlanApplicationService(new TestPlanRepository())
    final static def CATEGORY_ID = 2
    private static final UserId USER_ID = new UserId("user")

    def "should create plan for new month"() {
        given:
        aPlan()

        expect:
        def plan = planApplicationService.getPlanFor(USER_ID, LocalDate.of(2022, Month.JULY, 1))
        !plan.isEmpty()
    }

    def "should return empty option when plan is not created"() {
        expect:
        def plan = planApplicationService.getPlanFor(USER_ID, LocalDate.of(2022, Month.JULY, 1))
        plan.isEmpty()
    }

    def "should add plan for given category"() {
        given:
        def plan = aPlan()

        when:
        planApplicationService.planCategory(plan.getId(), CATEGORY_ID, 300)

        then:
        def amount = planApplicationService.getAmountPlannedFor(plan.getId(), CATEGORY_ID)
        amount == 300
    }

    def "should throw an exception when plan does not exists"() {
        when:
        planApplicationService.planCategory(33L, CATEGORY_ID, 300)

        then:
        thrown(PlanNotFound)
    }

    def "should return 0 when category is not planned"() {
        given:
        def plan = aPlan()

        expect:
        planApplicationService.getAmountPlannedFor(plan.getId(), 3) == 0
    }

    def "should be able to change plan for given category"() {
        given:
        def plan = aPlanWithCategoryPlanned()

        when:
        planApplicationService.changeCategoryPlan(plan.getId(), CATEGORY_ID, 100)

        then:
        planApplicationService.getAmountPlannedFor(plan.getId(), CATEGORY_ID) == 100
    }

    def "should be able to delete plan"() {
        given:
        def plan = aPlan()

        when:
        planApplicationService.deletePlan(plan.getId())

        then:
        planApplicationService.getPlanFor(USER_ID, LocalDate.of(2022, Month.JULY, 1)).isEmpty()
    }

    def "should delete plan for category"() {
        given:
        def plan = aPlanWithCategoryPlanned()

        when:
        planApplicationService.deletePlannedCategory(plan.getId(), CATEGORY_ID)

        then:
        planApplicationService.getAmountPlannedFor(plan.getId(), CATEGORY_ID) == 0
    }

    def "should pass deleting plan for non existing category"() {
        given:
        def plan = aPlan()

        when:
        planApplicationService.deletePlannedCategory(plan.getId(), CATEGORY_ID)

        then:
        noExceptionThrown()
    }

    private aPlan() {
        planApplicationService.createPlan(USER_ID, LocalDate.of(2022, Month.JULY, 1))
    }

    private aPlanWithCategoryPlanned() {
        def plan = planApplicationService.createPlan(USER_ID, LocalDate.of(2022, Month.JULY, 1))
        planApplicationService.planCategory(plan.getId(), CATEGORY_ID, 300)
        return plan
    }

    private static class TestPlanRepository implements PlanRepository {

        private final Map<Long, Plan> plans = new HashMap<>()
        private final AtomicLong id = new AtomicLong(1L)

        @Override
        Plan save(Plan plan) {
            plan.setId(id.getAndIncrement())
            plans.put(plan.getId(), plan)
            return plans.get(plan.getId())
        }

        @Override
        Option<Plan> findById(Long id) {
            return Option.of(plans.get(id))
        }

        @Override
        Option<Plan> findByUserIdAndYearAndMonth(String userId, int year, int month) {
            return Option.ofOptional(
                    plans.values()
                            .stream()
                            .filter(plan -> plan.getUserId() == userId)
                            .filter(plan -> plan.getYearAndMonth().getYear() == year)
                            .filter(plan -> plan.getYearAndMonth().getMonthValue() == month)
                            .findAny()
            )
        }

        @Override
        void deleteById(Long id) {
            plans.remove(id)
        }

        @Override
        Option<PlanReadModel> readPlanByUserIdAndYearAndMonth(String userId, int year, int month) {
            return Option.none()
        }
    }
}
