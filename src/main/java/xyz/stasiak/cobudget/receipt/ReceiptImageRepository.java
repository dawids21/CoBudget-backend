package xyz.stasiak.cobudget.receipt;

import org.springframework.data.repository.Repository;

@org.springframework.stereotype.Repository
interface ReceiptImageRepository extends Repository<ReceiptImage, Long> {
    ReceiptImage save(ReceiptImage receiptImage);
}
