package com.konkerlabs.platform.registry.services.mail.service.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

@Configuration
public class MailServiceSpringMailConfig implements ApplicationContextAware, EnvironmentAware {
	
	@Autowired
	private MailServiceSmtpConfig mailServiceSmtpConfig;
	
	private static final String JAVA_MAIL_FILE = "classpath:mail/javamail.properties";
	
	private ApplicationContext applicationContext;
	private Environment environment;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@Bean
	public JavaMailSender mailSender() throws IOException {
		final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		
		mailSender.setHost(mailServiceSmtpConfig.getHost());
		mailSender.setPort(Integer.parseInt(mailServiceSmtpConfig.getPort()));
		mailSender.setProtocol(mailServiceSmtpConfig.getProtocol());
		mailSender.setUsername(mailServiceSmtpConfig.getUsername());
		mailSender.setPassword(mailServiceSmtpConfig.getPassword());
		
		final Properties javaMailProperties = new Properties();
		javaMailProperties.load(this.applicationContext.getResource(JAVA_MAIL_FILE).getInputStream());
		mailSender.setJavaMailProperties(javaMailProperties);
		
		return mailSender;
	}
	
	@Bean
	public SpringTemplateEngine emailTemplateEngine(MessageSource messageSource) {
		final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(textTemplateResolver());
        templateEngine.addTemplateResolver(htmlTemplateResolver());
        templateEngine.addTemplateResolver(stringTemplateResolver());
        templateEngine.setTemplateEngineMessageSource(messageSource);
        return templateEngine;
	}

	private ITemplateResolver textTemplateResolver() {
		final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setOrder(1);
		templateResolver.setResolvablePatterns(Collections.singleton("text/*"));
		templateResolver.setPrefix("/mail/");
		templateResolver.setSuffix(".txt");
		templateResolver.setTemplateMode(TemplateMode.TEXT);
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setCacheable(false);
		return templateResolver;
	}

	private ITemplateResolver htmlTemplateResolver() {
		final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setOrder(2);
		templateResolver.setResolvablePatterns(Collections.singleton("html/*"));
		templateResolver.setPrefix("/mail/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setCacheable(false);
		return templateResolver;
	}

	private ITemplateResolver stringTemplateResolver() {
		final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setOrder(3);
		templateResolver.setTemplateMode(TemplateMode.HTML5);
		templateResolver.setCacheable(false);
		return templateResolver;
	}

}
