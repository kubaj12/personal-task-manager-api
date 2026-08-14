package io.github.kubaj12.personal_task_manager_api.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    private final String email;
    
    public EmailAlreadyExistsException(String email) {
        super("Email '" + email + "' already exists.");
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
