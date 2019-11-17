package stand.model.connection;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import stand.model.StandData;
import stand.model.serialization.IOFunctions;

public class MailSender {
	
	private static final Logger log4j = LogManager.getLogger(MailSender.class.getName());
		
	static String userName = "<gmail address>";
	static String password = "<password>";
	
	private StandData standData;
	
	public MailSender() {
		super();
		this.standData = StandData.getInstance();
	}
		
		
	public void sendEmail(File[] files, String text, String recipient) {
	System.out.println("címzett: "+recipient);
	// Setting gmail smtp properties
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
	
	// Check the authentication
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		});
	
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(userName));
		
		// recipient's address
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
			
		
		// add the subject
			message.setSubject(standData.getToday().toString() + " stand");
		
			Multipart multipart = new MimeMultipart();
		
		// add the body message
			BodyPart bodypart = new MimeBodyPart();
			bodypart.setText(text);
			multipart.addBodyPart(bodypart);
		
		// attach file
			MimeBodyPart mimeBodyPart1 = new MimeBodyPart();
			MimeBodyPart mimeBodyPart2 = new MimeBodyPart();
			mimeBodyPart1.attachFile(files[0]);
			mimeBodyPart2.attachFile(files[1]);
			multipart.addBodyPart(mimeBodyPart1);
			multipart.addBodyPart(mimeBodyPart2);
		
			message.setContent(multipart);
		
			Transport.send(message);
		
			System.out.println("Email sent successfully");
		
		
		} catch (AddressException e) {
			log4j.error(exceptionStackTraceToString(e));
			e.printStackTrace();
		} catch (MessagingException e) {
			log4j.error(exceptionStackTraceToString(e));
			e.printStackTrace();
		} catch (IOException e) {
			log4j.error(exceptionStackTraceToString(e));
			e.printStackTrace();
		}
	}


	public static void setUserName(String userName) {
		MailSender.userName = userName;
	}
	
	public String exceptionStackTraceToString(Exception e) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps);
		ps.close();
		return baos.toString();
	}




	
}
