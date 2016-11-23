package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSender;

@Profile("cloud")
@Configuration
public class CloudConfig extends AbstractCloudConfig {
	private final Logger log = LoggerFactory.getLogger(CloudConfig.class);

	@Bean
	MailSender mailSender() {
		try {
			return connectionFactory().service(MailSender.class);
		}
		catch (CloudException e) {
			log.warn("Fallback to use StubMailSender", e);
			return new StubMailSender();
		}
	}

	@Bean
	ConnectionFactory rabbitConnectionFactory() {
		return connectionFactory().rabbitConnectionFactory();
	}
}