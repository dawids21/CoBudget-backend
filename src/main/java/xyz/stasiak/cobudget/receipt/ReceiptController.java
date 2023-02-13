package xyz.stasiak.cobudget.receipt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/receipt")
@RequiredArgsConstructor
@Slf4j
class ReceiptController {
    private final ReceiptService receiptService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadFile(@RequestParam MultipartFile receiptFile) {
        try {
            receiptService.uploadFile(receiptFile);
            return ResponseEntity.ok().build();
        } catch (ReceiptException e) {
            log.error("Problem with uploading receipt file", e);
            throw e;
        }
    }
}
