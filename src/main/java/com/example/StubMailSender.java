package com.example;

import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Profile("!cloud")
@Component
public class StubMailSender implements MailSender {
	private final Logger log = LoggerFactory.getLogger(StubMailSender.class);

	@Override
	public void send(SimpleMailMessage simpleMailMessage) throws MailException {
		log.info("send {}", simpleMailMessage);
	}

	@Override
	public void send(SimpleMailMessage... simpleMailMessages) throws MailException {
		Stream.of(simpleMailMessages).forEach(this::send);
	}
}
