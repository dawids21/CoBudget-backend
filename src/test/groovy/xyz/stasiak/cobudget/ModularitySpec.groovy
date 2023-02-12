package xyz.stasiak.cobudget


import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.docs.Documenter
import spock.lang.Specification

class ModularitySpec extends Specification {
    private final ApplicationModules modules = ApplicationModules.of(BackendApplication)

    def "verify structure"() {
        expect:
        modules.verify()
    }

    def "create documentation"() {
        given:
        def documenter = new Documenter(modules)
        expect:
        documenter.writeDocumentation()
    }
}
