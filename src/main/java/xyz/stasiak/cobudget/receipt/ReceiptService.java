package xyz.stasiak.cobudget.receipt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import veryfi.Client;
import veryfi.VeryfiClientFactory;
import xyz.stasiak.cobudget.common.UserId;
import xyz.stasiak.cobudget.receipt.exception.CantUploadReceipt;
import xyz.stasiak.cobudget.util.FilenameUtil;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@PreAuthorize("@featureToggleService.isFeatureEnabled('receipts-scanning', authentication)")
class ReceiptService {
    private final ReceiptConfigurationProperties receiptConfigurationProperties;
    private final ReceiptImageRepository receiptImageRepository;

    @Value("${clientId}")
    private String clientId ;
    @Value("${clientSecret}")
    private String clientSecret;
    @Value("${username}")
    private String username;
    @Value("${apiKey}")
    private String apiKey;

    @Transactional
    public ReceiptImage saveReceiptImage(MultipartFile receiptFile, UserId userId) {
        ReceiptImage receiptImage = uploadFile(receiptFile, userId);
        return receiptImageRepository.save(receiptImage);
    }

    public String getDataFromReceipt(String fileUrl) {
        Client client = VeryfiClientFactory.createClient(clientId, clientSecret, username, apiKey);
        return client.processDocumentUrl(fileUrl, Collections.emptyList(), Collections.emptyList(), false, 10, false, null, null);
    }

    public Receipt mapJsonToReceipt(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(json);
            String date = root.get("date").asText().split(" ")[0];
            int total = (int) (root.get("total").asDouble() * 100);
            JsonNode lineItemsNode = root.get("line_items");
            List<LineItem> lineItems = new ArrayList<>();
            for (JsonNode itemNode : lineItemsNode) {
                String description = itemNode.get("description").asText();
                int order = itemNode.get("order").asInt();
                int total_product = (int) (itemNode.get("total").asDouble() * 100);
                LineItem item = LineItem.builder().order(order).description(description).total(total_product).build();
                lineItems.add(item);
            }
            return Receipt.builder()
                    .date(date)
                    .total(total)
                    .lineItems(lineItems)
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }


    private ReceiptImage uploadFile(MultipartFile receiptFile, UserId userId) {
        try (S3Client s3Client = S3Client.create()) {
//            String uuid = UUID.randomUUID().toString().substring(0,8);
            String key = getKey(receiptFile.getOriginalFilename());
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(receiptConfigurationProperties.bucket())
                    .key(key)
                    .build();
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(receiptFile.getInputStream(), receiptFile.getSize()));
            log.info("Uploaded receipt {}", key);
            String fileUrl = "https://cobudget-eu-central-1-receipts.s3.eu-central-1.amazonaws.com/" + key;
            return ReceiptImage.of(userId, fileUrl, OffsetDateTime.now(ZoneOffset.UTC));
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
