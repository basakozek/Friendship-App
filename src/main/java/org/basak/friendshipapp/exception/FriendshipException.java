package org.basak.friendshipapp.exception;

public class FriendshipException extends RuntimeException {
    private ErrorType errorType;
    public FriendshipException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
    public ErrorType getErrorType() {
        return errorType;
    }
}
