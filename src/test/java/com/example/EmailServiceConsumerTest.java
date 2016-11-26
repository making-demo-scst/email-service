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
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(ids = "com.example:customer-service", workOffline = true)
public class EmailServiceConsumerTest {
	@MockBean
	MailSender mailSender;
	@Autowired
	StubTrigger stubTrigger;

	@Test
	public void sendWelcomeEmail() throws Exception {
		stubTrigger.trigger("create-customer");
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