package xyz.stasiak.cobudget.featuretoggle;

import org.springframework.data.annotation.Id;

record FeatureToggle(
        @Id
        Long id,
        String name,
        boolean enabled
) {
    FeatureToggle enable() {
        return new FeatureToggle(id, name, true);
    }

    FeatureToggle disable() {
        return new FeatureToggle(id, name, false);
    }
}
