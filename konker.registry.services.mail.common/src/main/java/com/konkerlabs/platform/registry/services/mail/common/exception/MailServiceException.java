package com.konkerlabs.platform.registry.services.mail.common.exception;

import com.konkerlabs.platform.registry.core.common.exceptions.BusinessException;

public class MailServiceException extends BusinessException {
    public MailServiceException(String message) {
        super(message);
    }

    public MailServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
