package com.deltasmarttech.companyorganization.services;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;


    public void sendVerificationEmail(String to, String verificationCode) {

        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", "techdeltasmart");
        props.put("mail.smtp.password", "iobr gtrt rplf iger\n");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress("techdeltasmart"));
            InternetAddress toAddress = new InternetAddress(to);
            message.addRecipient(MimeMessage.RecipientType.TO, toAddress);

            message.setSubject("Delta Smart Tech - Verification Code");
            message.setText(verificationCode);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, "techdeltasmart", "iobr gtrt rplf iger\n");
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}
