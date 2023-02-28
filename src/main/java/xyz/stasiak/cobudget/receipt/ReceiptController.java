package xyz.stasiak.cobudget.receipt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.stasiak.cobudget.common.UserId;
import xyz.stasiak.cobudget.featuretoggle.FeatureToggleService;
import xyz.stasiak.cobudget.receipt.exception.CantUploadReceipt;

import java.security.Principal;

@RestController
@RequestMapping("/receipt")
@RequiredArgsConstructor
@Slf4j
class ReceiptController {
    private static final String FEATURE_NAME = "receipts-scanning";
    private final ReceiptService receiptService;
    private final FeatureToggleService featureToggleService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReceiptImage> uploadFile(@RequestParam MultipartFile receiptFile, Principal principal) {
        UserId userId = new UserId(principal.getName());
        ReceiptImage receiptImage = receiptService.saveReceiptImage(receiptFile, userId);
        return ResponseEntity.ok(receiptImage);
    }

    @GetMapping("/is-enabled")
    public IsEnabledResponse isFeatureEnabled(Authentication authentication) {
        boolean featureEnabled = featureToggleService.isFeatureEnabled(FEATURE_NAME, authentication);
        return new IsEnabledResponse(featureEnabled);
    }

    @PostMapping("/enable")
    public void enableFeature() {
        featureToggleService.enableFeature(FEATURE_NAME);
    }

    @PostMapping("/disable")
    public void disableFeature() {
        featureToggleService.disableFeature(FEATURE_NAME);
    }

    @ExceptionHandler(CantUploadReceipt.class)
    ProblemDetail cantUploadReceiptHandler(CantUploadReceipt exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage());
        problemDetail.setTitle("Problem with receipt upload");
        return problemDetail;
    }

    private record IsEnabledResponse(boolean enabled) {
    }
}
