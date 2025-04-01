package org.demo.atiperataskspring.exception;

public record ApiErrorResponse(
        int status,
        String message
) {
}
