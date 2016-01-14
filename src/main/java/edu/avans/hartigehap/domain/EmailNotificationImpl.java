package edu.avans.hartigehap.domain;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.*;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class EmailNotificationImpl {
	
	private MailSender mailSender;
	private SimpleMailMessage templateMessage;
	
	public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setTemplateMessage(SimpleMailMessage templateMessage) {
        this.templateMessage = templateMessage;
    }
	
	public Boolean doMakeEmailRequest(String address, String content) {
		System.out.println("Sending email notification to " + address + " with message " + content);
		
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost("smtp.gmail.com");

		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		try {
			helper.setTo("vadiemjanssens@gmail.com");
			helper.setText("Thank you for ordering!");
			sender.send(message);
		} catch (MessagingException ex) {
			System.err.println(ex.getMessage());
		}
		
		return true;
	}	
}
