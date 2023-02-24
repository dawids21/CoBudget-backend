package xyz.stasiak.cobudget.featuretoggle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;

@RequiredArgsConstructor
@Slf4j
public class FeatureToggleService {
    private final FeatureToggleRepository featureToggleRepository;

    private static void logFeatureNotFound(String featureName) {
        log.warn("No toggle for feature {} found in database", featureName);
    }

    @PreAuthorize("hasRole('admin')")
    public boolean isFeatureEnabled(String featureName) {
        return featureToggleRepository.findByName(featureName)
                .map(FeatureToggle::enabled)
                .orElseGet(() -> {
                    logFeatureNotFound(featureName);
                    return false;
                });
    }

    @PreAuthorize("hasRole('admin')")
    public void enableFeature(String featureName) {
        featureToggleRepository.findByName(featureName)
                .map(FeatureToggle::enable)
                .ifPresentOrElse(featureToggleRepository::save, () -> logFeatureNotFound(featureName));
    }

    @PreAuthorize("hasRole('admin')")
    public void disableFeature(String featureName) {
        featureToggleRepository.findByName(featureName)
                .map(FeatureToggle::disable)
                .ifPresentOrElse(featureToggleRepository::save, () -> logFeatureNotFound(featureName));
    }
}
