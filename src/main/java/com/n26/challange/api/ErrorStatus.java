package com.n26.challange.api;

class ErrorStatus extends Status {

    private final String message;

    public ErrorStatus(String message) {
        super("error");
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
