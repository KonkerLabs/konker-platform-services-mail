package com.konkerlabs.platform.registry.services.mail.service.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.Data;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Data
public class MailServiceSmtpConfig {
	
	private String host;
	private String port;
	private String protocol;
	private String username;
	private String password;
	private String baseurl;
	private String sender;
	private boolean enabled;
	
	public MailServiceSmtpConfig() {
		Map<String, Object> defaultMap = new HashMap<>();
		defaultMap.put("email.host", "email-smtp.us-east-1.amazonaws.com");
		defaultMap.put("email.port", "587");
		defaultMap.put("email.protocol", "smtp");
		defaultMap.put("email.username", "username");
		defaultMap.put("email.password", "pass");
		defaultMap.put("email.baseurl", "http://localhost:8080/");
		defaultMap.put("email.sender", "no-reply@konkerlabs.com");
		defaultMap.put("email.enabled", false);
		Config defaultConf = ConfigFactory.parseMap(defaultMap);

		Config config = ConfigFactory.load().withFallback(defaultConf);
		setHost(config.getString("email.host"));
		setPort(config.getString("email.port"));
		setProtocol(config.getString("email.protocol"));
		setUsername(config.getString("email.username"));
		setPassword(config.getString("email.password"));
		setBaseurl(config.getString("email.baseurl"));
		setSender(config.getString("email.sender"));
		setEnabled(config.getBoolean("email.enabled"));
	}
}
