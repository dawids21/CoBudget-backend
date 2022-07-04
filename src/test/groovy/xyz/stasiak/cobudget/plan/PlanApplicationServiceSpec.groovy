package xyz.stasiak.cobudget.plan

import io.vavr.control.Option
import spock.lang.Specification

import java.time.LocalDate
import java.time.Month

class PlanApplicationServiceSpec extends Specification {

    final def planApplicationService = new PlanApplicationService(new TestPlanRepository())

    def "should create plan for new month"() {
        given:
        aPlan()

        expect:
        def plan = planApplicationService.getPlanFor("user", LocalDate.of(2022, Month.JULY, 1))
        !plan.isEmpty()
    }
    
    def "should return empty option when plan is not created"() {
        expect:
        def plan = planApplicationService.getPlanFor("user", LocalDate.of(2022, Month.JULY, 1))
        plan.isEmpty()
    }

    def "should add plan for given category"() {
        given:
        def plan = aPlan()

        when:
        planApplicationService.planCategory(plan.getId(), 2L, 300)

        then:
        def amount = planApplicationService.getAmountPlannedFor(plan.getId(), 2L)
        amount == 300
    }

    def "should throw an exception when plan does not exists"() {
        when:
        planApplicationService.planCategory(33L, 2L, 300)

        then:
        thrown(IllegalArgumentException)
    }

    def "should return 0 when category is not planned"() {
        given:
        def plan = aPlan()

        expect:
        planApplicationService.getAmountPlannedFor(plan.getId(), 3L) == 0
    }

    private aPlan() {
        planApplicationService.createPlan("user", LocalDate.of(2022, Month.JULY, 1))
    }

    private static class TestPlanRepository implements PlanRepository {

        private final Map<Long, Plan> plans = new HashMap<>()

        @Override
        Plan save(Plan plan) {
            plan.setId(plans.keySet().size() + 1)
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
    }
}
