package xyz.stasiak.cobudget.receipt;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.Instant;

@RestController
@RequestMapping("/receipt")
class ReceiptController {
    private static final String BUCKET = "cobudget-eu-central-1-receipts";

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadFile(@RequestParam MultipartFile receiptFile) {

        try (S3Client s3Client = S3Client.create()) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET)
                    .key(String.format("receipt-%d", Instant.now().toEpochMilli()))
                    .build();
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(receiptFile.getInputStream(), receiptFile.getSize()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
