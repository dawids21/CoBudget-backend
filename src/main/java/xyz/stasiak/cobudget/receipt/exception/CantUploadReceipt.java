package xyz.stasiak.cobudget.receipt.exception;

public class CantUploadReceipt extends RuntimeException {
    public CantUploadReceipt(Throwable cause) {
        super("Can't upload receipt file to S3", cause);
    }
}
