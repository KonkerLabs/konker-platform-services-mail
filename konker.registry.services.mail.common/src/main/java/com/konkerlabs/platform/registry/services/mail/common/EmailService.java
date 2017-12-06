package com.konkerlabs.platform.registry.services.mail.common;

import com.konkerlabs.platform.registry.business.model.User;
import com.konkerlabs.platform.registry.business.services.api.ServiceResponse;
import com.konkerlabs.platform.registry.business.services.api.ServiceResponse.Status;

import java.util.List;
import java.util.Locale;
import java.util.Map;


public interface EmailService {
	
	enum Validations {
        SENDER_NULL("service.email.sender.not_null"),
        RECEIVERS_NULL("service.email.receivers.not_null"),
        SUBJECT_EMPTY("service.email.subject.empty"),
        BODY_EMPTY("service.email.body.empty");

        public String getCode() {
            return code;
        }

        private String code;

        Validations(String code) {
            this.code = code;
        }
    }
	
	ServiceResponse<Status> send(String sender,
                                 List<User> recipients,
                                 List<User> recipientsCopied,
                                 String subject,
                                 String templateName,
                                 Map<String, Object> templateParam,
                                 Locale locale);
}
