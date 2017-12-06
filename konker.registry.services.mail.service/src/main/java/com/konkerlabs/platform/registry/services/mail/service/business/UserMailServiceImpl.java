package com.konkerlabs.platform.registry.services.mail.service.business;


import com.konkerlabs.platform.registry.business.exceptions.BusinessException;
import com.konkerlabs.platform.registry.business.model.User;
import com.konkerlabs.platform.registry.business.model.UserNotification;
import com.konkerlabs.platform.registry.business.services.api.ServiceResponse;
import com.konkerlabs.platform.registry.business.services.api.TokenService;
import com.konkerlabs.platform.registry.business.services.api.UserService;
import com.konkerlabs.platform.registry.services.mail.common.EmailService;
import com.konkerlabs.platform.registry.services.mail.common.UserMailService;
import com.konkerlabs.platform.registry.services.mail.common.exception.MailServiceException;
import com.konkerlabs.platform.registry.services.mail.service.config.EmailConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserMailServiceImpl implements UserMailService {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private EmailConfig emailConfig;
    @Autowired
    private EmailService emailService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private Environment environment;

    private Logger LOGGER = LoggerFactory.getLogger(UserMailServiceImpl.class);


    @Override
    public void sendAccountExistsEmail(User user) throws MailServiceException {
        Map<String, Object> templateParam = new HashMap<>();
        try {
            templateParam.put("link", emailConfig.getBaseurl().concat("subscription/")
                    .concat(randomToken(user, TokenService.Purpose.VALIDATE_EMAIL)));
            templateParam.put("name", user.getName());
            sendMail(user, templateParam, UserService.Messages.USER_SUBJECT_MAIL,
                    "html/email-selfsubscription");
        } catch (BusinessException e) {
            throw new MailServiceException(e.getMessage());
        }
    }

    @Override
    public void sendValidateTokenEmail(User user) throws MailServiceException {
        Map<String, Object> templateParam = new HashMap<>();
        try {
            templateParam.put("link", emailConfig.getBaseurl()
                    .concat("subscription/")
                    .concat(randomToken(user, TokenService.Purpose.VALIDATE_EMAIL)));
            templateParam.put("name", user.getName());
            sendMail(user, templateParam, UserService.Messages.USER_SUBJECT_MAIL,
                    "html/email-selfsubscription");
        } catch (BusinessException e) {
            throw new MailServiceException(e.getMessage());
        }
    }

    @Override
    public void sendEmailNotification(User user, UserNotification saved) throws MailServiceException {
        List<String> profiles = Arrays.stream(environment.getActiveProfiles()).collect(Collectors.toList());

        if (user.isNotificationViaEmail() && profiles.contains("email")) {
            Map<String, Object> templateParam = new HashMap<>();
            templateParam.put("name", user.getName());
            templateParam.put("body", saved.getBody());
            sendMail(user, templateParam, saved.getSubject(),
                    "html/email-notification");
            LOGGER.info("E-mail sent: {}", saved.getSubject());
        } else {
            throw new MailServiceException(Errors.SYSTEM_ERROR.getCode());
        }
    }

    @Override
    public void sendUserPasswordRecover(User user) throws MailServiceException {
        ServiceResponse<String> responseToken = tokenService.generateToken(TokenService.Purpose.RESET_PASSWORD,
                user, Duration.ofMinutes(15));

        Map<String, Object> templateParam = new HashMap<>();
        templateParam.put("link",
                emailConfig.getBaseurl().concat("recoverpassword/").concat(responseToken.getResult()));
        templateParam.put("name", Optional.ofNullable(user.getName()).orElse(""));

        sendMail(user, templateParam, UserMailService.Messages.USER_EMAIL_SUBJECT.getCode(),
                "html/email-recover-pass");
    }


    private String randomToken(User user, TokenService.Purpose purpose) throws BusinessException {
        ServiceResponse<String> responseToken = tokenService.generateToken(
                purpose,
                user,
                Duration.ofDays(2L));
        if(responseToken.isOk()) {
            return responseToken.getResult();
        } else {
            throw new BusinessException(TokenService.Validations.INVALID_TOKEN.getCode());
        }
    }

    private void sendMail(User user, Map<String, Object> templateParam, String subject, String templateName) {
        emailService.send(
                emailConfig.getSender(),
                Collections.singletonList(user),
                Collections.emptyList(),
                subject,
                templateName,
                templateParam,
                user.getLanguage().getLocale());
    }

    private void sendMail(User user, Map<String, Object> templateParam, UserService.Messages message, String templateName) {
        emailService.send(
                emailConfig.getSender(),
                Collections.singletonList(user),
                Collections.emptyList(),
                messageSource.getMessage(message.getCode(), null, user.getLanguage().getLocale()),
                templateName,
                templateParam,
                user.getLanguage().getLocale());
    }
}
