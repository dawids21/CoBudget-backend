package xyz.stasiak.cobudget.receipt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.stasiak.cobudget.receipt.exception.CantUploadReceipt;

@RestController
@RequestMapping("/receipt")
@RequiredArgsConstructor
@Slf4j
class ReceiptController {
    private final ReceiptService receiptService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadFile(@RequestParam MultipartFile receiptFile) {
        receiptService.uploadFile(receiptFile);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(CantUploadReceipt.class)
    ProblemDetail cantUploadReceiptHandler(CantUploadReceipt exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage());
        problemDetail.setTitle("Problem with receipt upload");
        return problemDetail;
    }
}
