package com.konkerlabs.platform.registry.services.mail.common.exception;

import com.konkerlabs.platform.registry.business.exceptions.BusinessException;


public class MailServiceException extends BusinessException {
    public MailServiceException(String message) {
        super(message);
    }

    public MailServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
