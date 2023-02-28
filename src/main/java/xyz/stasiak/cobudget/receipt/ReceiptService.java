package xyz.stasiak.cobudget.receipt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import xyz.stasiak.cobudget.common.UserId;
import xyz.stasiak.cobudget.receipt.exception.CantUploadReceipt;
import xyz.stasiak.cobudget.util.FilenameUtil;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@RequiredArgsConstructor
@Slf4j
@PreAuthorize("@featureToggleService.isFeatureEnabled('receipts-scanning', authentication)")
class ReceiptService {
    private final ReceiptConfigurationProperties receiptConfigurationProperties;
    private final ReceiptImageRepository receiptImageRepository;

    @Transactional
    ReceiptImage saveReceiptImage(MultipartFile receiptFile, UserId userId) {
        ReceiptImage receiptImage = uploadFile(receiptFile, userId);
        return receiptImageRepository.save(receiptImage);
    }

    private ReceiptImage uploadFile(MultipartFile receiptFile, UserId userId) {
        try (S3Client s3Client = S3Client.create()) {
            String key = getKey(receiptFile.getOriginalFilename());
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(receiptConfigurationProperties.bucket())
                    .key(key)
                    .build();
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(receiptFile.getInputStream(), receiptFile.getSize()));
            log.info("Uploaded receipt {}", key);
            return ReceiptImage.of(userId, key, OffsetDateTime.now(ZoneOffset.UTC));
        } catch (IOException | SdkException e) {
            log.error("Problem with uploading receipt file", e);
            throw new CantUploadReceipt(e);
        }
    }

    private String getKey(String original) {
        String extension = FilenameUtil
                .getExtension(original)
                .map(e -> "." + e)
                .orElse("");
        return String.format("receipt-%d%s", Instant.now().toEpochMilli(), extension);
    }
}
