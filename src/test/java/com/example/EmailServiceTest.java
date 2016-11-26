package com.example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.assertj.core.data.Index;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class EmailServiceTest {
	@Autowired
	Sink sink;
	@MockBean
	MailSender mailSender;

	@Test
	public void sendWelcomeEmail() throws Exception {
		CustomerCreateEvent event = new CustomerCreateEvent();
		event.setId("foo");
		event.setName("John");
		event.setEmail("john@example.com");
		sink.input().send(MessageBuilder.withPayload(event).build());
		ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor
				.forClass(SimpleMailMessage.class);
		verify(mailSender).send(captor.capture());

		SimpleMailMessage mailMessage = captor.getValue();
		assertThat(mailMessage).isNotNull();
		assertThat(mailMessage.getFrom()).isEqualTo("noreply@example.com");
		assertThat(mailMessage.getTo()).contains("john@example.com", Index.atIndex(0));
		assertThat(mailMessage.getSubject()).isEqualTo("Welcome to JJUG Shop");
		assertThat(mailMessage.getText()).isEqualTo("Welcome John!!");
	}

}