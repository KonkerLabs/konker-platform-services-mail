package com.konkerlabs.platform.registry.services.mail.common;

import com.konkerlabs.platform.registry.business.model.User;
import com.konkerlabs.platform.registry.business.model.UserNotification;
import com.konkerlabs.platform.registry.services.mail.common.exception.MailServiceException;

public interface UserMailService extends IMailService {

    enum Errors {
        SYSTEM_ERROR("service.email.system.error");

        public String getCode() {
            return code;
        }

        private String code;

        Errors(String code) {
            this.code = code;
        }
    }

    enum Messages {
        USER_DOES_NOT_EXIST("controller.recover.user.does.not.exist"), USER_EMAIL_SUBJECT(
                "controller.recover.user.email.subject"), USER_CHANGE_PASSWORD_SUCCESS(
                "controller.recover.user.success");

        public String getCode() {
            return code;
        }

        private String code;

        Messages(String code) {
            this.code = code;
        }
    }

    void sendAccountExistsEmail(User user) throws MailServiceException;
    void sendValidateTokenEmail(User user) throws MailServiceException;
    void sendEmailNotification(User user, UserNotification saved) throws MailServiceException;
    void sendUserPasswordRecover(User user) throws MailServiceException;

}
