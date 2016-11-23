package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class EmailService {
	private final Logger log = LoggerFactory.getLogger(EmailService.class);
	private final MailSender mailSender;

	public EmailService(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	@StreamListener(Sink.INPUT)
	public void sendWelcomeEmail(CustomerCreateEvent event) {
		log.info("send welcome mail for {}", event);

		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(event.getEmail());
		email.setFrom("noreply@example.com");
		email.setSubject("Welcome to JJUG Shop");
		email.setText("Welcome " + event.getName() + "!!");
		mailSender.send(email);
	}
}
