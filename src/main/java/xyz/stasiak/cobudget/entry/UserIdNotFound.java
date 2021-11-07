package xyz.stasiak.cobudget.entry;

class UserIdNotFound extends RuntimeException {

    private final String subject;

    public UserIdNotFound(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }
}
