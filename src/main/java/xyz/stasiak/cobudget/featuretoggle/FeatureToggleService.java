package xyz.stasiak.cobudget.featuretoggle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import xyz.stasiak.cobudget.security.SecurityService;

@RequiredArgsConstructor
@Slf4j
public class FeatureToggleService {
    private final FeatureToggleRepository featureToggleRepository;
    private final SecurityService securityService;

    private static void logFeatureNotFound(String featureName) {
        log.warn("No toggle for feature {} found in database", featureName);
    }

    public boolean isFeatureEnabled(String featureName, Authentication authentication) {
        if (securityService.isAdmin(authentication)) {
            return true;
        }
        return featureToggleRepository.findByName(featureName)
                .map(FeatureToggle::enabled)
                .orElseGet(() -> {
                    logFeatureNotFound(featureName);
                    return false;
                });
    }

    @PreAuthorize("@securityService.isAdmin(authentication)")
    public void enableFeature(String featureName) {
        featureToggleRepository.findByName(featureName)
                .map(FeatureToggle::enable)
                .ifPresentOrElse(featureToggleRepository::save, () -> logFeatureNotFound(featureName));
    }

    @PreAuthorize("@securityService.isAdmin(authentication)")
    public void disableFeature(String featureName) {
        featureToggleRepository.findByName(featureName)
                .map(FeatureToggle::disable)
                .ifPresentOrElse(featureToggleRepository::save, () -> logFeatureNotFound(featureName));
    }
}
