package xyz.stasiak.cobudget.receipt;

class ReceiptException extends RuntimeException {
    private ReceiptException(String message, Throwable cause) {
        super(message, cause);
    }

    static ReceiptException becauseCantUploadFile(Throwable cause) {
        return new ReceiptException("Can't upload receipt file to S3", cause);
    }
}
