package xyz.stasiak.cobudget.receipt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import xyz.stasiak.cobudget.receipt.exception.CantUploadReceipt;
import xyz.stasiak.cobudget.util.FilenameUtil;

import java.io.IOException;
import java.time.Instant;

@RequiredArgsConstructor
@Slf4j
class ReceiptService {
    private final ReceiptConfigurationProperties receiptConfigurationProperties;

    void uploadFile(MultipartFile receiptFile) {
        try (S3Client s3Client = S3Client.create()) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(receiptConfigurationProperties.bucket())
                    .key(getFilename(receiptFile.getOriginalFilename()))
                    .build();
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(receiptFile.getInputStream(), receiptFile.getSize()));
            log.info("Uploaded receipt");
        } catch (IOException e) {
            log.error("Problem with uploading receipt file", e);
            throw new CantUploadReceipt(e);
        }
    }

    private String getFilename(String original) {
        String extension = FilenameUtil
                .getExtension(original)
                .map(e -> "." + e)
                .orElse("");
        return String.format("receipt-%d%s", Instant.now().toEpochMilli(), extension);
    }
}
