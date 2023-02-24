package xyz.stasiak.cobudget.featuretoggle;

import org.springframework.data.repository.Repository;

import java.util.Optional;

@org.springframework.stereotype.Repository
interface FeatureToggleRepository extends Repository<FeatureToggle, Long> {
    void save(FeatureToggle featureToggle);

    Optional<FeatureToggle> findByName(String name);
}
